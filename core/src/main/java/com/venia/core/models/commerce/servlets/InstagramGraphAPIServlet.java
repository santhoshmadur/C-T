package com.venia.core.models.commerce.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
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
import java.util.Scanner;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=/bin/venia/servlet"
        })
public class InstagramGraphAPIServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramGraphAPIServlet.class);
    private final String accessToken = "EAANWVbfnSJABO0ZC6SaBAkNsMeiNk5z8Fi9t9KUt715yPAWxCRNwipCuKbk9ZAANJeWKVb1EdpKaZASPF8sK0TzCXb7GFStb1xFl8HTlNZBxI75GokhQOviskN4d7pWdcnId8hmgHrVj4fRteXIoUZAyoc4WkZCced1hw6NouVRsrBFeXPXop94Dbcayoiu54z";
    private final String instagramAccountId = "17841471407996353";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String damPath = request.getParameter("damPath");
        String caption = request.getParameter("caption");
        String media_type = request.getParameter("media_type");

        if (damPath == null || damPath.isEmpty() || media_type == null || media_type.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing parameters: damPath or caption\"}");
            return;
        }

        try {
/*            boolean isPublished = isImagePublished(request, damPath);
            if (!isPublished) {
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Image is not published.\"}");
                return;
            }*/

            String publishUrl = "https://publish-p127270-e1239469.adobeaemcloud.com" + damPath;
            String creationId=null;
            if(media_type.equalsIgnoreCase("IMAGES")) {
                 creationId = createImageContainer(publishUrl, caption);
            } else if (media_type.equalsIgnoreCase("REELS")) {
                 creationId = createReelsContainer(publishUrl, caption);
            } else if (media_type.equalsIgnoreCase("IMAGE_STORIES")) {
                 creationId = createStoryImageContainer(publishUrl);
            } else if (media_type.equalsIgnoreCase("VIDEO_STORIES")) {
                 creationId = createStoryVideoContainer(publishUrl);
            }

            if (creationId != null) {
                publishMedia(creationId);
                response.setStatus(SlingHttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Image shared successfully!\"}");
            } else {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Failed to create or publish media.\"}");
            }
        } catch (Exception e) {
            LOG.error("Error interacting with Instagram API", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    private String createImageContainer(String publishUrl, String caption) throws Exception {
        String endpoint = "https://graph.facebook.com/v21.0/" + instagramAccountId + "/media";
        String payload = String.format(
                "{\"image_url\":\"%s\",\"caption\":\"%s\",\"access_token\":\"%s\"}",
                publishUrl, caption, accessToken
        );
        return sendPostRequest(endpoint, payload, "id");
    }
    private String createReelsContainer(String publishUrl, String caption) throws Exception {
        String endpoint = "https://graph.facebook.com/v21.0/" + instagramAccountId + "/media";
        String payload = String.format(
                "{\"media_type\":\"REELS\",\"video_url\":\"%s\",\"caption\":\"%s\",\"access_token\":\"%s\"}",
                publishUrl, caption, accessToken
        );
        return sendPostRequest(endpoint, payload, "id");
    }
    private String createStoryImageContainer(String imageUrl) throws Exception {
        String endpoint = "https://graph.facebook.com/v21.0/" + instagramAccountId + "/media";
        String payload = String.format(
                "{\"image_url\":\"%s\",\"media_type\":\"STORIES\",\"access_token\":\"%s\"}",
                imageUrl, accessToken
        );
        return sendPostRequest(endpoint, payload, "id");
    }
    private String createStoryVideoContainer(String videoUrl) throws Exception {
        String endpoint = "https://graph.facebook.com/v21.0/" + instagramAccountId + "/media";
        String payload = String.format(
                "{\"video_url\":\"%s\",\"media_type\":\"STORIES\",\"access_token\":\"%s\"}",
                videoUrl, accessToken
        );
        return sendPostRequest(endpoint, payload, "id");
    }

    private void publishMedia(String creationId) throws Exception {
        String endpoint = "https://graph.facebook.com/v21.0/" + instagramAccountId + "/media_publish";
        String payload = String.format(
                "{\"creation_id\":\"%s\",\"access_token\":\"%s\"}",
                creationId, accessToken
        );
        sendPostRequest(endpoint, payload, null);
    }

    private String sendPostRequest(String endpoint, String payload, String responseKey) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        // Wait 10 seconds after opening the connection
        Thread.sleep(10000);

        // Send the payload
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }

        // Wait 10 seconds after sending the payload
        Thread.sleep(10000);

        // Check the response code
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP request failed with response code: " + responseCode);
        }

        // Wait 10 seconds after checking the response code
        Thread.sleep(10000);

        // Read the response
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Wait 10 seconds before parsing the response
            Thread.sleep(10000);

            // Parse and return the response key value
            if (responseKey != null) {
                return parseResponse(response.toString(), responseKey);
            }
            return null;
        }
    }

    private String parseResponse(String response, String key) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode valueNode = rootNode.path(key);
        if (!valueNode.isMissingNode()) {
            return valueNode.asText();
        }
        throw new IllegalArgumentException("Key not found in response: " + key);
    }


    private boolean isImagePublished(SlingHttpServletRequest request, String damPath) {
        try {
            Resource resource = request.getResource();
            ResourceResolver resourceResolver = resource.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            Node assetNode = null;
            if (session != null) {
                assetNode = session.getNode(damPath + "/jcr:content");
            } else {
                LOG.error("Session is null");
            }
            if (assetNode != null && assetNode.hasProperty("cq:lastReplicationAction")) {
                String replicationAction = assetNode.getProperty("cq:lastReplicationAction").getString();
                return "Activate".equals(replicationAction);
            } else {
                LOG.error("Asset node or property cq:lastReplicationAction is missing");
            }
        } catch (Exception e) {
            LOG.error("Error checking publication status for {}", damPath, e);
        }
        return false;
    }
}
