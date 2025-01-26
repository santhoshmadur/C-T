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
public class NewsAndUpdatesModel {

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

    @ChildResource(name = "newsCards")
    private List<NewsCardsModel> newsCards;

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

    public List<NewsCardsModel> getNewsCards() {
        return newsCards;
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class NewsCardsModel {

        @ValueMapValue
        private String image;

        @ValueMapValue
        private String category;

        @ValueMapValue
        private String description;

        @ValueMapValue
        private String date;

        @ValueMapValue
        private String time;

        public String getImage() {
            return image;
        }

        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }
    }
}

