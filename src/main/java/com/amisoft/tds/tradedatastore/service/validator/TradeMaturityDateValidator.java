package com.amisoft.tds.tradedatastore.service.validator;

import com.amisoft.tds.tradedatastore.model.Trade;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Component
public class TradeMaturityDateValidator implements Validator<Trade> {

    public static final String ERROR_MESSAGE_NULL_MATURITY_DATE = "Trade Id(%s) maturity date is null.";
    public static final String ERROR_MESSAGE_MATURITY_DATE_LESSTHAN_TODAY = "Trade Id(%s) maturity date (%s) is less than today (%s)";

    @Override
    public void validate(Trade latest, List<String> validationErrorMessages) {
        if (latest.getMaturityDate() == null) {
            validationErrorMessages.add(format(ERROR_MESSAGE_NULL_MATURITY_DATE, latest.getId()));
        } else if (latest.getMaturityDate().before(today())) {
            validationErrorMessages.add(errorMessage(latest));
        }
    }

    private String errorMessage(Trade latest) {
        return format(ERROR_MESSAGE_MATURITY_DATE_LESSTHAN_TODAY, latest.getId(), latest.getMaturityDate(), today());
    }

    private Date today() {
        return Date.valueOf(LocalDate.now());
    }
}
