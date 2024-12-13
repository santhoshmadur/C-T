/*******************************************************************************
 *
 *    Copyright 2023 Adobe. All rights reserved.
 *    This file is licensed to you under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License. You may obtain a copy
 *    of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software distributed under
 *    the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 *    OF ANY KIND, either express or implied. See the License for the specific language
 *    governing permissions and limitations under the License.
 *
 ******************************************************************************/
package com.venia.core.models.commerce;

import com.adobe.cq.commerce.core.components.services.urls.UrlProvider;
import com.adobe.cq.commerce.core.search.models.SearchResultsSet;
import com.adobe.cq.commerce.core.search.models.Sorter;
import com.adobe.cq.commerce.core.search.models.SorterKey;
import com.adobe.cq.commerce.core.search.services.SearchResultsService;
import com.adobe.cq.commerce.magento.graphql.FilterMatchTypeInput;
import com.adobe.cq.commerce.magento.graphql.ProductAttributeFilterInput;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(AemContextExtension.class)
public class MySearchResultsImplTest {
    private static final String PAGE = "/content/page";
    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private MySearchResultsImpl underTest;
    private HashMap<String, Object> dataLayerConfigMap;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
        Page page = context.create().page(PAGE);
        Map<String, Object> props = new HashMap<>();
        props.put("sling:resourceType", "venia/components/commerce/searchresults");
        props.put("sling:resourceSuperType", "core/cif/components/commerce/searchresults/v1/searchresults");
        context.create().resource(page, "test", props);
        context.currentResource(PAGE + "/jcr:content/test");

        SearchResultsService searchResultsService = mock(SearchResultsService.class);
        SearchResultsSet searchResultsSet = mock(SearchResultsSet.class);
        Function<ProductAttributeFilterInput, ProductAttributeFilterInput> a = any();
        when(searchResultsService.performSearch(any(), any(), any(), any(), any(), a)).thenReturn(searchResultsSet);
        Sorter sorter = mock(Sorter.class);
        when(searchResultsSet.getSorter()).thenReturn(sorter);
        SorterKey k1 = mock(SorterKey.class);
        when(k1.getName()).thenReturn("k1");
        SorterKey k2 = mock(SorterKey.class);
        when(k2.getName()).thenReturn("k2");
        SorterKey k3 = mock(SorterKey.class);
        when(k3.getName()).thenReturn("position");
        when(sorter.getKeys()).thenReturn(new ArrayList<>(Arrays.asList(k1, k2, k3)));

        context.registerService(SearchResultsService.class, searchResultsService);
        context.registerService(UrlProvider.class, mock(UrlProvider.class));
        context.addModelsForClasses(MySearchResultsImpl.class);

        ConfigurationBuilder configurationBuilder = mock(ConfigurationBuilder.class);
        when(configurationBuilder.name("com.adobe.cq.wcm.core.components.internal.DataLayerConfig")).thenReturn(configurationBuilder);
        dataLayerConfigMap = new HashMap<>();
        when(configurationBuilder.asValueMap()).thenReturn(new ValueMapDecorator(dataLayerConfigMap));
        context.registerAdapter(Resource.class, ConfigurationBuilder.class, configurationBuilder);

        MockSlingHttpServletRequest request = context.request();
        request.addRequestParameter("search_query", "test");

        underTest = request.adaptTo(MySearchResultsImpl.class);
        Assertions.assertNotNull(underTest);
    }

    @Test
    void testInstance() {
        assertEquals("paginationbar", underTest.getPaginationType());
        assertFalse(underTest.isAddToCartEnabled());
        assertFalse(underTest.isAddToWishListEnabled());
        assertTrue(underTest.loadClientPrice());
        assertNotNull(underTest.getSearchResultsStorefrontContext());
        assertNotNull(underTest.getSearchStorefrontContext());
        underTest.getAppliedCssClasses();
        assertEquals("venia/components/commerce/searchresults", underTest.getExportedType());
        ComponentData componentData = underTest.getData();
        assertNull(componentData);
        assertEquals("searchresults-50df7e8869", underTest.getId());

        underTest.extendProductQueryWith(p-> p.color());
        underTest.extendProductFilterWith(f -> f.setName(new FilterMatchTypeInput().setMatch("winter")));
        assertNotNull(underTest.getProducts());
    }

    @Test
    void testComponentData() {
        dataLayerConfigMap.put("enabled", true);

        ComponentData componentData = underTest.getData();
        assertNotNull(componentData);
        assertEquals("venia/components/commerce/searchresults", componentData.getType());
        assertEquals("searchresults-50df7e8869", componentData.getId());
    }


    @Test
    void testSorterKeys() {
        List<SorterKey> keys = underTest.getSearchResultsSet().getSorter().getKeys();
        assertFalse(keys.stream().anyMatch(sorterKey -> sorterKey.getName().equals("position")));
        assertEquals(2, keys.size());
    }
}
