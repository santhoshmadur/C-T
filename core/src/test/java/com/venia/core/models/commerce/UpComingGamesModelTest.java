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
class UpComingGamesModelTest {

    private final AemContext context = new AemContext();

    private static final String COMPONENT_PATH = "/content/upcomingGames";

    private UpComingGamesModel model;

    @BeforeEach
    void setUp() {

        context.create().resource(COMPONENT_PATH,
                "title", "Upcoming Games",
                "ctaLabel", "View Schedule",
                "ctaIcon", "icon-calendar",
                "target", "_self",
                "ctaLink", "/content/games-page"
        );

        context.create().resource(COMPONENT_PATH + "/gameCards/1",
                "image", "/content/dam/image1.jpg",
                "scheduleType", "League",
                "teamName", "Team Alpha",
                "date", "2025-02-10",
                "time", "6:00 PM",
                "ctaLabel", "Book Now"
        );

        context.create().resource(COMPONENT_PATH + "/gameCards/2",
                "image", "/content/dam/image2.jpg",
                "scheduleType", "Exhibition",
                "teamName", "Team Beta",
                "date", "2025-02-15",
                "time", "8:00 PM",
                "ctaLabel", "Buy Tickets"
        );
        Resource headerResource = context.resourceResolver().getResource(COMPONENT_PATH);
        context.currentResource(headerResource);
        model = context.request().adaptTo(UpComingGamesModel.class);
    }

    @Test
    void testUpComingGamesModel() {

        assertNotNull(model);
        assertEquals("Upcoming Games", model.getTitle());
        assertEquals("View Schedule", model.getCtaLabel());
        assertEquals("icon-calendar", model.getCtaIcon());
        assertEquals("_self", model.getTarget());
        assertEquals("/content/games-page.html", model.getCtaLink());

        List<UpComingGamesModel.GameCardsModel> gameCards = model.getGameCards();
        assertNotNull(gameCards);
        assertEquals(2, gameCards.size());

        UpComingGamesModel.GameCardsModel firstCard = gameCards.get(0);
        assertEquals("/content/dam/image1.jpg", firstCard.getImage());
        assertEquals("League", firstCard.getScheduleType());
        assertEquals("Team Alpha", firstCard.getTeamName());
        assertEquals("2025-02-10", firstCard.getDate());
        assertEquals("6:00 PM", firstCard.getTime());
        assertEquals("Book Now", firstCard.getCtaLabel());

        UpComingGamesModel.GameCardsModel secondCard = gameCards.get(1);
        assertEquals("/content/dam/image2.jpg", secondCard.getImage());
        assertEquals("Exhibition", secondCard.getScheduleType());
        assertEquals("Team Beta", secondCard.getTeamName());
        assertEquals("2025-02-15", secondCard.getDate());
        assertEquals("8:00 PM", secondCard.getTime());
        assertEquals("Buy Tickets", secondCard.getCtaLabel());
    }
}
