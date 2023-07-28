package com.krab51.webapp.controllers;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/traps")
public class TrapWebController {
    private final Logger logger = getLogger(TrapWebController.class);

    @RequestMapping(method = GET)
    public ModelAndView getTrapsPage(ModelAndView modelAndView) {
        logger.info("Requesting traps web page");

        modelAndView.setViewName("traps");

        return modelAndView;
    }
}