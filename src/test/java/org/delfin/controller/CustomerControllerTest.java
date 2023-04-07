package org.delfin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.delfin.DelfinIntegrationTest;
import org.delfin.constant.Endpoint;
import org.delfin.model.Account;
import org.delfin.model.Currency;
import org.delfin.model.Customer;
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
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String URI = Endpoint.CUSTOMER;

    private static final String ACCOUNTS = "/accounts";

    @Test
    public void getCustomer_returnsBadRequestForInvalidId() throws Exception {
        // Perform GET request to fetch Customer with non-existing ID
        mockMvc.perform(get(URI + Long.MAX_VALUE)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCustomer_returnsValidCustomerForId() throws Exception {
        // Perform GET request to fetch Customer with ID of 1
        MvcResult mvcResult = mockMvc.perform(get(URI + 1)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Customer DTO
        Customer result = getCustomerDTO(mvcResult);

        // Validate that Account DTO matches expected values
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    public void getAccountsOfCustomer_returnsBadRequestForInvalidId() throws Exception {
        // Perform GET request to fetch Accounts for Customer with non-existing ID
        mockMvc.perform(get(URI + Long.MAX_VALUE + ACCOUNTS)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAccountsOfCustomer_returnsValidAccountsForCustomerId() throws Exception {
        // Perform GET request to fetch Transactions for Account with ID of 1
        MvcResult mvcResult = mockMvc.perform(get(URI + 1 + ACCOUNTS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to a collection of Transaction DTO
        List<Account> results = getListOfAccounts(mvcResult);

        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
        for (Account result : results) {
            assertEquals(Link.of(Endpoint.CUSTOMER + 1).getHref(), getObjectLink(result.getCustomer()));
        }
    }

    @Test
    public void getAccountsOfCustomer_returnsEmptyResultForCustomerWithoutAccounts() throws Exception {
        // Perform GET request to fetch Accounts for Customer with ID of 4
        MvcResult mvcResult = mockMvc.perform(get(URI + 4 + ACCOUNTS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to a collection of Account DTO
        List<Account> results = getListOfAccounts(mvcResult);

        // Validate that the returned Accounts match the Customer
        assertTrue(results.isEmpty());
    }

    @Test
    public void createCustomer_returnsValidCustomerForValidData() throws Exception {
        // Create the request body for the Customer
        Customer request = new Customer();
        request.setFirstName("Harald");
        request.setLastName("Schmidt");
        request.setActive(true);

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create new Customer
        MvcResult mvcResult = mockMvc.perform(post(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Customer
        Customer result = getCustomerDTO(mvcResult);

        // Validate that Customer DTO matches expected values
        assertEquals(request.getFirstName(), result.getFirstName());
        assertEquals(request.getLastName(), result.getLastName());
        assertEquals(request.isActive(), result.isActive());
    }

    @Test
    public void updateCustomer_returnsBadRequestForInvalidCustomerId() throws Exception {
        // Create the request body for the account
        Customer request = new Customer();
        request.setId(Long.MAX_VALUE);

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform PUT request for Customer with non-existing ID
        mockMvc.perform(put(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCustomer_returnsValidUpdatedCustomer() throws Exception {
        // Perform GET request to read Customer with ID of 4
        MvcResult mvcRequest = mockMvc.perform(get(URI + 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Customer
        Customer request = getCustomerDTO(mvcRequest);
        String orgFirstName = request.getFirstName();
        String orgLastName = request.getLastName();
        boolean orgState = request.isActive();

        // Update the request body for the Customer
        request.setFirstName("Patty");
        assertNotEquals(orgFirstName, request.getFirstName());
        request.setLastName("Smithee");
        assertNotEquals(orgLastName, request.getLastName());
        request.setActive(true);
        assertNotEquals(orgState, request.isActive());

        // Convert the request body to JSON
        String requestBody = getRequestBody(request);

        // Perform POST request to create Account for Customer with ID of 3
        MvcResult mvcResult = mockMvc.perform(put(URI)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Convert response body to Account
        Customer result = getCustomerDTO(mvcResult);

        // Validate that Customer DTO matches expected values
        assertNotEquals(orgFirstName, result.getFirstName());
        assertNotEquals(orgLastName, result.getLastName());
        assertNotEquals(orgState, result.isActive());
    }

    private List<Account> getListOfAccounts(MvcResult mvcResult) throws Exception {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jackson2HalModule());
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode embedded = mapper.readTree(responseContent);
        if (embedded == null) {
            return Collections.emptyList();
        }
        Iterator<JsonNode> iterator = embedded.elements();
        List<Account> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(mapper.readValue(iterator.next().toString(), Account.class));
        }
        return result;
    }

    private Customer getCustomerDTO(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jackson2HalModule());
        return mapper.readValue(mvcResult.getResponse().getContentAsString(), Customer.class);
    }

    private String getRequestBody(Customer request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(request);
    }

    private String getObjectLink(Object link) {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) link;
        return linkedHashMap.get("href");
    }
}

