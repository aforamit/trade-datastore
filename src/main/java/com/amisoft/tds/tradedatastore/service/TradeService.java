package com.amisoft.tds.tradedatastore.service;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.model.TradeId;
import com.amisoft.tds.tradedatastore.repository.TradeRepository;
import com.amisoft.tds.tradedatastore.service.validator.ValidationException;
import com.amisoft.tds.tradedatastore.service.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private TradeRepository repository;

    @Autowired
    private List<Validator> validators;

    @Transactional
    public void save(Trade trade) {
        log.info("Savings trade {} ", trade);
        validate(trade);

        repository.save(trade);
    }

    private void validate(Trade trade) {
        log.info("Validators size : " + validators.size());
        List<String> validationErrorMessages = new ArrayList<>();
        validators.forEach(v -> v.validate(trade, validationErrorMessages));

        if (!validationErrorMessages.isEmpty()) {
            log.error("Validation Errors encountered : size : {} ; Error Messages : {} ",
                    validationErrorMessages.size(), validationErrorMessages);
            throw new ValidationException(validationErrorMessages);
        }
    }

    public List<Trade> findAll() {
        return repository.findAll();
    }

    public Trade get(TradeId tradeId) {
        return repository.getOne(tradeId);
    }

    @Transactional
    public void expireTrades(Date date) {
        repository.expireTrade(date);
    }


    void setValidators(List<Validator> validators) {
        this.validators = validators;
    }
}
