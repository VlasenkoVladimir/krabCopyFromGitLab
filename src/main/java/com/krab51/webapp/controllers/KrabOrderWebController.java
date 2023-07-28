package com.krab51.webapp.controllers;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/")
public class KrabOrderWebController {
    private final Logger logger = getLogger(KrabOrderWebController.class);

    @RequestMapping(method = GET)
    public ModelAndView getKrabOrdersPage(ModelAndView modelAndView) {
        logger.info("Requesting kraborders web page");

        modelAndView.setViewName("kraborders");

        return modelAndView;
    }
}