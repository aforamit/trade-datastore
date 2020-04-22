package com.amisoft.tds.tradedatastore;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.repository.TradeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.amisoft.tds.tradedatastore.TradeFixture.trade;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TradeDataStoreIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void shouldSaveTradeInDatabaseSuccessfully() {
        Trade trade = trade();

        Trade tradeDB = tradeRepository.getOne(trade.getId());
        assertThat(tradeDB, is(notNullValue()));

        this.restTemplate.postForObject("http://localhost:" + port + "/api/trades/save", trade, Trade.class);

        tradeDB = tradeRepository.getOne(trade.getId());
        assertThat(tradeDB, is(notNullValue()));
    }
}
