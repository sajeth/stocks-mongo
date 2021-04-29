package com.saji.stocks.mongo.services;


import com.saji.stocks.mongo.pojos.StockData;

import java.io.IOException;
import java.util.List;

public interface IStockData {

    void updateMonthlyStock(String symbols);

    void updateWeeklyStock(String symbols);

    void updateDailyStock(String symbols);

    void updateThreeMonthStock(StockData symbols);

    boolean isStockValid(String symbol) throws IOException;

    List<String> getStocks();

}
