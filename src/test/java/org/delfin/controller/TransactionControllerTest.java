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
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String URI = Endpoint.TRANSACTION;

    @Test
    public void getNoTransactionForInvalidId() throws Exception {
        mockMvc.perform(get(URI + Long.MAX_VALUE)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTransactionForId() throws Exception {
        mockMvc.perform(get(URI + 1)
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
