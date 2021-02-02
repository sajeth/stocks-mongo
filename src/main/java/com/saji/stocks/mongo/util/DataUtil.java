package com.saji.stocks.mongo.util;

import com.saji.stocks.analysis.core.BarSeries;
import com.saji.stocks.analysis.core.BaseBarSeries;
import com.saji.stocks.analysis.pojo.StocksMetaData;
import com.saji.stocks.candle.pojo.Candle;
import com.saji.stocks.candle.services.CandleStickService;
import com.saji.stocks.finance.histquotes.HistoricalQuote;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DataUtil {
    public static StocksMetaData analyseData(BarSeries barSeries, StocksMetaData stocksMetaData) {
        //  stocksMetaData.setCompareNumType(CompareNumTypes.test(barSeries));
        //  stocksMetaData.setWalkforward(WalkForward.buildStrategy(barSeries));

        return stocksMetaData;
    }

    public static BarSeries loadData(List<HistoricalQuote> history, String symbol) {

        BarSeries barSeries = new BaseBarSeries(symbol);
        history.forEach(val -> {
            barSeries.addBar(ZonedDateTime.ofInstant(val.getDate().toInstant(), java.time.ZoneId.systemDefault()), val.getOpen().doubleValue(), val.getHigh().doubleValue(), val.getLow().doubleValue(), val.getClose().doubleValue(), val.getVolume().doubleValue());
        });
        return barSeries;
    }

    public static void determineCandleType(StocksMetaData metaData, List<HistoricalQuote> list) {
        int size;
        Double lossMax = Double.MIN_VALUE, totalLoss = 0.0, totalGains = 0.0, gainMin = Double.MAX_VALUE;
        Double gain, loss;
        List<Candle> data = list.subList(list.size() - 4, list.size() - 1).stream()
                .map(val -> new Candle(val.getOpen(), val.getClose(), val.getHigh(), val.getLow())).collect(Collectors.toList());
        CompletableFuture.runAsync(() -> {
            metaData.setPattern(CandleStickService.determineCandleType(data));
        });
        size = data.size();

        for (Candle val : data) {
            gain = val.getHigh().subtract(val.getOpen()).doubleValue();
            loss = val.getOpen().subtract(val.getLow()).doubleValue();
            lossMax = Math.max(lossMax, loss);
            gainMin = Math.min(gain, gainMin);
            totalGains += gain;
            totalLoss += loss;
        }
        metaData.setLowestReward(gainMin);
        metaData.setAverageReward(totalGains / size);
        metaData.setAverageRisk(totalLoss / size);
        metaData.setHighestRisk(lossMax);
    }
}
