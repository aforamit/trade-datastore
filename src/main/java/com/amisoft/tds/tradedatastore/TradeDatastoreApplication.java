package com.amisoft.tds.tradedatastore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TradeDatastoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TradeDatastoreApplication.class, args);
    }
}
