package com.krab51.webapp.controllers;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/trapowners")
public class TrapOwnerWebController {
    private final Logger logger = getLogger(TrapOwnerWebController.class);

    @RequestMapping(method = GET)
    public ModelAndView getTrapOwnersPage(ModelAndView modelAndView) {
        logger.info("Requesting trapowners web page");

        modelAndView.setViewName("trapowners");

        return modelAndView;
    }
}