package com.venia.core.models.commerce;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=/bin/venia/instagram"
        })
public class InstagramJobServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramJobServlet.class);

    @Reference
    private JobManager jobManager;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, IOException {
        response.setContentType("application/json");

        String damPath = request.getParameter("damPath");
        String caption = request.getParameter("caption");
        String mediaType = request.getParameter("media_type");

        if (StringUtils.isBlank(damPath) || StringUtils.isBlank(mediaType)) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing parameters: damPath or media_type\"}");
            return;
        }
        boolean isPublished = isImagePublished(request, damPath);
        if (!isPublished) {
            LOG.error("{\"error\": \"Image is not published.\"}");
            return;
        }

        String publishUrl = "https://publish-p127270-e1239469.adobeaemcloud.com" + damPath;

        try {
            Map<String, Object> jobProperties = new HashMap<>();
            jobProperties.put("publishUrl", publishUrl);
            jobProperties.put("caption", caption);
            jobProperties.put("mediaType", mediaType);

            jobManager.addJob("com/venia/instagram/publish", jobProperties);

            response.setStatus(SlingHttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Job submitted successfully.\"}");
        } catch (Exception e) {
            LOG.error("Error submitting Instagram job", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Job submission failed.\"}");
        }
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
}
