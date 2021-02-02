package com.saji.stocks.mongo.services;


import java.io.IOException;

public interface IStockData {

    void updateMonthlyStock(String symbols);

    void updateWeeklyStock(String symbols);

    void updateDailyStock(String symbols);

    boolean isStockValid(String symbol) throws IOException;

    void populateStock(String symbol);

}
