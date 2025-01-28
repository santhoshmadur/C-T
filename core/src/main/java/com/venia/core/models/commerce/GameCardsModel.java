package com.venia.core.models.commerce;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class GameCardsModel {

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String scheduleType;

    @ValueMapValue
    private String teamName;

    @ValueMapValue
    private String date;

    @ValueMapValue
    private String time;

    @ValueMapValue
    private String ctaLabel;

    public String getImage() {
        return image;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

}
