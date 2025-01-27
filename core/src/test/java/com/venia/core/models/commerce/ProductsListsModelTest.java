package com.venia.core.models.commerce;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductsListsModelTest {

    private final AemContext context = new AemContext();
    private ProductsListsModel productsListsModel;

    @BeforeEach
    void setUp() {
        // Create mock content
        context.create().resource("/content/productList",
                "title", "Product List Title",
                "label", "Shop Now",
                "link", "/content/site/products");

        context.create().resource("/content/productList/productDetails/item1",
                "productName", "Product 1",
                "productImage", "/content/dam/product1.jpg",
                "productPrice", "$10",
                "productAvailability", "In Stock",
                "productdetailpagelink", "/content/site/product1");

        context.create().resource("/content/productList/productDetails/item2",
                "productName", "Product 2",
                "productImage", "/content/dam/product2.jpg",
                "productPrice", "$20",
                "productAvailability", "Out of Stock",
                "productdetailpagelink", "/content/site/product2");

        // Adapt the model
        Resource productListResource = context.resourceResolver().getResource("/content/productList");
        context.currentResource(productListResource);
        productsListsModel = context.request().adaptTo(ProductsListsModel.class);
    }

    @Test
    void testTitle() {
        assertNotNull(productsListsModel);
        assertEquals("Product List Title", productsListsModel.getTitle());
    }

    @Test
    void testLabel() {
        assertEquals("Shop Now", productsListsModel.getLabel());
    }

    @Test
    void testLink() {
        assertEquals("/content/site/products.html", productsListsModel.getLink());
    }

    @Test
    void testProductDetails() {
        List<ProductsListsModel.ProductDetails> productDetails = productsListsModel.getProductDetails();
        assertNotNull(productDetails);
        assertEquals(2, productDetails.size());

        // Test the first product
        ProductsListsModel.ProductDetails firstProduct = productDetails.get(0);
        assertEquals("Product 1", firstProduct.getProductName());
        assertEquals("/content/dam/product1.jpg", firstProduct.getProductImage());
        assertEquals("$10", firstProduct.getProductPrice());
        assertEquals("In Stock", firstProduct.getProductAvailability());
        assertEquals("/content/site/product1.html", firstProduct.getProductdetailpagelink());

        // Test the second product
        ProductsListsModel.ProductDetails secondProduct = productDetails.get(1);
        assertEquals("Product 2", secondProduct.getProductName());
        assertEquals("/content/dam/product2.jpg", secondProduct.getProductImage());
        assertEquals("$20", secondProduct.getProductPrice());
        assertEquals("Out of Stock", secondProduct.getProductAvailability());
        assertEquals("/content/site/product2.html", secondProduct.getProductdetailpagelink());
    }
}
