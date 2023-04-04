package org.delfin.repository;

import org.delfin.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TransactionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        String sql = "SELECT * FROM transactions WHERE account_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class), accountId);
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
    }

    @Override
    public List<Transaction> findAll(Sort sort) {
        String sql = "SELECT * FROM transactions ORDER BY " + sort.toString().replace(": ", " ");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        String countSql = "SELECT COUNT(*) FROM transactions";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class);
        String sql = "SELECT * FROM transactions ORDER BY " + pageable.getSort().toString().replace(": ", " ")
                + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();
        List<Transaction> transactions = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
        return new PageImpl<>(transactions, pageable, total);
    }

    @Override
    public List<Transaction> findAllById(Iterable<Long> longs) {
        String sql = "SELECT * FROM transactions WHERE id IN (:ids)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class),
                Collections.singletonMap("ids", longs));
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM transactions";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        jdbcTemplate.update(sql, aLong);
    }

    @Override
    public void delete(Transaction entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        // Implementation omitted
    }

    @Override
    public void deleteAll(Iterable<? extends Transaction> entities) {
        // Implementation omitted
    }

    @Override
    public void deleteAll() {
        // Implementation omitted
    }

    @Override
    public <S extends Transaction> S save(S entity) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Transaction> List<S> saveAll(Iterable<S> entities) {
        // Implementation omitted
        return null;
    }

    @Override
    public Optional<Transaction> findById(Long aLong) {
        return Optional.empty();
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
    public <S extends Transaction> S saveAndFlush(S entity) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Transaction> List<S> saveAllAndFlush(Iterable<S> entities) {
        // Implementation omitted
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Transaction> entities) {
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
    public Transaction getOne(Long aLong) {
        // Implementation omitted
        return null;
    }

    /**
     * @param aLong
     * @deprecated
     */
    @Override
    public Transaction getById(Long aLong) {
        // Implementation omitted
        return null;
    }

    @Override
    public Transaction getReferenceById(Long aLong) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Transaction> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Transaction> List<S> findAll(Example<S> example) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Transaction> List<S> findAll(Example<S> example, Sort sort) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Transaction> Page<S> findAll(Example<S> example, Pageable pageable) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends Transaction> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Transaction> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Transaction, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // Implementation omitted
        return null;
    }
}
