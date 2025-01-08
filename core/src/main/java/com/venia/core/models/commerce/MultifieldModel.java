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
public class MultifieldModel {

    @ChildResource(name = "main")
    private List<Main> main;

    public List<Main> getMain() {
        return main;
    }
    @Model(
            adaptables = {Resource.class,SlingHttpServletRequest.class},
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class Main{

        @ValueMapValue
        private String listFrom;

        public String getListFrom() {
            return listFrom;
        }

        @ChildResource(name = "imageLink")
        private List<ImageLink> imageLink;

        @ChildResource(name = "textLink")
        private List<TextLink> textLink;

        public List<ImageLink> getImageLink() {
            return imageLink;
        }

        public List<TextLink> getTextLink() {
            return textLink;
        }
    }
    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class ImageLink {

        @ValueMapValue
        private String image;

        @ValueMapValue
        private String linkTarget;

        @ValueMapValue
        private String linkURL;

        public String getImage() {
            return image;
        }

        public String getLinkTarget() {
            return linkTarget;
        }

        public String getLinkURL() {
            return linkURL;
        }
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class TextLink {

        @ValueMapValue
        private String text;

        @ValueMapValue
        private String linkTarget;

        @ValueMapValue
        private String linkURL;

        public String getText() {
            return text;
        }

        public String getLinkTarget() {
            return linkTarget;
        }

        public String getLinkURL() {
            return linkURL;
        }
    }
}
