package com.albion.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {
    @GetMapping("/terms-of-service")
    public String getTermsOfService() {
        return "terms-of-service";
    }

    @GetMapping("/refund-policy")
    public String getRefundPage() {
        return "refund-policy";
    }

    @GetMapping("/privacy-policy")
    public String getPrivacyPage() {
        return "privacy-policy";
    }
}
