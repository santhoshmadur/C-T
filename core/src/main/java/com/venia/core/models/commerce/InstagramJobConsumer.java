package com.venia.core.models.commerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @Override
    public JobResult process(Job job) {
        String publishUrl = job.getProperty("publishUrl", String.class);
        String caption = job.getProperty("caption", String.class);
        String media_type = job.getProperty("mediaType", String.class);
        try {
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
                LOG.error("{\"message\": \"Shared successfully!\"}");
            } else {
                LOG.error("{\"error\": \"Failed to create or publish media.\"}");
            }
        } catch (Exception e) {
            LOG.error("Error interacting with Instagram API", e);
        }
        return JobResult.OK;
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

}

