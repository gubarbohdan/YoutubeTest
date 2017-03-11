
package com.bohdan.gubar.youtubetest.model.video;

import java.util.HashMap;
import java.util.Map;

public class ContentDetails {

    private String duration;
    private String dimension;
    private String definition;
    private String caption;
    private Boolean licensedContent;
    private String projection;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getLicensedContent() {
        return licensedContent;
    }

    public void setLicensedContent(Boolean licensedContent) {
        this.licensedContent = licensedContent;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
