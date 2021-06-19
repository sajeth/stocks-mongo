package com.saji.stocks.mongo.services;


import com.saji.stocks.mongo.pojos.StockData;
import com.saji.stocks.mongo.repository.ICacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author saji 11-Nov-2018
 */
@Service
public class MongoServiceImpl implements IService {


    private final ICacheRepository cacheRepository;
    private int cacheTime;

    @Autowired
    private MongoServiceImpl(@Lazy ICacheRepository cacheRepository, @Value("${stock.cache.time}") int cacheTime) {
        this.cacheRepository = cacheRepository;
        this.cacheTime = cacheTime;

    }

    @Override
    public void addStock(StockData stock) {

        cacheRepository.insert(stock);
    }


    @Override
    public void deleteStocks(String symbol) {

        cacheRepository.deleteStock(symbol);
    }

    @Override
    public List<StockData> getStocks() {
        return cacheRepository.findAll();
    }

    @Override
    public boolean isStockPresent(String symbol) {
        return cacheRepository.existsById(symbol);
    }

    @Override
    public void updateStock(StockData data) {
        cacheRepository.findById(data.getSymbol()).ifPresent(
                val -> {
                    val = data;
                    cacheRepository.save(val);
                }
        );

    }

}
