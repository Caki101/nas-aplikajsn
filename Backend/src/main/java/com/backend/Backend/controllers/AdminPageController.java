package com.backend.Backend.controllers;

import com.backend.Backend.security.SecurityData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    final String origin = "http://"+SecurityData.ORIGIN;

    @GetMapping("")
    public String adminLogin(Model model) {
        model.addAttribute("link", origin+"/css/admin/loginCss.css");
        model.addAttribute("src", origin+"/js/admin/loginJs.js");
        return "admin/login";
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        model.addAttribute("css", origin+"/css/admin/dashboardCss.css");
        model.addAttribute("js", origin+"/js/admin/dashboardJs.js");
        model.addAttribute("page", "admin/dashboard");
        return "admin/layout";
    }

    @GetMapping("/manage_data/korisnici")
    public String manageDataKorisnici(Model model) {
        model.addAttribute("css", origin+"/css/admin/manageData/korisniciCss.css");
        model.addAttribute("js", origin+"/js/admin/manageData/korisniciJs.js");
        model.addAttribute("page", "admin/manageData/korisnici");
        return "admin/layout";
    }

    @GetMapping("/manage_data/smestaji")
    public String manageDataSmestaji(Model model) {
        model.addAttribute("css", origin+"/css/admin/manageData/smestajiCss.css");
        model.addAttribute("js", origin+"/js/admin/manageData/smestajiJs.js");
        model.addAttribute("page", "admin/manageData/smestaji");
        return "admin/layout";
    }

    @GetMapping("/manage_data/tiketi")
    public String manageDataTiketi(Model model) {
        model.addAttribute("css", origin+"/css/admin/manageData/tiketiCss.css");
        model.addAttribute("js", origin+"/js/admin/manageData/tiketiJs.js");
        model.addAttribute("page", "admin/manageData/tiketi");
        return "admin/layout";
    }
}
