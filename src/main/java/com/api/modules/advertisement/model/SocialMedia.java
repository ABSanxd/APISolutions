package com.api.modules.advertisement.model;
import com.api.common.enums.SocialMediaType;
import lombok.Data;

@Data
public class SocialMedia {
    private SocialMediaType type;
    private String url;
}
