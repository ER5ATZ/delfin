package org.delfin.repository;

import org.delfin.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
@Transactional
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findByCustomerId(Long customerId) {
        String sql = "SELECT * FROM account WHERE customerid = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class), customerId);
    }

    @Override
    public List<Account> findAll() {
        // Implementation omitted
        return null;
    }

    @Override
    public List<Account> findAll(Sort sort) {
        // Implementation omitted
        return null;
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        // Implementation omitted
        return null;
    }

    @Override
    public List<Account> findAllById(Iterable<Long> longs) {
        // Implementation omitted
        return null;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM account";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public void deleteById(Long aLong) {
        // Implementation omitted
    }

    @Override
    public void delete(Account entity) {
        // Implementation omitted
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        // Implementation omitted
    }

    @Override
    public void deleteAll(Iterable<? extends Account> entities) {
        // Implementation omitted
    }

    @Override
    public void deleteAll() {
        // Implementation omitted
    }

    @Override
    public <S extends Account> S save(S entity) {
        String sql = "INSERT INTO account (customerid, balance) VALUES (?, ?)";
        jdbcTemplate.update(sql, entity.getCustomer(), entity.getBalance());
        return entity;
    }

    @Override
    public <S extends Account> List<S> saveAll(Iterable<S> entities) {
        // Implementation omitted
        return null;
    }

    @Override
    public Optional<Account> findById(Long aLong) {
        String sql = "SELECT * FROM account WHERE id = ?";
        List<Account> accounts = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class), aLong);
        if (accounts.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(accounts.get(0));
        }
    }

    @Override
    public boolean existsById(Long aLong) {
        return findById(aLong).isPresent();
    }

    @Override
    public void flush() {
        // No-op, since JdbcTemplate doesn't have a persistence context to flush
    }

    @Override
    public <S extends Account> S saveAndFlush(S entity) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Account> List<S> saveAllAndFlush(Iterable<S> entities) {
        // Implementation omitted
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Account> entities) {
        // Implementation omitted
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        // Implementation omitted
    }

    @Override
    public void deleteAllInBatch() {
        // Implementation omitted
    }

    /**
     * @param aLong
     * @deprecated
     */
    @Override
    public Account getOne(Long aLong) {
        // Implementation omitted
        return null;
    }

    /**
     * @param aLong
     * @deprecated
     */
    @Override
    public Account getById(Long aLong) {
        // Implementation omitted
        return null;
    }

    @Override
    public Account getReferenceById(Long aLong) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Account> Optional<S> findOne(Example<S> example) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Account> List<S> findAll(Example<S> example) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Account> List<S> findAll(Example<S> example, Sort sort) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Account> Page<S> findAll(Example<S> example, Pageable pageable) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Account> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Account> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Account, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // Implementation omitted
        return null;
    }
}
