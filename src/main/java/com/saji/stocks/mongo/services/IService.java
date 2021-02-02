package com.saji.stocks.mongo.services;

import com.saji.stocks.mongo.pojos.StockData;

import java.util.List;
import java.util.Optional;

/**
 * @author saji 11-Nov-2018
 */
public interface IService {
    Optional<StockData> getStockById(final String symbol);

    void addStock(final StockData stock);

    void addMonthly(final String symbol, final List<String> url);

    void addWeekly(final String symbol, final List<String> url);

    void addDaily(final String symbol, final List<String> url);

    void deleteStocks(String symbolx);

}
