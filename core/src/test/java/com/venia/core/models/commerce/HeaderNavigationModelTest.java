package com.venia.core.models.commerce;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class HeaderNavigationModelTest {

    private final AemContext context = new AemContext();
    private HeaderNavigationModel headerNavigationModel;

    @BeforeEach
    void setUp() {
        // Create mock content
        context.create().resource("/content/header",
                "headerLogo", "/content/dam/logo.png",
                "headerLogoLink", "/content/site/home",
                "linkTarget", true,
                "searchText", "Search",
                "searchIcon", "search-icon.png",
                "addToBagText", "Add to Bag",
                "addToBagIcon", "add-to-bag-icon.png");

        context.create().resource("/content/header/navLinks/item1",
                "navTitle", "Home",
                "navLink", "/content/site/home",
                "linkTarget", "_self");

        context.create().resource("/content/header/navLinks/item2",
                "navTitle", "Contact",
                "navLink", "https://external.com",
                "linkTarget", "_blank");

        // Adapt the model
        Resource headerResource = context.resourceResolver().getResource("/content/header");
        context.currentResource(headerResource);
        headerNavigationModel = context.request().adaptTo(HeaderNavigationModel.class);
    }

    @Test
    void testHeaderLogo() {
        assertNotNull(headerNavigationModel);
        assertEquals("/content/dam/logo.png", headerNavigationModel.getHeaderLogo());
    }

    @Test
    void testHeaderLogoLink() {
        assertEquals("/content/site/home.html", headerNavigationModel.getHeaderLogoLink());
    }

    @Test
    void testLinkTarget() {
        assertTrue(headerNavigationModel.getLinkTarget());
    }

    @Test
    void testNavLinks() {
        assertNotNull(headerNavigationModel.getNavLinks());
        assertEquals(2, headerNavigationModel.getNavLinks().size());

        HeaderNavigationModel.NavLinksModel firstNavLink = headerNavigationModel.getNavLinks().get(0);
        assertEquals("Home", firstNavLink.getNavTitle());
        assertEquals("/content/site/home.html", firstNavLink.getNavLink());
        assertEquals("_self", firstNavLink.getLinkTarget());

        HeaderNavigationModel.NavLinksModel secondNavLink = headerNavigationModel.getNavLinks().get(1);
        assertEquals("Contact", secondNavLink.getNavTitle());
        assertEquals("https://external.com", secondNavLink.getNavLink());
        assertEquals("_blank", secondNavLink.getLinkTarget());
    }

    @Test
    void testSearchFields() {
        assertEquals("Search", headerNavigationModel.getSearchText());
        assertEquals("search-icon.png", headerNavigationModel.getSearchIcon());
    }

    @Test
    void testAddToBagFields() {
        assertEquals("Add to Bag", headerNavigationModel.getAddToBagText());
        assertEquals("add-to-bag-icon.png", headerNavigationModel.getAddToBagIcon());
    }
}
