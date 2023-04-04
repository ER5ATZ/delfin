package org.delfin.repository;

import org.delfin.model.TransactionType;
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
public class TransactionTypeRepositoryImpl implements TransactionTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TransactionTypeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TransactionType> findAll() {
        String sql = "SELECT * FROM transactiontype";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionType.class));
    }

    @Override
    public List<TransactionType> findAll(Sort sort) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Page<TransactionType> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public List<TransactionType> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM transactiontype";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void delete(TransactionType entity) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends TransactionType> entities) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM transactiontype";
        jdbcTemplate.update(sql);
    }

    @Override
    public <S extends TransactionType> S save(S entity) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public <S extends TransactionType> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Optional<TransactionType> findById(Long aLong) {
        String sql = "SELECT * FROM transactiontype WHERE id = ?";
        List<TransactionType> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionType.class), aLong);
        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }

    @Override
    public boolean existsById(Long aLong) {
        String sql = "SELECT COUNT(*) FROM transactiontype WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, aLong) > 0;
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public <S extends TransactionType> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public <S extends TransactionType> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void deleteAllInBatch(Iterable<TransactionType> entities) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    /**
     * @param aLong
     * @deprecated
     */
    @Override
    public TransactionType getOne(Long aLong) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    /**
     * @param aLong
     * @deprecated
     */
    @Override
    public TransactionType getById(Long aLong) {
        return null;
    }

    @Override
    public TransactionType getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends TransactionType> Optional<S> findOne(Example<S> example) {
        // Implementation omitted
        return Optional.empty();
    }

    @Override
    public <S extends TransactionType> List<S> findAll(Example<S> example) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends TransactionType> List<S> findAll(Example<S> example, Sort sort) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends TransactionType> Page<S> findAll(Example<S> example, Pageable pageable) {
        // Implementation omitted
        return null;
    }

    @Override
    public <S extends TransactionType> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TransactionType> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends TransactionType, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // Implementation omitted
        return null;
    }
}
