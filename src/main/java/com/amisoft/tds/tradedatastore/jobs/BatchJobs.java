package com.amisoft.tds.tradedatastore.jobs;

import com.amisoft.tds.tradedatastore.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@EnableScheduling
public class BatchJobs {
    public static final Logger log = LoggerFactory.getLogger(BatchJobs.class);

    @Autowired
    private TradeService tradeService;

    @Scheduled(cron = "${batch.job.expire.trades.cron}")
    public void expireTradesJob() {
        log.info("Batch Job : expireTradeJobs : triggering now : " + LocalTime.now());
        try {
            tradeService.expireTrades(Date.valueOf(LocalDate.now()));
        } catch (Throwable t) {
            log.error("Batch Job : expireTradeJobs : failed due to exceptions : " + t.getMessage(), t );
        }
        log.info("Batch Job : expireTradeJobs : completed : " + LocalTime.now());
    }
}
