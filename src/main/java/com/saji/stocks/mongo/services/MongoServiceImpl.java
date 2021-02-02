package com.saji.stocks.mongo.services;


import com.saji.stocks.mongo.pojos.StockData;
import com.saji.stocks.mongo.repository.ICacheRepository;
import com.saji.stocks.redis.services.IRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author saji 11-Nov-2018
 */
@Service
public class MongoServiceImpl implements IService {


    private final ICacheRepository cacheRepository;
    private final IRedis iRedis;
    private int cacheTime;

    @Autowired
    private MongoServiceImpl(@Lazy ICacheRepository cacheRepository, IRedis iRedis, @Value("${stock.cache.time}") int cacheTime) {
        this.cacheRepository = cacheRepository;
        this.iRedis = iRedis;
        this.cacheTime = cacheTime;

    }

    @Override
    public Optional<StockData> getStockById(String symbol) {
        Optional<StockData> stockData = cacheRepository.findById(symbol);
        if (stockData.isPresent()) {
            stockData.get().setMetaData(iRedis.getMetatData(symbol));
        }
        return stockData;
    }

    @Override
    public void addStock(StockData stock) {

        cacheRepository.insert(stock);
    }

    @Override
    public void addMonthly(String symbol, String url) {
        cacheRepository.addMonthly(symbol, url);

    }

    @Override
    public void addWeekly(String symbol, String url) {
        cacheRepository.addWeekly(symbol, url);

    }

    @Override
    public void addDaily(String symbol, String url) {
        cacheRepository.addDaily(symbol, url);

    }

    @Override
    public void addThreeMonths(String symbol, String url) {
        cacheRepository.addThreeMonths(symbol, url);
    }

    @Override
    public void deleteStocks(String symbol) {

        cacheRepository.deleteStock(symbol);
    }

    private void setCache(StockData data) {
//    Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(
//            CrumbManager::getCrumb, cacheTime, TimeUnit.DAYS);
        // IntStream.
    }
}
