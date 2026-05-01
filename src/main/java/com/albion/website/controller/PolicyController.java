package com.albion.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {
    @GetMapping("/polices/contact-information/")
    public String getContactInformation() {
        return "contact-information";
    }
    @GetMapping("/polices/terms-of-service/")
    public String getTermsOfService() {
        return "terms-of-service";
    }
    @GetMapping("/polices/refund-policy/")
    public String getRefundPage() {
        return "refund-policy";
    }
    @GetMapping("/polices/privacy-policy/")
    public String getPrivacyPage() {
        return "privacy-policy";
    }
}
