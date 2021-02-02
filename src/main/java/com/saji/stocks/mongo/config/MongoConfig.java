package com.saji.stocks.mongo.config;

import com.saji.stocks.business.config.BusinessConfig;
import com.saji.stocks.redis.config.RedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableCaching
@Configuration
@EnableMongoRepositories(basePackages = {"com.saji.stocks.mongo.repository"})
@PropertySource("classpath:mongo.properties")
@ComponentScan({"com.saji.stocks.mongo.services"})
@Import({RedisConfig.class, BusinessConfig.class})
public class MongoConfig {

}
