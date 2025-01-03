package com.venia.core.models.commerce.config;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Instagram API Configuration")
public @interface InstagramConfig {
    String accessToken();
    String instagramAccountId();
}
