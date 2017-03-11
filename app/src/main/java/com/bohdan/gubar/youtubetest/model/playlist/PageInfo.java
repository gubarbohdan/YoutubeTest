
package com.bohdan.gubar.youtubetest.model.playlist;

import java.util.HashMap;
import java.util.Map;

public class PageInfo {

    private Long totalResults;
    private Long resultsPerPage;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public Long getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Long resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
