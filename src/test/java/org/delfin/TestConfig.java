package org.delfin;

import org.delfin.repository.*;
import org.delfin.service.AccountService;
import org.delfin.service.CustomerService;
import org.delfin.service.TransactionService;
import org.delfin.service.TransactionTypeService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@TestConfiguration
public class TestConfig {

    @Bean
    public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
        return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("org.delfin.entities");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public AccountRepository accountRepository(JdbcTemplate jdbcTemplate) {
        return new AccountRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public CustomerRepository customerRepository(JdbcTemplate jdbcTemplate) {
        return new CustomerRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public TransactionRepository transactionRepository(JdbcTemplate jdbcTemplate) {
        return new TransactionRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public TransactionTypeRepository transactionTypeRepository(JdbcTemplate jdbcTemplate) {
        return new TransactionTypeRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public AccountService accountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        return new AccountService(accountRepository, transactionRepository);
    }

    @Bean
    public CustomerService customerService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        return new CustomerService(customerRepository, accountRepository);
    }

    @Bean
    public TransactionService transactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        return new TransactionService(transactionRepository, accountRepository);
    }

    @Bean
    public TransactionTypeService transactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        return new TransactionTypeService(transactionTypeRepository);
    }
}
