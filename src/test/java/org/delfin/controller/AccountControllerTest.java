package org.delfin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.delfin.DelfinIntegrationTest;
import org.delfin.constant.Endpoint;
import org.delfin.model.Account;
import org.delfin.model.Currency;
import org.delfin.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@DelfinIntegrationTest
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String URI = Endpoint.ACCOUNT;

    private static final String TRANSACTIONS = "/transactions";

    @Test
    public void getAccount_returnsBadRequestForInvalidId() throws Exception {
        // Perform GET request to fetch Account with non-existing ID
        mockMvc.perform(get(URI + Long.MAX_VALUE)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAccount_returnsValidAccountForId() throws Exception {
        // Perform GET request to fetch Account with ID of 1
        MvcResult mvcResult = mockMvc.perform(get(URI + 1)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Account DTO
        Account result = getAccountDTO(mvcResult);

        // Validate that Account DTO matches expected values
        assertEquals(1L, result.getId());
        assertEquals(Link.of(Endpoint.CUSTOMER + 1).getHref(), getObjectLink(result.getCustomer()));
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        assertEquals(new BigDecimal("2000.00"), result.getAccountLimit());
        assertEquals(Currency.USD.code(), result.getCurrency());
    }

    @Test
    public void getTransationsOfAccount_returnsBadRequestForInvalidId() throws Exception {
        // Perform GET request to fetch Transactions for Account with non-existing ID
        mockMvc.perform(get(URI + Long.MAX_VALUE + TRANSACTIONS)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTransactionsOfAccount_returnsValidTransactionsForAccountId() throws Exception {
        // Perform GET request to fetch Transactions for Account with ID of 1
        MvcResult mvcResult = mockMvc.perform(get(URI + 1 + TRANSACTIONS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to a collection of Transaction DTO
        List<Transaction> results = getListOfTransactions(mvcResult);

        // Validate that the returned Transactions match the Account
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
        for (Transaction result : results) {
            assertEquals(Link.of(Endpoint.ACCOUNT + 1).getHref(), getObjectLink(result.getAccount()));
        }
    }

    @Test
    public void getTransactionsOfAccount_returnsEmptyResultForAccountWithoutTransactions() throws Exception {
        // Perform GET request to fetch Transactions for Account with ID of 4
        MvcResult mvcResult = mockMvc.perform(get(URI + 4 + TRANSACTIONS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to a collection of Transaction DTO
        List<Transaction> results = getListOfTransactions(mvcResult);

        // Validate that the returned Transactions match the account
        assertTrue(results.isEmpty());
    }

    @Test
    public void createAccount_returnsBadRequestForInvalidCustomerId() throws Exception {
        // Create the request body for the account
        Account request = new Account();
        request.setCustomer(Long.MAX_VALUE);
        request.setAccountLimit(BigDecimal.ONE);

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create Account for Customer with non-existing ID
        mockMvc.perform(post(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccount_returnsValidAccountForValidCustomerId() throws Exception {
        // Create the request body for the account
        Account request = new Account();
        request.setCustomer(3L);
        request.setCurrency(Currency.USD.code());
        request.setBalance(new BigDecimal(1000));
        request.setAccountLimit(new BigDecimal(0));

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create Account for Customer with ID of 3
        MvcResult mvcResult = mockMvc.perform(post(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Account
        Account result = getAccountDTO(mvcResult);

        // Validate that account DTO matches expected values
        assertEquals(Link.of(Endpoint.CUSTOMER + 3).getHref(), getObjectLink(result.getCustomer()));
        assertEquals(new BigDecimal(1000), result.getBalance());
        assertEquals(new BigDecimal(0), result.getAccountLimit());
        assertEquals(Currency.USD.code(), result.getCurrency());
    }

    @Test
    public void updateAccount_returnsBadRequestForInvalidAccountId() throws Exception {
        // Create the request body for the account
        Account request = new Account();
        request.setCustomer(Long.MAX_VALUE);

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform PUT request for Account with non-existing ID
        mockMvc.perform(put(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAccount_returnsValidUpdatedAccount() throws Exception {
        // Perform GET request to read Account for Customer with ID of 3
        MvcResult mvcRequest = mockMvc.perform(get(URI + 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Account
        Account request = getAccountDTO(mvcRequest);
        BigDecimal orgBalance = request.getBalance();
        BigDecimal orgLimit = request.getAccountLimit();
        String orgCurrency = request.getCurrency();

        // Update the request body for the Account
        request.setCustomer(2L);
        request.setCurrency(Currency.EUR.code());
        assertNotEquals(orgCurrency, request.getCurrency());
        request.setBalance(new BigDecimal(1000));
        assertNotEquals(orgBalance, request.getBalance());
        // We expect only this field to be updated, balance should only be
        // changed by transaction, while currency should never be changed by API
        request.setAccountLimit(new BigDecimal(0));
        assertNotEquals(orgLimit, request.getAccountLimit());

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform PUT request update Customer
        MvcResult mvcResult = mockMvc.perform(put(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Account
        Account result = getAccountDTO(mvcResult);

        // Validate that account DTO matches expected values
        assertEquals(orgBalance, result.getBalance());
        assertNotEquals(orgLimit, result.getAccountLimit());
        assertEquals(Currency.USD.code(), result.getCurrency());
    }

    @Test
    public void deleteAccount_returnsBadRequestForInvalidAccountId() throws Exception {
        // Perform DEL request to deativate Account with non-existing ID
        mockMvc.perform(delete(URI + Long.MAX_VALUE)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteAccount_returnsSuccess() throws Exception {
        // Create the request body for the account
        Account request = new Account();
        request.setCustomer(3L);
        request.setCurrency(Currency.EUR.code());
        request.setBalance(new BigDecimal(0));
        request.setAccountLimit(new BigDecimal(0));

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create Account for Customer with ID of 3
        MvcResult mvcResult = mockMvc.perform(post(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Account
        Account toDelete = getAccountDTO(mvcResult);

        // Perform DEL request to deativate Account with new ID
        mockMvc.perform(delete(URI + toDelete.getId())
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNoContent());

        // Perform GET request to fetch Account with inactive ID
        mockMvc.perform(get(URI + toDelete.getId())
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    private List<Transaction> getListOfTransactions(MvcResult mvcResult) throws Exception {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jackson2HalModule());
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode embedded = mapper.readTree(responseContent);
        if (embedded == null) {
            return Collections.emptyList();
        }
        Iterator<JsonNode> iterator = embedded.elements();
        List<Transaction> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(mapper.readValue(iterator.next().toString(), Transaction.class));
        }
        return result;
    }

    private Account getAccountDTO(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jackson2HalModule());
        return mapper.readValue(mvcResult.getResponse().getContentAsString(), Account.class);
    }

    private String getRequestBody(Account request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(request);
    }

    private String getObjectLink(Object link) {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) link;
        return linkedHashMap.get("href");
    }
}
