package com.venia.core.models.commerce;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class UpComingGamesModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String ctaLabel;

    @ValueMapValue
    private String ctaIcon;

    @ValueMapValue
    private String target;

    @ValueMapValue
    private String ctaLink;

    @ChildResource(name = "gameCards")
    private List<GameCardsModel> gameCards;

    public String getTitle() {
        return title;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public String getCtaIcon() {
        return ctaIcon;
    }

    public String getTarget() {
        return target;
    }

    public String getCtaLink() {
        if (ctaLink.contains("/content/")){
            return ctaLink+".html";
        }else {
            return ctaLink;
        }
    }

    public List<GameCardsModel> getGameCards() {
        return gameCards;
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class GameCardsModel {

        @ValueMapValue
        private String image;

        @ValueMapValue
        private String scheduleType;

        @ValueMapValue
        private String teamName;

        @ValueMapValue
        private String date;

        @ValueMapValue
        private String time;

        @ValueMapValue
        private String ctaLabel;

        public String getImage() {
            return image;
        }

        public String getScheduleType() {
            return scheduleType;
        }

        public String getTeamName() {
            return teamName;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getCtaLabel() {
            return ctaLabel;
        }

    }
}

