package com.saji.stocks.mongo.repository;


import com.saji.stocks.mongo.pojos.StockData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author saji 12-Nov-2018
 */
@Repository
public interface ICacheRepository extends MongoRepository<StockData, String> {


    default void addMonthly(String symbol, List<String> url) {

        StockData stockdata = this.findById(symbol).orElse(new StockData(symbol));
        stockdata.getData().addAll(url);
        this.save(stockdata);
    }

    default void addDaily(String symbol, List<String> url) {
        StockData stockdata = this.findById(symbol).orElse(new StockData(symbol));
        stockdata.getData().addAll(url);
        this.save(stockdata);
    }


    default void addWeekly(String symbol, List<String> url) {
        StockData stockdata = this.findById(symbol).orElse(new StockData(symbol));
        stockdata.getData().addAll(url);
        this.save(stockdata);
    }

    default void deleteStock(String symbol) {
        this.delete(this.findById(symbol).get());
    }

}
