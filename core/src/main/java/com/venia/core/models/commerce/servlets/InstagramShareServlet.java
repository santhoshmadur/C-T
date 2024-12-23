package com.venia.core.models.commerce.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import com.day.cq.dam.api.Asset;
import javax.servlet.Servlet;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/example/shareToInstagram",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST
        }
)
public class InstagramShareServlet extends SlingAllMethodsServlet {

    private static final String ACCESS_TOKEN = "EAANWVbfnSJABO8BUrMra7yJ8WWI5Gdey9ZAQmBGS29BU7hKg2NA6kJnyZB9OdflRZCHN5d3WuEvIVFZCHt0vHesK6PO6jx2i2eXfJge6G7h3GZBEbQu2munXyUJA0bwCnNSkogzy0o9yC0ZA1TXZCgGZAvjK7ZAeaNTEvx70mmruJAcEIiwCxGAOqOLBnk3skM7BdQes5E1Jwypm1wW9IvUZB4KI5LmQZDZD";
    private static final String IG_BUSINESS_ACCOUNT_ID = "17841471407996353";
    private static final String GRAPH_API_BASE_URL = "https://graph.facebook.com/v21.0/";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String damPath = request.getParameter("damPath");
        String caption = request.getParameter("caption");

        if (damPath == null || caption == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing required parameters: 'damPath' or 'caption'.");
            return;
        }
        try {
            Resource resource = request.getResource();
            ResourceResolver resourceResolver = resource.getResourceResolver();
            Resource damResource = resourceResolver.getResource(damPath);
            if (damResource == null) {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("DAM asset not found at: " + damPath);
                return;
            }

            Asset asset = damResource.adaptTo(Asset.class);
            if (asset == null) {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Unable to adapt DAM resource to Asset at: " + damPath);
                return;
            }

            String publicImageUrl = "https://publish-p127270-e1239469.adobeaemcloud.com/" + asset.getPath();

            String containerId = uploadMedia(publicImageUrl, caption);

            publishMedia(containerId);

            response.setStatus(SlingHttpServletResponse.SC_OK);
            response.getWriter().write("Image shared to Instagram successfully.");

        }
        catch(Exception e){
            response.getWriter().write("Image not uploaded");

        }
    }

    private String uploadMedia(String publicImageUrl, String caption) throws Exception {
        String uploadUrl = GRAPH_API_BASE_URL + IG_BUSINESS_ACCOUNT_ID + "/media?access_token=" + ACCESS_TOKEN;
        HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonPayload = "{\"image_url\": \"" + publicImageUrl + "\", \"caption\": \"" + caption + "\"}";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonPayload.getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = connection.getInputStream()) {
                String response = new String(inputStream.readAllBytes());
                // Extract "container_id" from response JSON (use a JSON parser)
                return "container_id_from_response"; // Replace with actual extraction logic
            }
        } else {
            // Log the error response
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                String errorResponse = new String(errorStream.readAllBytes());
                System.err.println("Error Response: " + errorResponse);
            }
            throw new RuntimeException("Failed to upload media: HTTP response code " + responseCode);
        }
    }



    private void publishMedia(String containerId) throws Exception {
        String publishUrl = GRAPH_API_BASE_URL + IG_BUSINESS_ACCOUNT_ID + "/media_publish?access_token=" + ACCESS_TOKEN;
        HttpURLConnection connection = (HttpURLConnection) new URL(publishUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonPayload = "{\"creation_id\": \"" + containerId + "\"}";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonPayload.getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed to publish media: HTTP response code " + responseCode);
        }
    }
}
