package org.delfin.repository;

import org.delfin.model.Account;
import org.delfin.model.Customer;
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
public class CustomerRepositoryImpl implements CustomerRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CustomerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public List<Customer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Customer> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        // Implementation omitted
    }

    @Override
    public void delete(Customer entity) {
        // Implementation omitted
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        // Implementation omitted
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> entities) {
        // Implementation omitted
    }

    @Override
    public void deleteAll() {
        // Implementation omitted
    }

    @Override
    public <S extends Customer> S save(S entity) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Customer> List<S> saveAll(Iterable<S> entities) {
        // Implementation omitted
        return null;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customer.class), id).stream().findAny();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {
        // Implementation omitted
    }

    @Override
    public <S extends Customer> S saveAndFlush(S entity) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Customer> List<S> saveAllAndFlush(Iterable<S> entities) {
        // Implementation omitted
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Customer> entities) {
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
    public Customer getOne(Long aLong) {
        // Implementation omitted
        return null;
    }

    /**
     * @param aLong
     * @deprecated
     */
    @Override
    public Customer getById(Long aLong) {
        // Implementation omitted
        return null;
    }

    @Override
    public Customer getReferenceById(Long aLong) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Customer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example, Sort sort) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Customer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Customer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Customer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // Implementation omitted
        return null;
    }
}
