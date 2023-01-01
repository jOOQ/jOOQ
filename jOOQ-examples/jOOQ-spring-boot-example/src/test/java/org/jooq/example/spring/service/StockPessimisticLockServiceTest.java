package org.jooq.example.spring.service;

import org.jooq.example.spring.domain.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockPessimisticLockServiceTest {

    @Autowired
    private StockPessimisticLockService stockPessimisticLockService;

    /**
     * After saving the number of chairs as zero,
     * send 100 requests to increase the stock quantity by two.
     * @throws InterruptedException
     * @author pir
     */
    @Test
    void incrementWithPessimisticLock() throws InterruptedException {
        final int threadCount = 100;
        final Long plusQuantity = 2L;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        final Stock chair = new Stock("chair", 0L);
        final Stock expected = stockPessimisticLockService.save(chair);

        final List<Thread> workers = Stream
                                .generate(() -> new Thread(
                                        new PessimisticLockWorker(countDownLatch, expected, plusQuantity)
                                ))
                                .limit(threadCount)
                                .collect(Collectors.toList());
        workers.forEach(Thread::start);
        countDownLatch.await();

        final Stock actual = stockPessimisticLockService.findById(expected.getId());
        assertEquals(actual.getQuantity(), threadCount * plusQuantity);
    }

    public class PessimisticLockWorker implements Runnable{
        private CountDownLatch countDownLatch;
        private Stock stock;
        private Long quantity;

        public PessimisticLockWorker(CountDownLatch countDownLatch, Stock stock, Long quantity){
            this.countDownLatch = countDownLatch;
            this.stock = stock;
            this.quantity = quantity;
        }

        @Override
        public void run() {
            stockPessimisticLockService.increment(stock, quantity);
            countDownLatch.countDown();
        }
    }
}