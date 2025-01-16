package com.venia.core.models.commerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = JobConsumer.class,
        immediate = true,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=com/venia/instagram/publish"
        }
)
public class InstagramJobConsumer implements JobConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramJobConsumer.class);

    private final String instagramAccountId = "17841471407996353";
    private final String accessToken = "EAANWVbfnSJABO0ZC6SaBAkNsMeiNk5z8Fi9t9KUt715yPAWxCRNwipCuKbk9ZAANJeWKVb1EdpKaZASPF8sK0TzCXb7GFStb1xFl8HTlNZBxI75GokhQOviskN4d7pWdcnId8hmgHrVj4fRteXIoUZAyoc4WkZCced1hw6NouVRsrBFeXPXop94Dbcayoiu54z";
    private static final String API_VERSION = "v21.0";
    private String mediaType = null;

    @Override
    public JobResult process(Job job) {
        String publishUrl = job.getProperty("publishUrl", String.class);
        String caption = job.getProperty("caption", String.class);
        mediaType = job.getProperty("mediaType", String.class);
        try {
            String creationId = handleMediaCreation(publishUrl, caption, mediaType);

            if (creationId != null) {
                publishMedia(creationId);

            }
        } catch (Exception e) {
            LOG.error("Error interacting with Instagram API", e);

        }
        return JobResult.OK;
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
        if(mediaType.equalsIgnoreCase("REELS") || mediaType.equalsIgnoreCase("VIDEO_STORIES")) {
            Thread.sleep(20000);
        }
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

