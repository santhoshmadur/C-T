package com.venia.core.models.commerce.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
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
public class InstagramGraphAPIServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramGraphAPIServlet.class);
    private volatile String accessToken="EAANWVbfnSJABO02EuTNXTHh2FLJawRjAHZAIlNSlvsY9JchTZAnDFfusBfoBwyIh2rsMcbl68ZCK8EEtzfU4eMmwcW473iaoqk9nw2it45XpBO03VC6Mri8ucei0obo7kj60OoOI9NZCOiZBTS7KlkBZCjKVfuxN7KPNqJpFoc7Jdps2mHXwc7uCZAHD6kQ2d05";
    private volatile String instagramAccountId="17841471407996353";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String damPath = request.getParameter("damPath");
        String caption = request.getParameter("caption");

        if (damPath == null || caption == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing parameters: damPath or caption");
            return;
        }

        try {
            boolean isPublished = isImagePublished(request, damPath);
            if (!isPublished) {
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Image is not published.");
                return;
            }
            String publishImpageUrl = "https://publish-p127270-e1239469.adobeaemcloud.com/"+damPath;
            String creationId = createMediaContainer(publishImpageUrl, caption);

            if (creationId != null) {
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

    private String createMediaContainer(String publishImpageUrl, String caption) throws Exception {
        String endpoint = "https://graph.facebook.com/v21.0/" + instagramAccountId + "/media";
        String payload = String.format(
                "{\"image_url\":\"%s\",\"caption\":\"%s\",\"access_token\":\"%s\"}",
                publishImpageUrl, caption, accessToken
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

    private boolean isImagePublished(SlingHttpServletRequest request, String damPath) {
        try {
            Session session = request.getResourceResolver().adaptTo(Session.class);
            Node assetNode = session.getNode(damPath);
            if (assetNode.hasProperty("cq:lastReplicationAction")) {
                String replicationAction = assetNode.getProperty("cq:lastReplicationAction").getString();
                return "Activate".equals(replicationAction);
            }
        } catch (Exception e) {
            LOG.error("Error checking publication status for {}", damPath, e);
        }
        return false;
    }
}
