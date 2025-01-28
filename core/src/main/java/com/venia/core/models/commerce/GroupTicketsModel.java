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
public class GroupTicketsModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ChildResource(name = "seasonTickets")
    private List<SeasonTicketsModel> seasonTickets;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String textOnImage;

    @ValueMapValue
    private String secondDescription;

    @ChildResource(name = "titleAndDescription")
    private List<TitleAndDescriptionModel> titleAndDescription;

    @ValueMapValue
    private String ctaLabel;

    @ValueMapValue
    private String phoneIcon;

    @ValueMapValue
    private String mailIcon;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<SeasonTicketsModel> getSeasonTickets() {
        return seasonTickets;
    }

    public String getImage() {
        return image;
    }

    public String getTextOnImage() {
        return textOnImage;
    }

    public String getSecondDescription() {
        return secondDescription;
    }

    public List<TitleAndDescriptionModel> getTitleAndDescription() {
        return titleAndDescription;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public String getPhoneIcon() {
        return phoneIcon;
    }

    public String getMailIcon() {
        return mailIcon;
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class SeasonTicketsModel {

        @ValueMapValue
        private String image;

        @ValueMapValue
        private String textOnImage;

        @ValueMapValue
        private String startingPrice;

        @ValueMapValue
        private String description;

        @ValueMapValue
        private String ctaLabel;

        public String getImage() {
            return image;
        }

        public String getTextOnImage() {
            return textOnImage;
        }

        public String getStartingPrice() {
            return startingPrice;
        }

        public String getDescription() {
            return description;
        }

        public String getCtaLabel() {
            return ctaLabel;
        }

        public String getTicketStatus(){
            if(ctaLabel.equalsIgnoreCase("SOLD OUT")) {
                return "sold-out";
            }else {
                return "buy-tickets";
            }
        }
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class TitleAndDescriptionModel {

        @ValueMapValue
        private String title;

        @ValueMapValue
        private String description;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }


}
