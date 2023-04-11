package com.wimix.automation.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "app_name",
        "app_version",
        "app_url",
        "app_id",
        "uploaded_at"
})

@Data
public class UploadedApp {

    @JsonProperty("app_name")
    private String appName;
    @JsonProperty("app_version")
    private String appVersion;
    @JsonProperty("app_url")
    private String appUrl;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("uploaded_at")
    private String uploadedAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}