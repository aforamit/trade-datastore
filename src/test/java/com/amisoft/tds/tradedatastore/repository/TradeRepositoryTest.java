package com.amisoft.tds.tradedatastore.repository;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.model.TradeId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.amisoft.tds.tradedatastore.TradeFixture.trade;
import static com.amisoft.tds.tradedatastore.TradeFixture.tradeId;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TradeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void shouldRetrieveTradeBasedOnTradeIdSuccessfully() {
        Trade trade = trade("T1", 2);
        save(trade);

        Optional<Trade> actual = tradeRepository.findById(tradeId("T1", 2));

        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get(), is(trade));
    }

    @Test
    public void shouldRetrieveAllVersionsOfATradeId() {
        List<Trade> trades = asList(trade("T1", 1), trade("T1", 2), trade("T1", 3));
        saveAll(trades);

        List<Trade> tradesOrderedByVersion = tradeRepository.findAllByIdIdOrderByIdVersionDesc("T1");

        assertThat(tradesOrderedByVersion.size(), is(3));
        assertThat(tradesOrderedByVersion.get(0).getId().getVersion(), is(3));
        assertThat(tradesOrderedByVersion.get(1).getId().getVersion(), is(2));
        assertThat(tradesOrderedByVersion.get(2).getId().getVersion(), is(1));
    }

    @Test
    public void shouldRetrieveMaxVersionForATradeId() {
        List<Trade> trades = asList(trade("T1", 1), trade("T1", 2), trade("T1", 3));
        saveAll(trades);

        Integer maxVersion = tradeRepository.findMaxVersionById("T1");

        assertThat(maxVersion, is(3));
    }

    @Test
    public void shouldExpireTradesWhereMaturityDateIsLowertThanGivenDate() {
        LocalDate expiryDate = LocalDate.of(2020, 02, 10);

        Trade tradesToBeExpired1 = trade("T1", 1);
        tradesToBeExpired1.setMaturityDate(Date.valueOf(expiryDate.minusDays(3)));
        Trade tradesToBeExpired2 = trade("T2", 2);
        tradesToBeExpired2.setMaturityDate(Date.valueOf(expiryDate.minusDays(2)));
        Trade tradesToBeExpired3 = trade("T3", 3);
        tradesToBeExpired3.setMaturityDate(Date.valueOf(expiryDate));

        Trade tradesNotToExpire4 = trade("T4", 1);
        tradesNotToExpire4.setMaturityDate(Date.valueOf(expiryDate.plusDays(3)));
        Trade tradesNotToExpire5 = trade("T5", 2);
        tradesNotToExpire5.setMaturityDate(Date.valueOf(expiryDate.plusDays(2)));

        List<TradeId> expiredTradeIds = asList(tradesToBeExpired1.getId(), tradesToBeExpired2.getId(), tradesToBeExpired3.getId());

        List<Trade> trades = asList(tradesToBeExpired1, tradesToBeExpired2, tradesToBeExpired3, tradesNotToExpire4, tradesNotToExpire5);
        saveAll(trades);

        tradeRepository.findAll().forEach(t -> {
            assertThat(t.isExpired(), is(false));
        });

        Integer updateCount = tradeRepository.expireTrade(Date.valueOf(expiryDate));
        assertThat(updateCount, is(3));
        tradeRepository.findAll().forEach(t -> {
            assertThat(t.isExpired(), is(expiredTradeIds.contains(t.getId())));
        });
    }

    private void saveAll(List<Trade> trades) {
        trades.stream().forEach(t -> {
            save(t);
        });
    }

    private void save(Trade trade) {
        entityManager.persist(trade);
        entityManager.flush();
    }
}
