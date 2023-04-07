package org.delfin.controller;

import org.delfin.DelfinIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@DelfinIntegrationTest
public class HelloControllerTest {

    private String expected = "DEL/FINANZ TEST\rHello, Test!";

    @Value("${greeting.message}")
    private String greetingMessage;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getHelloReturnsGreetingMessage() throws Exception {
        mockMvc.perform(get("/")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }
}
