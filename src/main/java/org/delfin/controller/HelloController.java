package org.delfin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping("/")
@Tag(name = "LANDING", description = "Splash page for the application")
public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @Value("${greeting.message}")
    private String greetingMessage;

    @Value("${greeting.title}")
    private String greetingTitle;

    @GetMapping
    public String hello() {
        LOG.info("Someone's at the front door.");
        String banner = greetingTitle;
        try {
            banner = getBanner();
        } catch (IOException e) {
            LOG.warn("Banner content could not be loaded.");
        }
        return "<pre>" + banner + "</pre>" + greetingMessage;
    }

    private String getBanner() throws IOException {
        ClassPathResource resource = new ClassPathResource("banner.txt");
        byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
