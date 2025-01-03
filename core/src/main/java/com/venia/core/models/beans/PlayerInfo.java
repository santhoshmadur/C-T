package com.venia.core.models.beans;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlayerInfo {

    @Inject
    private String fileReference;

    @Inject
    private String firstName;

    @Inject
    private String lastName;

    @Inject
    private String playerRole;

    @Inject
    private String playerJerseyNumber;

    @Inject
    private String numberOfCards;

    @Inject
    private String playerImageFileReference;

    public String getPlayerJerseyNumber() {
        return playerJerseyNumber;
    }

    public String getNumberOfCards() {
        return numberOfCards;
    }

    public String getPlayerImageFileReference() {
        return playerImageFileReference;
    }

    public String getFileReference() {
        return fileReference;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPlayerRole() {
        return playerRole;
    }
}
