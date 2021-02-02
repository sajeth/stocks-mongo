package com.saji.stocks.mongo.services;

import com.saji.stocks.analysis.pojo.StocksMetaData;
import com.saji.stocks.core.services.stock.IStock;
import com.saji.stocks.finance.Stock;
import com.saji.stocks.finance.YahooFinance;
import com.saji.stocks.finance.histquotes.HistoricalQuote;
import com.saji.stocks.finance.histquotes.Interval;
import com.saji.stocks.redis.services.IRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.saji.stocks.mongo.util.DataUtil.analyseData;
import static com.saji.stocks.mongo.util.DataUtil.determineCandleType;
import static com.saji.stocks.mongo.util.DataUtil.loadData;
import static com.saji.stocks.mongo.util.DateUtil.getFirstDayOfWeek;
import static com.saji.stocks.mongo.util.DateUtil.getSecondDayOfMonth;

@Service
public class StockDataService implements IStockData {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Logger log = Logger.getLogger("StockDataService");
    private IService iService;
    private IStock iStock;
    private IRedis iRedis;
    @Value("${stock.data.years}")
    private int years;

    @Autowired
    private StockDataService(@Lazy IService iService, @Lazy IRedis iRedis, @Lazy IStock iStock) {
        this.iService = iService;
        this.iRedis = iRedis;
        this.iStock = iStock;
    }

    @Async
    private CompletableFuture<Map<String, Stock>> getHistoryByInterval(String[] symbol, final Interval interval, final Calendar to) throws IOException {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.YEAR, -years);
        return CompletableFuture.completedFuture(YahooFinance.get(symbol, from, to, interval));
    }

    @Async
    private Stock getHistoryByInterval(String symbol, final Interval interval, final Calendar to) throws IOException {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.YEAR, -years);
        Stock stock = YahooFinance.get(symbol, from, to, interval);
        if (stock == null) {
            stock = new Stock(symbol);
            stock.setHistory(new ArrayList<>());
        }
        return stock;
    }

    @Override
    public void updateMonthlyStock(String symbol) {
        try {

            Stock stock = getHistoryByInterval(symbol, Interval.MONTHLY, getSecondDayOfMonth());
            List<HistoricalQuote> list = stock.getHistory();
            iService.addMonthly(symbol, stock.getUrl());
            StocksMetaData metaData = new StocksMetaData();
            determineCandleType(metaData, list);
            iRedis.addMonthlyStock(symbol, analyseData(loadData(list, symbol), metaData));

        } catch (IOException e) {
            deleteStock(symbol);
            log.log(Level.SEVERE, e.getMessage());

        }
    }

    @Override
    public void updateWeeklyStock(String symbol) {

        try {
            Stock stock = getHistoryByInterval(symbol, Interval.WEEKLY, getFirstDayOfWeek());
            List<HistoricalQuote> list = stock.getHistory();
            iService.addWeekly(symbol, stock.getUrl());
            StocksMetaData metaData = new StocksMetaData();
            determineCandleType(metaData, list);
            iRedis.addWeeklyStock(symbol, analyseData(loadData(list, symbol), metaData));

        } catch (IOException e) {
            deleteStock(symbol);
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void updateDailyStock(String symbol) {

        try {
            Stock stock = getHistoryByInterval(symbol, Interval.DAILY, Calendar.getInstance());
            List<HistoricalQuote> list = stock.getHistory();
            iService.addDaily(symbol, stock.getUrl());
            StocksMetaData metaData = new StocksMetaData();
            determineCandleType(metaData, list);
            iRedis.addDailyStock(symbol, analyseData(loadData(list, symbol), metaData));
        } catch (IOException e) {
            deleteStock(symbol);
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public boolean isStockValid(String symbol) throws IOException {
        return YahooFinance.get(symbol) != null;
    }

    @Override
    public void populateStock(String symbol) {
        try {
            if (isStockValid(symbol)) {
                updateDailyStock(symbol);
                updateMonthlyStock(symbol);
                updateWeeklyStock(symbol);
            } else {
                iStock.deleteStock(symbol);
            }
        } catch (IOException e) {
            //      e.printStackTrace();
            iStock.deleteStock(symbol);
        }
    }

    private void deleteStock(String symbol) {
        iStock.deleteStock(symbol);
        iRedis.deleteStock(symbol);
        iService.deleteStocks(symbol);
    }

}
