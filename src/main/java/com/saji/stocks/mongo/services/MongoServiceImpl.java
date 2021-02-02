package com.saji.stocks.mongo.services;

import com.saji.stocks.mongo.pojos.StockData;
import com.saji.stocks.mongo.repository.ICacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author saji 11-Nov-2018
 */
@Service
public class MongoServiceImpl implements IService {


    private final ICacheRepository cacheRepository;

    @Autowired
    private MongoServiceImpl(@Lazy ICacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @Override
    public Optional<StockData> getStockById(String symbol) {
        return cacheRepository.findById(symbol);
    }


    @Override
    public void addStock(StockData stock) {

        cacheRepository.insert(stock);
    }

    @Override
    public void addMonthly(String symbol, List<String> url) {
        cacheRepository.addMonthly(symbol, url);

    }

    @Override
    public void addWeekly(String symbol, List<String> url) {
        cacheRepository.addWeekly(symbol, url);

    }

    @Override
    public void addDaily(String symbol, List<String> url) {
        cacheRepository.addDaily(symbol, url);

    }

    @Override
    public void deleteStocks(String symbol) {

        cacheRepository.deleteStock(symbol);
    }


}
