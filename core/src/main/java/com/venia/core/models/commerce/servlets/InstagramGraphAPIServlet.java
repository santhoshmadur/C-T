package com.venia.core.models.commerce.servlets;

import com.venia.core.models.commerce.config.InstagramConfig;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Component(service = Servlet.class, property = {
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths=/bin/instagram/post",
})
@Designate(ocd = InstagramConfig.class)
public class InstagramGraphAPIServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramGraphAPIServlet.class);
    private volatile String accessToken;
    private volatile String instagramAccountId;

    @Activate
    @Deactivate
    protected void activateDeactivate(InstagramConfig config) {
        accessToken = config.accessToken();
        instagramAccountId = config.instagramAccountId();
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String imageUrl = request.getParameter("imageUrl");
        String caption = request.getParameter("caption");

        if (imageUrl == null || caption == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing parameters: imageUrl or caption");
            return;
        }

        try {
            // Step 1: Create Media Container
            String creationId = createMediaContainer(imageUrl, caption);

            if (creationId != null) {
                // Step 2: Publish Media
                publishMedia(creationId);
                response.setStatus(SlingHttpServletResponse.SC_OK);
                response.getWriter().write("Media Published Successfully");
            } else {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to create or publish media.");
            }
        } catch (Exception e) {
            LOG.error("Error interacting with Instagram API", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    private String createMediaContainer(String imageUrl, String caption) throws Exception {
        String endpoint = "https://graph.facebook.com/v21.0/" + instagramAccountId + "/media";
        String payload = String.format(
                "{\"image_url\":\"%s\",\"caption\":\"%s\",\"access_token\":\"%s\"}",
                imageUrl, caption, accessToken
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

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }

        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            if (responseKey != null && response.indexOf(responseKey) != -1) {
                return parseResponse(response.toString(), responseKey);
            }
            return null;
        }
    }

    private String parseResponse(String response, String key) {
        int startIndex = response.indexOf(key) + key.length() + 3;
        int endIndex = response.indexOf("\"", startIndex);
        return response.substring(startIndex, endIndex);
    }
}
