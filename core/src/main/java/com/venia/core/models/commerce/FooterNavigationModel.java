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
public class FooterNavigationModel {

    @ValueMapValue
    private String footerLogo;

    @ValueMapValue
    private String footerTarget;

    @ValueMapValue
    private String footerLink;

    @ChildResource(name = "firstColumn")
    private List<ColumnModel> firstColumn;

    @ChildResource(name = "secondColumn")
    private List<ColumnModel> secondColumn;

    @ChildResource(name = "thirdColumn")
    private List<ColumnModel> thirdColumn;

    @ChildResource(name = "fourthColumn")
    private List<ColumnModel> fourthColumn;

    @ChildResource(name = "socialIcons")
    private List<SocialIconsModel> socialIcons;

    public String getFooterLogo() {
        return footerLogo;
    }

    public String getFooterTarget() {
        return footerTarget;
    }

    public String getFooterLink() {
        if (footerLink.contains("/content/")){
            return footerLink+".html";
        }else {
            return footerLink;
        }
    }

    public List<ColumnModel> getFirstColumn() {
        return firstColumn;
    }

    public List<ColumnModel> getSecondColumn() {
        return secondColumn;
    }

    public List<ColumnModel> getThirdColumn() {
        return thirdColumn;
    }

    public List<ColumnModel> getFourthColumn() {
        return fourthColumn;
    }

    public List<SocialIconsModel> getSocialIcons() {
        return socialIcons;
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class ColumnModel {

        @ValueMapValue
        private String navTitle;

        @ValueMapValue
        private String navLink;

        @ValueMapValue
        private String navTarget;

        public String getNavTitle() {
            return navTitle;
        }

        public String getNavLink() {
            if (navLink.contains("/content/")){
                return navLink+".html";
            }else {
                return navLink;
            }
        }

        public String getLinkTarget() {
            return navTarget;
        }
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class SocialIconsModel {

        @ValueMapValue
        private String socialIcon;

        @ValueMapValue
        private String socialTarget;

        @ValueMapValue
        private String socialLink;

        public String getSocialTarget() {
            return socialTarget;
        }

        public String getSocialIcon() {
            return socialIcon;
        }

        public String getSocialLink() {
            return socialLink;
        }
    }
}