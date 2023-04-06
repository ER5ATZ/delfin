package org.delfin.controller;

import org.delfin.DelfinIntegrationTest;
import org.delfin.constant.Endpoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@DelfinIntegrationTest
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String URI = Endpoint.CUSTOMER;

    @Test
    public void getNoCustomerForInvalidId() throws Exception {
        mockMvc.perform(get(URI + Long.MAX_VALUE)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCustomerForId() throws Exception {
        mockMvc.perform(get(URI + 1)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }
}

