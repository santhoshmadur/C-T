package com.venia.core.models.commerce;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(AemContextExtension.class)
class NewsAndUpdatesModelTest {

    private final AemContext context = new AemContext();

    private static final String COMPONENT_PATH = "/content/newsAndUpdates";

    private NewsAndUpdatesModel model;

    @BeforeEach
    void setUp() {

        context.create().resource(COMPONENT_PATH,
                "title", "News and Updates",
                "ctaLabel", "Read More",
                "ctaIcon", "icon-link",
                "target", "_blank",
                "ctaLink", "/content/sample-page"
        );

        context.create().resource(COMPONENT_PATH + "/newsCards/1",
                "image", "/content/dam/image1.jpg",
                "category", "Technology",
                "description", "Tech news description",
                "date", "2025-01-01",
                "time", "10:00 AM"
        );

        context.create().resource(COMPONENT_PATH + "/newsCards/2",
                "image", "/content/dam/image2.jpg",
                "category", "Business",
                "description", "Business news description",
                "date", "2025-01-02",
                "time", "2:00 PM"
        );
        Resource headerResource = context.resourceResolver().getResource(COMPONENT_PATH);
        context.currentResource(headerResource);
        model = context.request().adaptTo(NewsAndUpdatesModel.class);
    }

    @Test
    void testNewsAndUpdatesModel() {

        assertNotNull(model);
        assertEquals("News and Updates", model.getTitle());
        assertEquals("Read More", model.getCtaLabel());
        assertEquals("icon-link", model.getCtaIcon());
        assertEquals("_blank", model.getTarget());
        assertEquals("/content/sample-page.html", model.getCtaLink());

        List<NewsAndUpdatesModel.NewsCardsModel> newsCards = model.getNewsCards();
        assertNotNull(newsCards);
        assertEquals(2, newsCards.size());

        NewsAndUpdatesModel.NewsCardsModel firstCard = newsCards.get(0);
        assertEquals("/content/dam/image1.jpg", firstCard.getImage());
        assertEquals("Technology", firstCard.getCategory());
        assertEquals("Tech news description", firstCard.getDescription());
        assertEquals("2025-01-01", firstCard.getDate());
        assertEquals("10:00 AM", firstCard.getTime());

        NewsAndUpdatesModel.NewsCardsModel secondCard = newsCards.get(1);
        assertEquals("/content/dam/image2.jpg", secondCard.getImage());
        assertEquals("Business", secondCard.getCategory());
        assertEquals("Business news description", secondCard.getDescription());
        assertEquals("2025-01-02", secondCard.getDate());
        assertEquals("2:00 PM", secondCard.getTime());
    }
}
