package com.amisoft.tds.tradedatastore.repository;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.model.TradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, TradeId> {

    List<Trade> findAllByIdIdOrderByIdVersionDesc(String tradeId);

    @Query("select max(t.id.version) from Trade t where t.id.id = ?1")
    Integer findMaxVersionById(String tradeId);

    @Modifying(clearAutomatically = true)
    @Query("update Trade t set t.expired=true where t.maturityDate <= ?1")
    Integer expireTrade(Date today);
}
