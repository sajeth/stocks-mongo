package com.saji.stocks.mongo.pojos;


import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author saji 20-Sep-2018
 */
public class StockData implements Serializable {
    @Id
    private String symbol;

    private Set<String> data = new HashSet<>();

    public StockData(String symbol) {
        this.symbol = symbol;
    }

    public Set<String> getData() {
        return data;
    }

    public void setData(Set<String> data) {
        this.data = data;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
