package com.backend.Backend.controllers;

import com.backend.Backend.security.SecurityData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping("/")
    public String adminPage(Model model) {
        model.addAttribute("link", "http://"+SecurityData.ORIGIN+"/css/admin/layoutCss.css");

        model.addAttribute("css", "http://"+SecurityData.ORIGIN+"/css/admin/homeCss.css");
        model.addAttribute("js", "http://"+SecurityData.ORIGIN+"/js/admin/homeJs.js");
        model.addAttribute("page", "admin/home");
        return "admin/layout";
    }

    @GetMapping("/manage_tikets")
    public String manageTikets(Model model) {
        model.addAttribute("link", "http://"+SecurityData.ORIGIN+"/css/admin/layoutCss.css");
        model.addAttribute("page", "admin/manageTikets");
        return "admin/layout";
    }
}
