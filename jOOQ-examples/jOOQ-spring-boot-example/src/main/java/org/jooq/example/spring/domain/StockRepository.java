package org.jooq.example.spring.domain;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static org.jooq.example.db.h2.tables.Stock.STOCK;

@Repository
public class StockRepository {
    private final DSLContext dslContext;

    public StockRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public Stock save(Stock stock){
        return dslContext
                .insertInto(STOCK,STOCK.NAME, STOCK.QUANTITY)
                    .values(stock.getName(), stock.getQuantity())
                .returning()
                .fetchOneInto(Stock.class);
    }

    public Stock findById(Long id){
        return dslContext
                .select(STOCK.asterisk())
                .from(STOCK)
                .where(STOCK.ID.eq(id))
                .fetchOneInto(Stock.class);
    }

    public void incrementWithPessimisticLock(Stock stock, Long quantity){
        dslContext.transaction(configuration ->{
            final Stock findStock = DSL.using(configuration)
                                       .selectFrom(STOCK)
                                       .where(STOCK.ID.eq(stock.getId()))
                                       .forUpdate()
                                       .fetchOneInto(Stock.class);

            DSL.using(configuration)
               .update(STOCK)
                   .set(STOCK.QUANTITY, findStock.getQuantity() + quantity)
               .where(STOCK.ID.eq(stock.getId()))
               .execute();
        });
    }
}
