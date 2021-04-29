package com.saji.stocks.mongo.services;

import com.saji.stocks.mongo.pojos.StockData;

import java.util.List;

/**
 * @author saji 11-Nov-2018
 */
public interface IService {

    void addStock(final StockData stock);

    void deleteStocks(String symbolx);

    public List<StockData> getStocks();

    public boolean isStockPresent(String symbol);
}
