package com.amisoft.tds.tradedatastore.controller;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.model.TradeId;
import com.amisoft.tds.tradedatastore.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/trades")
public class TradeResource {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/save")
    public void save(@RequestBody Trade trade) {
        tradeService.save(trade);
    }

    @PostMapping("/get")
    public void get(@RequestBody TradeId tradeId) {
        tradeService.get(tradeId);
    }
}
