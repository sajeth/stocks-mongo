/**
 * @author saji 06-Apr-2018
 */
module stocks.mongo {
    requires spring.boot.starter.data.mongodb;
    requires spring.data.commons;
    requires spring.context;
    requires spring.aop;
    requires java.logging;
    requires spring.beans;
    requires transitive stocks.finance;
    requires transitive stocks.analysis;
    requires transitive stocks.candlesticks;
    requires transitive stocks.core;
    requires transitive stocks.redis;
    requires spring.core;
    requires spring.data.mongodb;
    requires spring.boot.autoconfigure;
    requires org.mongodb.driver.core;
    exports com.saji.stocks.mongo.pojos;
    exports com.saji.stocks.mongo.services to stocks.services, stocks.batch;
    exports com.saji.stocks.mongo.config to spring.beans, spring.context, stocks.batch, stocks.services;
    opens com.saji.stocks.mongo.services to spring.core, spring.aop, spring.beans;
    opens com.saji.stocks.mongo.repository to spring.core, spring.beans;
    opens com.saji.stocks.mongo.config to spring.core;
}