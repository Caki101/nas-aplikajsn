package com.backend.Backend.controllers;

import com.backend.Backend.security.SecurityData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ft")
public class FrontendTemplateController {
    final String ORIGIN = "http://"+ SecurityData.ORIGIN;

    @Value("${ft.facebook.link}")
    String fb_link;
    @Value("${ft.instagram.link}")
    String ig_link;
    @Value("${ft.x.link}")
    String x_link;

    public void addStatic(Model model) {
        model.addAttribute("static_css",ORIGIN + "/css/ft/layoutCss.css");
        model.addAttribute("static_js",ORIGIN + "/js/ft/layoutJs.js");

        model.addAttribute("facebook",fb_link);
        model.addAttribute("instagram",ig_link);
        model.addAttribute("x",x_link);
    }

    @GetMapping("/")
    public String home(Model model) {
        addStatic(model);
        model.addAttribute("page","ft/home");

        model.addAttribute("css",ORIGIN + "/css/ft/homeCss.css");
        model.addAttribute("js",ORIGIN + "/js/ft/homeJs.js");

        return "ft/layout";
    }

    @GetMapping("/destinations")
    public String destinations(Model model) {
        addStatic(model);
        model.addAttribute("page","ft/destinations");

        model.addAttribute("css",ORIGIN + "/css/ft/destinationsCss.css");
        model.addAttribute("js",ORIGIN + "/js/ft/destinationsJs.js");

        return "ft/layout";
    }

    @GetMapping("/offers")
    public String offers(Model model) {
        addStatic(model);
        model.addAttribute("page","ft/offers");

        return "ft/layout";
    }
}
