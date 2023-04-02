package org.delfin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @Value("${greeting.message}")
    private String greetingMessage;

    @GetMapping("/hello")
    public String hello() {
        LOG.info("Replied to hello.");
        return greetingMessage;
    }
}
