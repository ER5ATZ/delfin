package org.delfin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCustomer_returnsCreated() throws Exception {
        String request = objectMapper.writeValueAsString(
                new CreateCustomerRequestDto("John", "Doe")
        );

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void createAccount_returnsCreated() throws Exception {
        // First create a customer
        String customerRequest = objectMapper.writeValueAsString(
                new CreateCustomerRequestDto("Jane", "Smith")
        );
        String customerResponse = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CustomerResponseDto customer = objectMapper.readValue(customerResponse, CustomerResponseDto.class);
        UUID customerId = customer.id();

        // Now create an account
        String accountRequest = objectMapper.writeValueAsString(
                new CreateAccountRequestDto(customerId, "EUR", new BigDecimal("500.00"))
        );

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountRequest))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deposit_createsLedgerEntry() throws Exception {
        // Create customer and account
        UUID customerId = createCustomer("Alice", "Johnson");
        UUID accountId = createAccount(customerId, "EUR", new BigDecimal("1000.00"));

        // Deposit
        String entryRequest = objectMapper.writeValueAsString(
                new CreateEntryRequestDto("CREDIT", new BigDecimal("250.00"), "EUR", "Initial deposit")
        );

        String response = mockMvc.perform(post("/api/accounts/{id}/entries", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryRequest)
                        .header("Idempotency-Key", UUID.randomUUID().toString()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LedgerEntryResponseDto entry = objectMapper.readValue(response, LedgerEntryResponseDto.class);
        assertThat(entry.type()).isEqualTo("CREDIT");
        assertThat(entry.amount()).isEqualByComparingTo(new BigDecimal("250.00"));
        assertThat(entry.currency()).isEqualTo("EUR");
    }

    @Test
    void getAccount_showsDerivedBalance() throws Exception {
        // Create customer and account
        UUID customerId = createCustomer("Bob", "Williams");
        UUID accountId = createAccount(customerId, "EUR", new BigDecimal("500.00"));

        // Make a deposit
        String entryRequest = objectMapper.writeValueAsString(
                new CreateEntryRequestDto("CREDIT", new BigDecimal("300.00"), "EUR", "Deposit")
        );
        mockMvc.perform(post("/api/accounts/{id}/entries", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryRequest)
                        .header("Idempotency-Key", UUID.randomUUID().toString()))
                .andExpect(status().isCreated());

        // Get account and verify balance
        String response = mockMvc.perform(get("/api/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponseDto account = objectMapper.readValue(response, AccountResponseDto.class);
        assertThat(account.balance()).isEqualByComparingTo(new BigDecimal("300.00"));
    }

    @Test
    void transfer_debitsSourceCreditsDestination() throws Exception {
        // Create customers and accounts
        UUID customer1Id = createCustomer("Charles", "Brown");
        UUID customer2Id = createCustomer("Diana", "Green");
        UUID sourceAccountId = createAccount(customer1Id, "EUR", new BigDecimal("500.00"));
        UUID destAccountId = createAccount(customer2Id, "EUR", new BigDecimal("500.00"));

        // Deposit initial funds in source account
        depositFunds(sourceAccountId, new BigDecimal("1000.00"), "EUR", "Starting balance");

        // Transfer funds
        String transferRequest = objectMapper.writeValueAsString(
                new TransferRequestDto(sourceAccountId, destAccountId, new BigDecimal("200.00"), "EUR", "Transfer test")
        );
        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferRequest)
                        .header("Idempotency-Key", UUID.randomUUID().toString()))
                .andExpect(status().isCreated());

        // Verify source account balance
        String sourceResponse = mockMvc.perform(get("/api/accounts/{id}", sourceAccountId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AccountResponseDto sourceAccount = objectMapper.readValue(sourceResponse, AccountResponseDto.class);
        assertThat(sourceAccount.balance()).isEqualByComparingTo(new BigDecimal("800.00"));

        // Verify destination account balance
        String destResponse = mockMvc.perform(get("/api/accounts/{id}", destAccountId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AccountResponseDto destAccount = objectMapper.readValue(destResponse, AccountResponseDto.class);
        assertThat(destAccount.balance()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    void deposit_idempotent_returnsSameEntry() throws Exception {
        // Create customer and account
        UUID customerId = createCustomer("Eve", "Davis");
        UUID accountId = createAccount(customerId, "EUR", new BigDecimal("1000.00"));

        String entryRequest = objectMapper.writeValueAsString(
                new CreateEntryRequestDto("CREDIT", new BigDecimal("100.00"), "EUR", "Test deposit")
        );
        String idempotencyKey = UUID.randomUUID().toString();

        // First deposit
        String firstResponse = mockMvc.perform(post("/api/accounts/{id}/entries", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryRequest)
                        .header("Idempotency-Key", idempotencyKey))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        LedgerEntryResponseDto firstEntry = objectMapper.readValue(firstResponse, LedgerEntryResponseDto.class);

        // Second deposit with same idempotency key - should return 201 (idempotency means same response)
        String secondResponse = mockMvc.perform(post("/api/accounts/{id}/entries", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryRequest)
                        .header("Idempotency-Key", idempotencyKey))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        // Accept either 200 or 201 for idempotent replay
        int status = mockMvc.perform(post("/api/accounts/{id}/entries", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryRequest)
                        .header("Idempotency-Key", idempotencyKey))
                .andReturn()
                .getResponse()
                .getStatus();
        assertThat(status).isIn(200, 201);
        
        LedgerEntryResponseDto secondEntry = objectMapper.readValue(secondResponse, LedgerEntryResponseDto.class);

        // Both should have the same ID
        assertThat(secondEntry.id()).isEqualTo(firstEntry.id());
    }

    @Test
    void withdrawal_exceedsLimit_returns422() throws Exception {
        // Create customer and account with no overdraft
        UUID customerId = createCustomer("Frank", "Miller");
        UUID accountId = createAccount(customerId, "EUR", BigDecimal.ZERO);

        // Try to withdraw more than balance
        String entryRequest = objectMapper.writeValueAsString(
                new CreateEntryRequestDto("DEBIT", new BigDecimal("100.00"), "EUR", "Overdraft attempt")
        );

        mockMvc.perform(post("/api/accounts/{id}/entries", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryRequest)
                        .header("Idempotency-Key", UUID.randomUUID().toString()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAccountEntries_pagination_returnsPaginatedResults() throws Exception {
        // Create customer and account
        UUID customerId = createCustomer("Grace", "Taylor");
        UUID accountId = createAccount(customerId, "EUR", new BigDecimal("5000.00"));

        // Create multiple entries
        for (int i = 0; i < 5; i++) {
            depositFunds(accountId, new BigDecimal("100.00"), "EUR", "Entry " + i);
        }

        // Get entries with default pagination
        String response = mockMvc.perform(get("/api/accounts/{id}/entries", accountId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify response contains entries
        assertThat(response).contains("\"content\"");
    }

    // Helper methods

    private UUID createCustomer(String firstName, String lastName) throws Exception {
        String request = objectMapper.writeValueAsString(
                new CreateCustomerRequestDto(firstName, lastName)
        );
        String response = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CustomerResponseDto customer = objectMapper.readValue(response, CustomerResponseDto.class);
        return customer.id();
    }

    private UUID createAccount(UUID customerId, String currency, BigDecimal overdraftLimit) throws Exception {
        String request = objectMapper.writeValueAsString(
                new CreateAccountRequestDto(customerId, currency, overdraftLimit)
        );
        String response = mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponseDto account = objectMapper.readValue(response, AccountResponseDto.class);
        return account.id();
    }

    private void depositFunds(UUID accountId, BigDecimal amount, String currency, String description) throws Exception {
        String request = objectMapper.writeValueAsString(
                new CreateEntryRequestDto("CREDIT", amount, currency, description)
        );
        mockMvc.perform(post("/api/accounts/{id}/entries", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("Idempotency-Key", UUID.randomUUID().toString()))
                .andExpect(status().isCreated());
    }

    // Test DTOs
    record CreateCustomerRequestDto(String firstName, String lastName) {}

    record CustomerResponseDto(UUID id, String firstName, String lastName, java.util.List<?> accounts) {}

    record CreateAccountRequestDto(UUID customerId, String currency, BigDecimal overdraftLimit) {}

    record AccountResponseDto(UUID id, String iban, String currency, BigDecimal balance, BigDecimal overdraftLimit, boolean active) {}

    record CreateEntryRequestDto(String type, BigDecimal amount, String currency, String description) {}

    record LedgerEntryResponseDto(UUID id, String type, BigDecimal amount, String currency, String description, String idempotencyKey, java.time.Instant createdAt) {}

    record TransferRequestDto(UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount, String currency, String description) {}
}
