package com.venia.core.models.commerce;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class FooterNavigationModelTest {

    private final AemContext context = new AemContext();
    private FooterNavigationModel footerNavigationModel;

    @BeforeEach
    void setUp() {
        // Create mock content
        context.create().resource("/content/footer",
                "footerLogo", "/content/dam/logo.png",
                "footerTarget", "_blank",
                "footerLink", "/content/about");

        context.create().resource("/content/footer/firstColumn/1",
                "navTitle", "Column 1",
                "navLink", "/content/page1",
                "navTarget", "_self");

        context.create().resource("/content/footer/secondColumn/1",
                "navTitle", "Column 2",
                "navLink", "/content/page2",
                "navTarget", "_blank");

        context.create().resource("/content/footer/socialIcons/1",
                "socialIcon", "facebook",
                "socialLink", "https://facebook.com",
                "socialTarget", "_blank");

        // Adapt the model
        context.currentResource(context.resourceResolver().getResource("/content/footer"));
        footerNavigationModel = context.request().adaptTo(FooterNavigationModel.class);
    }

    @Test
    void testFooterLogo() {
        // Assert footer logo value
        assertEquals("/content/dam/logo.png", footerNavigationModel.getFooterLogo());
    }

    @Test
    void testFooterLinkWithContent() {
        // Assert footer link value includes .html extension
        assertEquals("/content/about.html", footerNavigationModel.getFooterLink());
    }


    @Test
    void testFirstColumn() {
        // Assert first column values
        List<FooterNavigationModel.ColumnModel> firstColumn = footerNavigationModel.getFirstColumn();
        assertNotNull(firstColumn);
        assertEquals(1, firstColumn.size());
        FooterNavigationModel.ColumnModel column = firstColumn.get(0);
        assertEquals("Column 1", column.getNavTitle());
        assertEquals("/content/page1.html", column.getNavLink());
        assertEquals("_self", column.getLinkTarget());
    }

    @Test
    void testSecondColumn() {
        // Assert second column values
        List<FooterNavigationModel.ColumnModel> secondColumn = footerNavigationModel.getSecondColumn();
        assertNotNull(secondColumn);
        assertEquals(1, secondColumn.size());
        FooterNavigationModel.ColumnModel column = secondColumn.get(0);
        assertEquals("Column 2", column.getNavTitle());
        assertEquals("/content/page2.html", column.getNavLink());
        assertEquals("_blank", column.getLinkTarget());
    }

    @Test
    void testSocialIcons() {
        // Assert social icon values
        List<FooterNavigationModel.SocialIconsModel> socialIcons = footerNavigationModel.getSocialIcons();
        assertNotNull(socialIcons);
        assertEquals(1, socialIcons.size());
        FooterNavigationModel.SocialIconsModel socialIcon = socialIcons.get(0);
        assertEquals("facebook", socialIcon.getSocialIcon());
        assertEquals("https://facebook.com", socialIcon.getSocialLink());
        assertEquals("_blank", socialIcon.getSocialTarget());
    }

    @Test
    void testFooterTarget() {
        // Assert footer target value
        assertEquals("_blank", footerNavigationModel.getFooterTarget());
    }
}
