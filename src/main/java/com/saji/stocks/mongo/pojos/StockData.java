package com.saji.stocks.mongo.pojos;


import com.saji.stocks.candle.pojo.StocksMetaData;
import com.saji.stocks.finance.yahoo.YahooFinance;
import com.saji.stocks.finance.yahoo.constants.Modules;
import com.saji.stocks.mongo.constants.KeyWord;
import com.saji.stocks.redis.constants.DateConstant;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author saji 20-Sep-2018
 */
public class StockData implements Serializable {
    @Id
    private String symbol;
    private Map<DateConstant, StocksMetaData> metaData = new HashMap<>(3);
    private String[] data = new String[9];

    public StockData(String symbol) {
        this.symbol = symbol;
        this.data[0] = YahooFinance.QUOTES_QUERY1V7_BASE_URL + "?symbols=" + symbol;
        this.data[1] = YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + KeyWord.SUMMARY;
        this.data[2] = YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.balanceSheetHistory + KeyWord.SEPARATOR + Modules.balanceSheetHistoryQuarterly;
        this.data[3] = YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.cashflowStatementHistory + KeyWord.SEPARATOR + Modules.cashflowStatementHistoryQuarterly;
        this.data[4] = YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.incomeStatementHistory + KeyWord.SEPARATOR + Modules.incomeStatementHistoryQuarterly;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Map<DateConstant, StocksMetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<DateConstant, StocksMetaData> metaData) {
        this.metaData = metaData;
    }
}
