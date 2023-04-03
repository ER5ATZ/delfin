package org.delfin.controller;

import org.delfin.DelfinIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@DelfinIntegrationTest
@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Value("${greeting.message}")
    private String greetingMessage;

    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() throws Exception {
        mvc.perform(get("/hello")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(greetingMessage));
    }
}
