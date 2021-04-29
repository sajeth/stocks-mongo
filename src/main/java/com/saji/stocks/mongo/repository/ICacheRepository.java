package com.saji.stocks.mongo.repository;


import com.saji.stocks.mongo.pojos.StockData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author saji 12-Nov-2018
 */
@Repository
public interface ICacheRepository extends MongoRepository<StockData, String> {

    default void deleteStock(String symbol) {
        this.findById(symbol).ifPresent(val -> this.delete(val));
    }

    private StockData findStock(String symbol) {
        return this.findById(symbol).orElse(new StockData(symbol));
    }

}
