package com.amisoft.tds.tradedatastore.jobs;

import com.amisoft.tds.tradedatastore.service.TradeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class BatchJobsTest {

    @Mock
    private TradeService tradeService;

    @InjectMocks
    private BatchJobs batchJobs;

    @Test
    public void shouldTriggerExpireTradesJob() {
        batchJobs.expireTradesJob();

        verify(tradeService).expireTrades(Date.valueOf(LocalDate.now()));

    }
}
