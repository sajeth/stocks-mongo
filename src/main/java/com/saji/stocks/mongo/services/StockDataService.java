package com.saji.stocks.mongo.services;

import com.saji.stocks.business.services.IFinance;
import com.saji.stocks.candle.pojo.StocksMetaData;
import com.saji.stocks.finance.yahoo.Stock;
import com.saji.stocks.finance.yahoo.YahooFinance;
import com.saji.stocks.finance.yahoo.histquotes.HistoricalQuote;
import com.saji.stocks.finance.yahoo.histquotes.Interval;
import com.saji.stocks.finance.yahoo.quotes.stock.StockQuote;
import com.saji.stocks.finance.yahoo.quotes.stock.StockStats;
import com.saji.stocks.mongo.pojos.StockData;
import com.saji.stocks.redis.services.IRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.MathContext;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.saji.stocks.candle.services.CandleStickService.determineMetaData;
import static com.saji.stocks.mongo.util.DateUtil.getFirstDayOfWeek;
import static com.saji.stocks.mongo.util.DateUtil.getSecondDayOfMonth;

@Service
public class StockDataService implements IStockData {

    private final Logger log = Logger.getLogger("StockDataService");
    private IService iService;
    private IRedis iRedis;
    @Value("${stock.data.years}")
    private int years;
    private MathContext mx;
    private IFinance iFinance;

    @Autowired
    private StockDataService(@Lazy IService iService, @Lazy IRedis iRedis, @Value("${stock.data.precision}") int precision, @Lazy IFinance iFinance) {
        this.iService = iService;
        this.iRedis = iRedis;
        this.mx = new MathContext(precision);
        this.iFinance = iFinance;
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
            if (null != stock) {
                //   addStock(new StockData(symbol));
                List<HistoricalQuote> list = stock.getHistory();
                StocksMetaData metaData = new StocksMetaData();
                metaData.setUrl(stock.getUrl().get(1));
                determineMetaData(metaData, list, stock.getQuote(true));
                iRedis.addMonthlyStock(symbol, metaData);
            }
        } catch (IOException e) {
            deleteStock(symbol);
            log.log(Level.SEVERE, e.getMessage());

        }
    }

    @Override
    public void updateWeeklyStock(String symbol) {

        try {
            Stock stock = getHistoryByInterval(symbol, Interval.WEEKLY, getFirstDayOfWeek());
            if (null != stock) {
                //     addStock(new StockData(symbol));
                List<HistoricalQuote> list = stock.getHistory();
                StocksMetaData metaData = new StocksMetaData();
                metaData.setUrl(stock.getUrl().get(1));
                determineMetaData(metaData, list, stock.getQuote(true));
                //  iRedis.addWeeklyStock(symbol, analyseData(DataUtil.loadData(list, symbol), metaData));
                iRedis.addWeeklyStock(symbol, metaData);
            }
        } catch (IOException e) {
            deleteStock(symbol);
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void updateDailyStock(StockData data) {
        final String symbol = data.getSymbol();
        try {

            Stock stock = getHistoryByInterval(symbol, Interval.DAILY, Calendar.getInstance());
            List<HistoricalQuote> list = stock.getHistory();
            StockQuote quote = stock.getQuote();
            StockStats stats = stock.getStats();
            if (quote != null && null != quote.getOpen()) {
                data.setMarketCap(stats.getMarketCap());
                Optional.ofNullable(stats.getPeg()).ifPresent(val -> data.setPeg(val));
                Optional.ofNullable(stats.getEBITDA()).ifPresent(val -> data.setEDITDA(val));
                Optional.ofNullable(stats.getROE()).ifPresent(val -> data.setROE(val));
                Optional.ofNullable(stock.getDividend().getAnnualYield()).ifPresent(val -> data.setDividend(val));
                Optional.ofNullable(stats.getPe()).ifPresent(val -> data.setPe(val));
                Optional.ofNullable(stats.getPriceBook()).ifPresent(val -> data.setPb(val));
                iService.updateStock(data);
                StocksMetaData metaData = new StocksMetaData();
                metaData.setUrl(stock.getUrl().get(1));
                determineMetaData(metaData, list, quote);
                iRedis.addDailyStock(symbol, metaData);
                if (stats.getMarketCap() != null && stats.getPriceBook() != null && stats.getPe() != null) {
                    iFinance.validateStock(stock);
                }
            } else {
                throw new IOException("INVALID STOCK");
            }
        } catch (IOException e) {
            deleteStock(symbol);
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void updateThreeMonthStock(StockData data) {
        try {
            Stock stock = getHistoryByInterval(data.getSymbol(), Interval.THREEMONTHS, getFirstDayOfWeek());
            if (null != stock) {
                List<HistoricalQuote> list = stock.getHistory();
                addStock(data);
                StocksMetaData metaData = new StocksMetaData();
                metaData.setUrl(stock.getUrl().get(1));
                determineMetaData(metaData, list, stock.getQuote(true));
                iRedis.addThreeMonthStock(data.getSymbol(), metaData);
            }
        } catch (IOException e) {
            deleteStock(data.getSymbol());
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public boolean isStockValid(String symbol) throws IOException {
        return YahooFinance.get(symbol) != null;
    }


    @Override
    public List<String> getStocks() {
        return iService.getStocks().stream().map(StockData::getSymbol).collect(Collectors.toList());
    }

    private void addStock(StockData data) {
        if (!iService.isStockPresent(data.getSymbol())) {
            iService.addStock(data);
        }
    }

    private void deleteStock(String symbol) {
        // iStock.deleteStock(symbol);
        iRedis.deleteStock(symbol);
        iService.deleteStocks(symbol);
    }


}
