package com.venia.core.models.commerce.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=/bin/venia/test"
        })
public class InstagramGraphAPIServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramGraphAPIServlet.class);
    private static final String API_VERSION = "v21.0";

    private final String accessToken = "EAANWVbfnSJABO0ZC6SaBAkNsMeiNk5z8Fi9t9KUt715yPAWxCRNwipCuKbk9ZAANJeWKVb1EdpKaZASPF8sK0TzCXb7GFStb1xFl8HTlNZBxI75GokhQOviskN4d7pWdcnId8hmgHrVj4fRteXIoUZAyoc4WkZCced1hw6NouVRsrBFeXPXop94Dbcayoiu54z";
    private final String instagramAccountId = "17841471407996353";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String damPath = request.getParameter("damPath");
        String caption = request.getParameter("caption");
        String mediaType = request.getParameter("media_type");

        if (damPath == null || damPath.isEmpty() || mediaType == null || mediaType.isEmpty()) {
            respondWithError(response, SlingHttpServletResponse.SC_BAD_REQUEST, "Missing parameters: damPath or media_type");
            return;
        }

        try {
            if (!isImagePublished(request, damPath)) {
                respondWithError(response, SlingHttpServletResponse.SC_BAD_REQUEST, "Image is not published.");
                return;
            }

            String publishUrl = "https://publish-p127270-e1239469.adobeaemcloud.com" + damPath;
            String creationId = handleMediaCreation(publishUrl, caption, mediaType);

            if (creationId != null) {
                publishMedia(creationId);
                respondWithSuccess(response, "Shared successfully!");
            } else {
                respondWithError(response, SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create or publish media.");
            }
        } catch (Exception e) {
            LOG.error("Error interacting with Instagram API", e);
            respondWithError(response, SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    private String handleMediaCreation(String publishUrl, String caption, String mediaType) throws Exception {
        switch (mediaType.toUpperCase()) {
            case "IMAGES":
                return createImageContainer(publishUrl, caption);
            case "REELS":
                return createReelsContainer(publishUrl, caption);
            case "IMAGE_STORIES":
                return createStoryImageContainer(publishUrl);
            case "VIDEO_STORIES":
                return createStoryVideoContainer(publishUrl);
            default:
                throw new IllegalArgumentException("Unsupported media type: " + mediaType);
        }
    }

    private String createImageContainer(String publishUrl, String caption) throws Exception {
        return sendPostRequest(getMediaEndpoint(), buildPayload("image_url", publishUrl, "caption", caption), "id", prepareHeaders());
    }

    private String createReelsContainer(String publishUrl, String caption) throws Exception {
        return sendPostRequest(getMediaEndpoint(), buildPayload("media_type", "REELS", "video_url", publishUrl, "caption", caption), "id", prepareHeaders());
    }

    private String createStoryImageContainer(String imageUrl) throws Exception {
        return sendPostRequest(getMediaEndpoint(), buildPayload("image_url", imageUrl, "media_type", "STORIES"), "id", prepareHeaders());
    }

    private String createStoryVideoContainer(String videoUrl) throws Exception {
        return sendPostRequest(getMediaEndpoint(), buildPayload("video_url", videoUrl, "media_type", "STORIES"), "id", prepareHeaders());
    }

    private void publishMedia(String creationId) throws Exception {
        sendPostRequest(
                "https://graph.facebook.com/" + API_VERSION + "/" + instagramAccountId + "/media_publish",
                buildPayload("creation_id", creationId),
                null,
                prepareHeaders()
        );
    }

    private boolean isImagePublished(SlingHttpServletRequest request, String damPath) {
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);

            if (session != null) {
                Node assetNode = session.getNode(damPath + "/jcr:content");
                if (assetNode != null && assetNode.hasProperty("cq:lastReplicationAction")) {
                    String replicationAction = assetNode.getProperty("cq:lastReplicationAction").getString();
                    return "Activate".equals(replicationAction);
                }
            }
        } catch (Exception e) {
            LOG.error("Error checking publication status for {}", damPath, e);
        }
        return false;
    }

    private Map<String, String> prepareHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private String getMediaEndpoint() {
        return "https://graph.facebook.com/" + API_VERSION + "/" + instagramAccountId + "/media";
    }

    private String sendPostRequest(String endpoint, String payload, String responseKey, Map<String, String> headers) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        headers.forEach(conn::setRequestProperty);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }
        Thread.sleep(20000);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP request failed with response code: " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return responseKey == null ? null : parseResponse(response.toString(), responseKey);
        }
    }

    private String buildPayload(String... args) throws JSONException {
        JSONObject payload = new JSONObject();
        for (int i = 0; i < args.length; i += 2) {
            payload.put(args[i], args[i + 1]);
        }
        return payload.toString();
    }

    private String parseResponse(String response, String key) throws Exception {
        JsonNode rootNode = new ObjectMapper().readTree(response);
        JsonNode valueNode = rootNode.path(key);
        if (!valueNode.isMissingNode()) {
            return valueNode.asText();
        }
        throw new IllegalArgumentException("Key not found in response: " + key);
    }

    private void respondWithError(SlingHttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
    }

    private void respondWithSuccess(SlingHttpServletResponse response, String message) throws IOException {
        response.setStatus(SlingHttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}
