package com.backend.Backend.controllers;

import com.backend.Backend.security.SecurityData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    final String ORIGIN = "http://"+SecurityData.ORIGIN;

    public Model addStatic(Model model) {
        model.addAttribute("static_css",ORIGIN + "/css/admin/layoutCss.css");
        model.addAttribute("static_js",ORIGIN + "/js/admin/layoutJs.js");
        return model;
    }
    
    @GetMapping("")
    public String adminLogin(Model model) {
        model.addAttribute("link", ORIGIN+"/css/admin/loginCss.css");
        model.addAttribute("src", ORIGIN+"/js/admin/loginJs.js");
        return "admin/login";
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        model = addStatic(model);
        model.addAttribute("css", ORIGIN+"/css/admin/dashboardCss.css");
        model.addAttribute("js", ORIGIN+"/js/admin/dashboardJs.js");
        model.addAttribute("page", "admin/dashboard");
        return "admin/layout";
    }

    @GetMapping("/manage_data/korisnici")
    public String manageDataKorisnici(Model model) {
        model = addStatic(model);
        model.addAttribute("css", ORIGIN+"/css/admin/manageData/korisniciCss.css");
        model.addAttribute("js", ORIGIN+"/js/admin/manageData/korisniciJs.js");
        model.addAttribute("page", "admin/manageData/korisnici");
        model.addAttribute("global_css", ORIGIN + "/css/admin/manageData/manageDataCss.css");
        model.addAttribute("global_methods", ORIGIN + "/js/admin/manageData/manageDataJs.js");
        return "admin/layout";
    }

    @GetMapping("/manage_data/smestaji")
    public String manageDataSmestaji(Model model) {
        model = addStatic(model);
        model.addAttribute("css", ORIGIN+"/css/admin/manageData/smestajiCss.css");
        model.addAttribute("js", ORIGIN+"/js/admin/manageData/smestajiJs.js");
        model.addAttribute("page", "admin/manageData/smestaji");
        model.addAttribute("global_css", ORIGIN + "/css/admin/manageData/manageDataCss.css");
        model.addAttribute("global_methods", ORIGIN + "/js/admin/manageData/manageDataJs.js");
        return "admin/layout";
    }

    @GetMapping("/manage_data/tiketi")
    public String manageDataTiketi(Model model) {
        model = addStatic(model);
        model.addAttribute("css", ORIGIN+"/css/admin/manageData/tiketiCss.css");
        model.addAttribute("js", ORIGIN+"/js/admin/manageData/tiketiJs.js");
        model.addAttribute("page", "admin/manageData/tiketi");
        model.addAttribute("global_css", ORIGIN + "/css/admin/manageData/manageDataCss.css");
        model.addAttribute("global_methods", ORIGIN + "/js/admin/manageData/manageDataJs.js");
        return "admin/layout";
    }
}
