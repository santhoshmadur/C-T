package com.venia.core.models.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.venia.core.models.TeamRosterModel;
import com.venia.core.models.beans.PlayerInfo;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class}, adapters = TeamRosterModelImpl.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "venia/components/content/teamroster")
public class TeamRosterModelImpl implements TeamRosterModel {

    @Inject
    @Named("sling:resourceType")
    private String slingResourceType;
    
    @ValueMapValue
    private String teamName;

    @ValueMapValue
    private String teamLogoFileReference;

    @ValueMapValue
    private String numberofcards;

    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL, name = "teamRoster")
    private List<PlayerInfo>playerInfo;
    
    public List<PlayerInfo>getPlayerInfo(){
        return Collections.unmodifiableList(playerInfo);
    }

    @Override
    public String getExportedType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExportedType'");
    }
}
