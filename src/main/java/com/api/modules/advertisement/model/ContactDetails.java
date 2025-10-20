package com.api.modules.advertisement.model;
import lombok.Data;

@Data
public class ContactDetails {
    private String email;
    private String phone;
    private String address;
    private SocialMedia socialMedia;// ig + link
}
