package org.delfin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.delfin.DelfinIntegrationTest;
import org.delfin.constant.Endpoint;
import org.delfin.model.Transaction;
import org.delfin.model.CalculationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void getTransaction_returns404NotFoundForInvalidId() throws Exception {
        // Perform GET request to fetch transaction with non-existing ID
        mockMvc.perform(get(URI + Long.MAX_VALUE)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTransaction_returnsValidDtoForValidId() throws Exception {
        // Perform GET request to fetch transaction with ID of 1
        MvcResult mvcResult = mockMvc.perform(get(URI + 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Transaction
        Transaction result = getTransactionDTO(mvcResult);

        // Validate that transaction DTO matches expected values
        assertEquals(1L, result.getId());
        assertEquals(Link.of(Endpoint.ACCOUNT + 1).getHref(), getObjectLink(result.getAccount()));
        assertEquals(new BigDecimal("950.00"), result.getAmount());
        assertEquals(new BigDecimal("1000.00"), result.getPrevBalance());
        assertEquals(CalculationType.NEGATIVE, result.getTransactionType());
        assertEquals(new BigDecimal("50.00"), result.getNewBalance());
    }

    @Test
    public void createTransaction_shouldReturnCreatedTransaction() throws Exception {
        // Create the request body for the transaction
        Transaction request = new Transaction();
        request.setAccount(2L);
        request.setExternalId("TESTGOOD");
        request.setAmount(new BigDecimal("500.00"));
        request.setTransactionType(CalculationType.POSITIVE);

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create the transaction
        MvcResult mvcResult = mockMvc.perform(post(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Transaction
        Transaction result = getTransactionDTO(mvcResult);

        // Validate that transaction DTO matches expected values
        assertNotNull(result.getId());
        assertEquals(Endpoint.ACCOUNT + 2, getObjectLink(result.getAccount()));
        assertEquals(new BigDecimal("500.00"), result.getAmount());
        assertEquals(CalculationType.POSITIVE, result.getTransactionType());
        assertNotNull(result.getTransactiontime());
        assertEquals(new BigDecimal("500.00"), result.getPrevBalance());
        assertEquals(new BigDecimal("1000.00"), result.getNewBalance());
        assertTrue(result.isBooked());
    }

    @Test
    public void createTransaction_exceedingLimit_shouldBeDenied() throws Exception {
        // Create the request body for the transaction
        Transaction request = new Transaction();
        request.setAccount(3L);
        request.setExternalId("TESTBAD");
        request.setAmount(new BigDecimal("10000.00"));
        request.setTransactionType(CalculationType.NEGATIVE);

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create the transaction
       mockMvc.perform(post(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void createTransaction_withNonExistingAccount_shouldReturnBadRequest() throws Exception {
        // Create the request body for the transaction
        Transaction request = new Transaction();
        request.setAccount(Long.MAX_VALUE);
        request.setExternalId("HUMBUG");
        request.setAmount(new BigDecimal("500.00"));
        request.setTransactionType(CalculationType.POSITIVE);

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create the transaction
        mockMvc.perform(post(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private Transaction getTransactionDTO(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jackson2HalModule());
        return mapper.readValue(mvcResult.getResponse().getContentAsString(), Transaction.class);
    }

    private String getRequestBody(Transaction request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(request);
    }

    private String getObjectLink(Object link) {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) link;
        return linkedHashMap.get("href");
    }

}
