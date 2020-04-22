package com.amisoft.tds.tradedatastore;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.model.TradeId;

import java.sql.Date;
import java.time.LocalDate;

public class TradeFixture {

    public static TradeId tradeId(String id, int version) {
        TradeId tradeId = new TradeId();
        tradeId.setId(id);
        tradeId.setVersion(version);
        return tradeId;
    }

    public static Trade trade() {
        return trade("T1");
    }
    public static Trade trade(String id) {
        return trade(id, 1);
    }

    public static Trade trade(String id, int version) {
        TradeId tradeId = tradeId(id, version);
        Trade trade = new Trade();
        trade.setId(tradeId);
        trade.setCounterPartyId("C1");
        trade.setBookId("B1");
        trade.setMaturityDate(Date.valueOf(LocalDate.now().plusDays(10)));
        trade.setCreatedDate(Date.valueOf(LocalDate.now()));
        trade.setExpired(false);
        return trade;
    }
}
