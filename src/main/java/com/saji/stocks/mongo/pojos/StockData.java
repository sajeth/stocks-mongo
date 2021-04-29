package com.saji.stocks.mongo.pojos;


import com.saji.stocks.finance.yahoo.YahooFinance;
import com.saji.stocks.finance.yahoo.constants.Modules;
import com.saji.stocks.mongo.constants.KeyWord;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author saji 20-Sep-2018
 */
public class StockData implements Serializable {
    @Id
    private String symbol;
    private String industry;
    private Float faceValue;
    private String sector;

    //  private Map<DateConstant, StocksMetaData> metaData = new HashMap<>(3);
    private String[] data = new String[5];

    public StockData(String symbol) {
        this.symbol = symbol;
        setURL(YahooFinance.QUOTES_QUERY1V7_BASE_URL + "?symbols=" + symbol, 0);
        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + KeyWord.SUMMARY, 1);
        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.balanceSheetHistory + KeyWord.SEPARATOR + Modules.balanceSheetHistoryQuarterly, 2);
        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.cashflowStatementHistory + KeyWord.SEPARATOR + Modules.cashflowStatementHistoryQuarterly, 3);
        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.incomeStatementHistory + KeyWord.SEPARATOR + Modules.incomeStatementHistoryQuarterly, 4);

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

//    public Map<DateConstant, StocksMetaData> getMetaData() {
//        return metaData;
//    }
//
//    public void setMetaData(Map<DateConstant, StocksMetaData> metaData) {
//        this.metaData = metaData;
//    }

    public void setURL(String url, int index) {
        this.data[index] = url;

    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Float getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Float faceValue) {
        this.faceValue = faceValue;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

}
