package org.jooq.example.spring.service;

import org.jooq.example.spring.domain.Stock;
import org.jooq.example.spring.domain.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * It is an implementation of the StockLock Service,
 * and Pestimistic lock on the stock quantity
 * @author pir
 */
@Service
@Transactional(readOnly = true)
public class StockPessimisticLockService implements StockLockService {

    private final StockRepository stockRepository;

    public StockPessimisticLockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public Stock save(Stock stock){
        return stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void increment(Stock stock, Long quantity) {
        stockRepository.incrementWithPessimisticLock(stock, quantity);
    }

    public Stock findById(Long id){
        return stockRepository.findById(id);
    }
}
