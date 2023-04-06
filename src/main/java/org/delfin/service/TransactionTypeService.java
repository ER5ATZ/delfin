package org.delfin.service;

import org.delfin.model.entity.TransactionTypeEntity;
import org.delfin.repository.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Service
public class TransactionTypeService {

    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    public List<TransactionTypeEntity> findAll() {
        return transactionTypeRepository.findAll();
    }

    public TransactionTypeEntity findById(Long id) {
        return transactionTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction Type not found with id: " + id));
    }

    public TransactionTypeEntity save(TransactionTypeEntity transactionType) {
        return transactionTypeRepository.save(transactionType);
    }

    public TransactionTypeEntity update(TransactionTypeEntity transactionType) {
        return transactionTypeRepository.save(transactionType);
    }

    public void deleteById(Long id) {
        transactionTypeRepository.deleteById(id);
    }
}
