package org.delfin.service;

import org.delfin.model.TransactionType;
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

    public List<TransactionType> findAll() {
        return transactionTypeRepository.findAll();
    }

    public TransactionType findById(Long id) {
        return transactionTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction Type not found with id: " + id));
    }

    public TransactionType save(TransactionType transactionType) {
        return transactionTypeRepository.save(transactionType);
    }

    public TransactionType update(TransactionType transactionType) {
        return transactionTypeRepository.save(transactionType);
    }

    public void deleteById(Long id) {
        transactionTypeRepository.deleteById(id);
    }
}
