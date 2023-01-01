package org.jooq.example.spring.service;

import org.jooq.example.spring.domain.Stock;

/**
 * The interface that provides the Lock service for the Stock domain.
 * @author pir
 */
public interface StockLockService {

    /**
     * Lock and increase the stock quantity.
     * @param stock
     * @param quantity
     * @author pir
     */
    void increment(Stock stock, Long quantity);
}
