package com.krab51.webapp.controllers;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/clients")
public class ClientWebController {
    private final Logger logger = getLogger(ClientWebController.class);

    @RequestMapping(method = GET)
    public ModelAndView getClientsPage(ModelAndView modelAndView) {
        logger.info("Requesting clients web page");

        modelAndView.setViewName("clients");

        return modelAndView;
    }
}