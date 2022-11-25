package com.statsmind.bootstrap.controller;

import com.statsmind.bootstrap.ui.UILoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UIController {
    private static UILoader uiLoader = new UILoader();

    @RequestMapping(value = {"/", "/portal/**"}, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String uiIndex() {
        return "index";
    }
}
