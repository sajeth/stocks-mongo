package com.saji.stocks.mongo.services;

import com.saji.stocks.business.services.FinanceService;
import com.saji.stocks.candle.pojo.StocksMetaData;
import com.saji.stocks.core.services.stock.IStock;
import com.saji.stocks.finance.yahoo.Stock;
import com.saji.stocks.finance.yahoo.YahooFinance;
import com.saji.stocks.finance.yahoo.histquotes.HistoricalQuote;
import com.saji.stocks.finance.yahoo.histquotes.Interval;
import com.saji.stocks.finance.yahoo.quotes.stock.StockQuote;
import com.saji.stocks.finance.yahoo.quotes.stock.StockStats;
import com.saji.stocks.redis.services.IRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.saji.stocks.candle.services.CandleStickService.determineMetaData;
import static com.saji.stocks.mongo.util.DateUtil.getFirstDayOfWeek;
import static com.saji.stocks.mongo.util.DateUtil.getSecondDayOfMonth;

@Service
public class StockDataService implements IStockData {

    private final Logger log = Logger.getLogger("StockDataService");
    private IService iService;
    private IStock iStock;
    private IRedis iRedis;
    @Value("${stock.data.years}")
    private int years;
    private MathContext mx;
    private FinanceService financeService;

    @Autowired
    private StockDataService(@Lazy IService iService, @Lazy IRedis iRedis, @Lazy IStock iStock, @Value("${stock.data.precision}") int precision, @Lazy FinanceService financeService) {
        this.iService = iService;
        this.iRedis = iRedis;
        this.iStock = iStock;
        this.mx = new MathContext(precision);
        this.financeService = financeService;
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
            iService.addMonthly(symbol, stock.getUrl().get(1));
            StocksMetaData metaData = new StocksMetaData();
            determineMetaData(metaData, list, stock.getQuote(true));
            iRedis.addMonthlyStock(symbol, metaData);

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
            iService.addWeekly(symbol, stock.getUrl().get(1));
            StocksMetaData metaData = new StocksMetaData();
            determineMetaData(metaData, list, stock.getQuote(true));
            //  iRedis.addWeeklyStock(symbol, analyseData(DataUtil.loadData(list, symbol), metaData));
            iRedis.addWeeklyStock(symbol, metaData);
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
            StockQuote quote = stock.getQuote();
            StockStats stats = stock.getStats();
            if (quote != null) {
                iService.addDaily(symbol, stock.getUrl().get(1));
                StocksMetaData metaData = new StocksMetaData();
                determineMetaData(metaData, list, quote);
                iRedis.addDailyStock(symbol, metaData);
                if (stats.getMarketCap() != null && stats.getPriceBook() != null && stats.getPe() != null) {
                    financeService.validateStock(stock);
                }

//
            } else {
                throw new IOException("INVALID STOCK");
            }
        } catch (IOException e) {
            deleteStock(symbol);
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void updateThreeMonthStock(String symbols) {
        try {
            Stock stock = getHistoryByInterval(symbols, Interval.THREEMONTHS, getFirstDayOfWeek());
            List<HistoricalQuote> list = stock.getHistory();
            iService.addThreeMonths(symbols, stock.getUrl().get(1));
            StocksMetaData metaData = new StocksMetaData();
            determineMetaData(metaData, list, stock.getQuote(true));
            iRedis.addThreeMonthStock(symbols, metaData);
        } catch (IOException e) {
            deleteStock(symbols);
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public boolean isStockValid(String symbol) throws IOException {
        return YahooFinance.get(symbol) != null;
    }


    private void deleteStock(String symbol) {
        iStock.deleteStock(symbol);
        iRedis.deleteStock(symbol);
        iService.deleteStocks(symbol);
    }



}
