package com.saji.stocks.mongo.pojos;


import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author saji 20-Sep-2018
 */
public class StockData implements Serializable {
    @Id
    private String symbol;
    private String industry;
    private BigDecimal pe;
    private BigDecimal pb;
    private BigDecimal marketCap;
    private BigDecimal peg;
    private BigDecimal EDITDA;
    private BigDecimal ROE;
    private BigDecimal dividend;
    //    private String faceValue;
//    private String sector;
//    private String securityCode;
//    private String isinNo;
    //  private Map<DateConstant, StocksMetaData> metaData = new HashMap<>(3);
    private String[] data = new String[5];

    public StockData() {
    }

    public StockData(String symbol, String industry, BigDecimal pe, BigDecimal pb, BigDecimal marketCap, BigDecimal peg, BigDecimal EDITDA, BigDecimal ROE, BigDecimal dividend) {
        this.symbol = symbol;
        this.industry = industry;
        this.pe = pe;
        this.pb = pb;
        this.marketCap = marketCap;
        this.peg = peg;
        this.EDITDA = EDITDA;
        this.ROE = ROE;
        this.dividend = dividend;
    }

    public StockData(BigDecimal pe, BigDecimal pb, BigDecimal marketCap) {
        this.pe = pe;
        this.pb = pb;
        this.marketCap = marketCap;
    }

    public StockData(String symbol) {
        this.symbol = symbol;
//        setURL(YahooFinance.QUOTES_QUERY1V7_BASE_URL + "?symbols=" + symbol, 0);
//        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + KeyWord.SUMMARY, 1);
//        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.balanceSheetHistory + KeyWord.SEPARATOR + Modules.balanceSheetHistoryQuarterly, 2);
//        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.cashflowStatementHistory + KeyWord.SEPARATOR + Modules.cashflowStatementHistoryQuarterly, 3);
//        setURL(YahooFinance.FINANCE_QUERY2V8_BASE_URL + symbol + KeyWord.MODULE + Modules.incomeStatementHistory + KeyWord.SEPARATOR + Modules.incomeStatementHistoryQuarterly, 4);

    }

    public BigDecimal getDividend() {
        return dividend;
    }

    public void setDividend(BigDecimal dividend) {
        this.dividend = dividend;
    }

    public BigDecimal getROE() {
        return ROE;
    }

    public void setROE(BigDecimal ROE) {
        this.ROE = ROE;
    }

    public BigDecimal getPeg() {
        return peg;
    }

    public void setPeg(BigDecimal peg) {
        this.peg = peg;
    }

    public BigDecimal getEDITDA() {
        return EDITDA;
    }

    public void setEDITDA(BigDecimal EDITDA) {
        this.EDITDA = EDITDA;
    }

//    public String[] getData() {
//        return data;
//    }
//
//    public void setData(String[] data) {
//        this.data = data;
//    }

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
//
//    public void setURL(String url, int index) {
//        this.data[index] = url;
//
//    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

//    public String getFaceValue() {
//        return faceValue;
//    }
//
//    public void setFaceValue(String faceValue) {
//        this.faceValue = faceValue;
//    }
//
//    public String getSector() {
//        return sector;
//    }
//
//    public void setSector(String sector) {
//        this.sector = sector;
//    }
//
//    public String getSecurityCode() {
//        return securityCode;
//    }
//
//    public void setSecurityCode(String securityCode) {
//        this.securityCode = securityCode;
//    }
//
//    public String getIsinNo() {
//        return isinNo;
//    }
//
//    public void setIsinNo(String isinNo) {
//        this.isinNo = isinNo;
//    }

    public BigDecimal getPe() {
        return pe;
    }

    public void setPe(BigDecimal pe) {
        this.pe = pe;
    }

    public BigDecimal getPb() {
        return pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

}
