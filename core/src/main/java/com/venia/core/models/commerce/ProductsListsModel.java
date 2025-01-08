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
public class ProductsListsModel {


    @ValueMapValue
    private String title;

    @ValueMapValue
    private String label;

    @ValueMapValue
    private String link;

    @ChildResource(name = "productDetails")
    private List<ProductDetails> productDetails;

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public String getTitle() {
        return title;
    }

    public String getLabel() {
        return label;
    }

    public String getLink() {
        if (link.contains("/content/")){
            return link+".html";
        }else {
            return link;
        }
    }

    @Model(
            adaptables = {Resource.class,SlingHttpServletRequest.class},
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class ProductDetails{

       @ValueMapValue
       private String productName;

        @ValueMapValue
        private String productImage;

        @ValueMapValue
        private String productLabel;

        public String getProductName() {
            return productName;
        }

        public String getProductImage() {
            return productImage;
        }

        public String getProductLabel() {
            return productLabel;
        }
    }
}
