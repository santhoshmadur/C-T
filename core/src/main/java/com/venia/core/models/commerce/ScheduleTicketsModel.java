package com.venia.core.models.commerce;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ScheduleTicketsModel {

    @ValueMapValue
    private String heroBgImage;

    @ValueMapValue
    private String heroTitle;

    @ValueMapValue
    private String cardTitle;

    @ValueMapValue
    private String cardDescriptionOne;

    @ValueMapValue
    private String cardDescriptionTwo;

    @ValueMapValue
    private String ctaLabel;

    @ChildResource(name = "gameCards")
    private List<GameCardsModel> gameCards;

    public String getHeroBgImage() {
        return heroBgImage;
    }

    public String getHeroTitle() {
        return heroTitle;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardDescriptionOne() {
        return cardDescriptionOne;
    }

    public String getCardDescriptionTwo() {
        return cardDescriptionTwo;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public List<GameCardsModel> getGameCards() {
        return gameCards;
    }

}

