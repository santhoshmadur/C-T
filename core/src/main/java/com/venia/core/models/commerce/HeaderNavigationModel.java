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
public class HeaderNavigationModel {

    @ValueMapValue
    private String headerLogo;

    @ValueMapValue
    private String headerLogoLink;

    @ValueMapValue
    private Boolean linkTarget;

    @ChildResource(name = "navLinks")
    private List<NavLinksModel> navLinks;

    @ValueMapValue
    private String searchText;

    @ValueMapValue
    private String searchIcon;

    @ValueMapValue
    private String addToBagText;

    @ValueMapValue
    private String addToBagIcon;

    public String getHeaderLogo() {
        return headerLogo;
    }

    public String getHeaderLogoLink() {
        if (headerLogoLink.contains("/content/")){
            return headerLogoLink+".html";
        }else {
            return headerLogoLink;
        }
    }

    public Boolean getLinkTarget() {
        return linkTarget;
    }

    public List<NavLinksModel> getNavLinks() {
        return navLinks;
    }

    public String getSearchText() {
        return searchText;
    }

    public String getSearchIcon() {
        return searchIcon;
    }

    public String getAddToBagText() {
        return addToBagText;
    }

    public String getAddToBagIcon() {
        return addToBagIcon;
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class NavLinksModel {

        @ValueMapValue
        private String navTitle;

        @ValueMapValue
        private String navLink;

        @ValueMapValue
        private String linkTarget;

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
            return linkTarget;
        }
    }
}

