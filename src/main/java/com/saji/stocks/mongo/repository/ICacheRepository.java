package com.saji.stocks.mongo.repository;


import com.saji.stocks.mongo.pojos.StockData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author saji 12-Nov-2018
 */
@Repository
public interface ICacheRepository extends MongoRepository<StockData, String> {


    default void addMonthly(String symbol, String url) {

        StockData stockdata = this.findById(symbol).orElse(new StockData(symbol));
        stockdata.getData()[7] = url;
        this.save(stockdata);
    }

    default void addDaily(String symbol, String url) {
        StockData stockdata = this.findById(symbol).orElse(new StockData(symbol));
        stockdata.getData()[5] = url;
        this.save(stockdata);
    }

    default void addThreeMonths(String symbol, String url) {
        StockData stockdata = this.findById(symbol).orElse(new StockData(symbol));
        stockdata.getData()[8] = url;
        this.save(stockdata);
    }


    default void addWeekly(String symbol, String url) {
        StockData stockdata = this.findById(symbol).orElse(new StockData(symbol));
        stockdata.getData()[6] = url;
        this.save(stockdata);
    }

    default void deleteStock(String symbol) {
        this.findById(symbol).ifPresent(val -> this.delete(val));
    }

}
