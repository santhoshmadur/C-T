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
class FooterNavigationModelTest {

    private final AemContext context = new AemContext();

    private static final String COMPONENT_PATH = "/content/footer";

    private FooterNavigationModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(FooterNavigationModel.class);

        context.create().resource(COMPONENT_PATH,
                "footerLogo", "/content/dam/footer-logo.png",
                "footerTarget", "_self",
                "footerLink", "/content/footer-page"
        );

        context.create().resource(COMPONENT_PATH + "/firstColumn/1",
                "navTitle", "Home",
                "navLink", "/content/home",
                "navTarget", "_self"
        );

        context.create().resource(COMPONENT_PATH + "/secondColumn/1",
                "navTitle", "About Us",
                "navLink", "/content/about-us",
                "navTarget", "_self"
        );

        context.create().resource(COMPONENT_PATH + "/socialIcons/1",
                "socialIcon", "facebook-icon",
                "socialTarget", "_blank",
                "socialLink", "https://facebook.com"
        );

        context.create().resource(COMPONENT_PATH + "/socialIcons/2",
                "socialIcon", "twitter-icon",
                "socialTarget", "_blank",
                "socialLink", "https://twitter.com"
        );
        Resource headerResource = context.resourceResolver().getResource(COMPONENT_PATH);
        context.currentResource(headerResource);
        model = context.request().adaptTo(FooterNavigationModel.class);
    }

    @Test
    void testFooterNavigationModel() {

        assertNotNull(model);
        assertEquals("/content/dam/footer-logo.png", model.getFooterLogo());
        assertEquals("_self", model.getFooterTarget());
        assertEquals("/content/footer-page.html", model.getFooterLink());

        List<FooterNavigationModel.ColumnModel> firstColumn = model.getFirstColumn();
        assertNotNull(firstColumn);
        assertEquals(1, firstColumn.size());
        FooterNavigationModel.ColumnModel firstColumnItem = firstColumn.get(0);
        assertEquals("Home", firstColumnItem.getNavTitle());
        assertEquals("/content/home.html", firstColumnItem.getNavLink());
        assertEquals("_self", firstColumnItem.getLinkTarget());

        List<FooterNavigationModel.ColumnModel> secondColumn = model.getSecondColumn();
        assertNotNull(secondColumn);
        assertEquals(1, secondColumn.size());
        FooterNavigationModel.ColumnModel secondColumnItem = secondColumn.get(0);
        assertEquals("About Us", secondColumnItem.getNavTitle());
        assertEquals("/content/about-us.html", secondColumnItem.getNavLink());
        assertEquals("_self", secondColumnItem.getLinkTarget());

        List<FooterNavigationModel.SocialIconsModel> socialIcons = model.getSocialIcons();
        assertNotNull(socialIcons);
        assertEquals(2, socialIcons.size());

        FooterNavigationModel.SocialIconsModel facebookIcon = socialIcons.get(0);
        assertEquals("facebook-icon", facebookIcon.getSocialIcon());
        assertEquals("_blank", facebookIcon.getSocialTarget());
        assertEquals("https://facebook.com", facebookIcon.getSocialLink());

        FooterNavigationModel.SocialIconsModel twitterIcon = socialIcons.get(1);
        assertEquals("twitter-icon", twitterIcon.getSocialIcon());
        assertEquals("_blank", twitterIcon.getSocialTarget());
        assertEquals("https://twitter.com", twitterIcon.getSocialLink());
    }
}
