package org.delfin.controller;

import org.delfin.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getNoTransactionForInvalidId() throws Exception {
        mockMvc.perform(get("/transaction/" + Long.MAX_VALUE)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTransactionForId() throws Exception {
        mockMvc.perform(get("/transaction/" + 1)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    public void createTransaction_shouldReturnCreatedTransaction() throws Exception {
    }

    @Test
    public void createTransaction_withNonExistingAccount_shouldReturnBadRequest() throws Exception {
    }

    @Test
    public void getTransaction_withExistingId_shouldReturnTransaction() throws Exception {
    }

    @Test
    public void getTransaction_withNonExistingId_shouldReturnNotFound() throws Exception {
    }

    @Test
    public void getTransactionsForAccount_withExistingAccountId_shouldReturnTransactions() throws Exception {
    }

}
