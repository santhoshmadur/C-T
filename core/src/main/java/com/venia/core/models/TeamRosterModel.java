package com.venia.core.models;

import java.util.List;

import com.adobe.cq.export.json.ComponentExporter;

public interface TeamRosterModel extends ComponentExporter{

    default String getTeamName() {
        throw new UnsupportedOperationException();
    }
    default String getTeamLogoFileReference()
    {
        throw new UnsupportedOperationException();
    }
    default String getNumberOfCards()
    {
        throw new UnsupportedOperationException();
    }
    default List<TeamRosterModel> getTeamMembers()
    {
        throw new UnsupportedOperationException();
    }
}