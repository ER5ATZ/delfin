package org.delfin.service;

import org.delfin.model.entity.TransactionTypeEntity;
import org.delfin.repository.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Service
public class TransactionTypeService {

    private static List<TransactionTypeEntity> transactionTypes;

    private final TransactionTypeRepository transactionTypeRepository;

    @Autowired
    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
        transactionTypes = findAll();
    }

    public List<TransactionTypeEntity> findAll() {
        return transactionTypeRepository.findAll();
    }

    public static TransactionTypeEntity getTransactionTypeByCalculationType(String calculation) {
        for (TransactionTypeEntity tt : transactionTypes) {
            if (tt.getCalculation().equals(calculation.toLowerCase())) {
                return tt;
            }
        }

        // this should not happen
        return new TransactionTypeEntity();
    }
    public static TransactionTypeEntity getTransactionTypeById(long id) {
        for (TransactionTypeEntity tt : transactionTypes) {
            if (tt.getId() == id) {
                return tt;
            }
        }

        // this should not happen
        return new TransactionTypeEntity();
    }
}
