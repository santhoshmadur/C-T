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
public class CardContainerModel {

    @ValueMapValue
    private String hoodieImage;

    @ValueMapValue
    private String hoodieBgImage;

    @ValueMapValue
    private String hoodieTitle;

    @ValueMapValue
    private String hoodiePrice;

    @ValueMapValue
    private String hoodieCustomizable;

    @ValueMapValue
    private String hoodieCtaLabel;

    @ValueMapValue
    private String hoodieCtaLink;

    @ValueMapValue
    private String shoesBgImage;

    @ValueMapValue
    private String shoesProductImage;

    @ValueMapValue
    private String shoesTitle;

    @ValueMapValue
    private String shoesSubTitle;

    @ValueMapValue
    private String shoesPrice;

    @ValueMapValue
    private String shoesCtaLabel;

    @ValueMapValue
    private String shoesImage;

    @ValueMapValue
    private String basketballimage;

    @ValueMapValue
    private String basketballBgImage;

    @ValueMapValue
    private String basketballTitle;

    @ValueMapValue
    private String basketballAvailability;

    @ValueMapValue
    private String basketballPrice;

    @ValueMapValue
    private String maskBgImage;

    @ValueMapValue
    private String maskImage;

    @ValueMapValue
    private String maskTitle;

    @ValueMapValue
    private String maskSubtitle;

    @ValueMapValue
    private String maskPrice;

    @ValueMapValue
    private String maskProductImage;

    @ValueMapValue
    private String mobileBgImage;

    @ValueMapValue
    private String mobileImage;

    @ValueMapValue
    private String mobileTitle;

    @ValueMapValue
    private String mobileCustomizable;

    @ValueMapValue
    private String mobilePrice;

    @ValueMapValue
    private String hatImage;

    @ValueMapValue
    private String hatBgImage;

    @ValueMapValue
    private String hatTitle;

    @ValueMapValue
    private String hatPrice;

    @ValueMapValue
    private String hatAvailability;

    @ValueMapValue
    private String hatCtaLabel;

    @ValueMapValue
    private String hatCtaLink;

    @ValueMapValue
    private String jerseyImage;

    @ValueMapValue
    private String jerseyBgImage;

    @ValueMapValue
    private String jerseyTitle;

    @ValueMapValue
    private String jerseyPrice;

    @ValueMapValue
    private String jerseyAvailability;

    @ValueMapValue
    private String jerseyCtaLabel;

    @ValueMapValue
    private String jerseyCtaLink;


    @ChildResource(name = "hoodieProducts")
    private List<ProductsModel> hoodieProducts;

    @ChildResource(name = "hatProducts")
    private List<ProductsModel> hatProducts;

    @ChildResource(name = "jerseyProducts")
    private List<ProductsModel> jerseyProducts;

    public String getHoodieImage() {
        return hoodieImage;
    }

    public String getHoodieBgImage() {
        return hoodieBgImage;
    }

    public String getHoodieTitle() {
        return hoodieTitle;
    }

    public String getHoodiePrice() {
        return hoodiePrice;
    }

    public String getHoodieCustomizable() {
        return hoodieCustomizable;
    }

    public String getHoodieCtaLabel() {
        return hoodieCtaLabel;
    }

    public String getHoodieCtaLink() {
        if (hoodieCtaLink.contains("/content/")){
            return hoodieCtaLink+".html";
        }else {
            return hoodieCtaLink;
        }
    }

    public String getShoesBgImage() {
        return shoesBgImage;
    }

    public String getShoesProductImage() {
        return shoesProductImage;
    }

    public String getShoesTitle() {
        return shoesTitle;
    }

    public String getShoesSubTitle() {
        return shoesSubTitle;
    }

    public String getShoesPrice() {
        return shoesPrice;
    }

    public String getShoesCtaLabel() {
        return shoesCtaLabel;
    }

    public String getShoesImage() {
        return shoesImage;
    }

    public String getBasketballimage() {
        return basketballimage;
    }

    public String getBasketballBgImage() {
        return basketballBgImage;
    }

    public String getBasketballTitle() {
        return basketballTitle;
    }

    public String getBasketballAvailability() {
        return basketballAvailability;
    }

    public String getBasketballPrice() {
        return basketballPrice;
    }

    public String getMaskBgImage() {
        return maskBgImage;
    }

    public String getMaskImage() {
        return maskImage;
    }

    public String getMaskTitle() {
        return maskTitle;
    }

    public String getMaskSubtitle() {
        return maskSubtitle;
    }

    public String getMaskPrice() {
        return maskPrice;
    }

    public String getMaskProductImage() {
        return maskProductImage;
    }

    public String getMobileBgImage() {
        return mobileBgImage;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public String getMobileTitle() {
        return mobileTitle;
    }

    public String getMobileCustomizable() {
        return mobileCustomizable;
    }

    public String getMobilePrice() {
        return mobilePrice;
    }

    public String getHatImage() {
        return hatImage;
    }

    public String getHatBgImage() {
        return hatBgImage;
    }

    public String getHatTitle() {
        return hatTitle;
    }

    public String getHatPrice() {
        return hatPrice;
    }

    public String getHatAvailability() {
        return hatAvailability;
    }

    public String getHatCtaLabel() {
        return hatCtaLabel;
    }

    public String getHatCtaLink() {
        if (hatCtaLink.contains("/content/")){
            return hatCtaLink+".html";
        }else {
            return hatCtaLink;
        }
    }

    public String getJerseyImage() {
        return jerseyImage;
    }

    public String getJerseyBgImage() {
        return jerseyBgImage;
    }

    public String getJerseyTitle() {
        return jerseyTitle;
    }

    public String getJerseyPrice() {
        return jerseyPrice;
    }

    public String getJerseyAvailability() {
        return jerseyAvailability;
    }

    public String getJerseyCtaLabel() {
        return jerseyCtaLabel;
    }

    public String getJerseyCtaLink() {
        if (jerseyCtaLink.contains("/content/")){
            return jerseyCtaLink+".html";
        }else {
            return jerseyCtaLink;
        }
    }

    public List<ProductsModel> getHoodieProducts() {
        return hoodieProducts;
    }

    public List<ProductsModel> getHatProducts() {
        return hatProducts;
    }

    public List<ProductsModel> getJerseyProducts() {
        return jerseyProducts;
    }

    @Model(
            adaptables = SlingHttpServletRequest.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class ProductsModel {

        @ValueMapValue
        private String productImage;

        public String getProductImage() {
            return productImage;
        }
    }

}
