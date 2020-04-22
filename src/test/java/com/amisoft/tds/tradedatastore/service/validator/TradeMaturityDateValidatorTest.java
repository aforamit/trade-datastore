package com.amisoft.tds.tradedatastore.service.validator;

import com.amisoft.tds.tradedatastore.model.Trade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.amisoft.tds.tradedatastore.TradeFixture.trade;
import static com.amisoft.tds.tradedatastore.service.validator.TradeMaturityDateValidator.ERROR_MESSAGE_MATURITY_DATE_LESSTHAN_TODAY;
import static com.amisoft.tds.tradedatastore.service.validator.TradeMaturityDateValidator.ERROR_MESSAGE_NULL_MATURITY_DATE;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TradeMaturityDateValidatorTest {

    private TradeMaturityDateValidator validator;
    private List<String> validationErrorMessages;

    @Before
    public void setUp() {
        validator = new TradeMaturityDateValidator();
        validationErrorMessages = new ArrayList<>();
    }

    @Test
    public void shouldAddErrorMessageWhenMaturityDateIsNull() {
        Trade tradeWithNullMaturityDate = trade();
        tradeWithNullMaturityDate.setMaturityDate(null);

        validator.validate(tradeWithNullMaturityDate, validationErrorMessages);

        assertThat(validationErrorMessages.size(), is(1));
        assertThat(validationErrorMessages.get(0), is(format(ERROR_MESSAGE_NULL_MATURITY_DATE, tradeWithNullMaturityDate.getId())));
    }

    @Test
    public void shouldAddErrorMessageWhenMaturityDateIsLessThanToday() {
        Trade tradeWithMaturityDateInPast = trade();
        tradeWithMaturityDateInPast.setMaturityDate(Date.valueOf(LocalDate.now().minusDays(2)));

        validator.validate(tradeWithMaturityDateInPast, validationErrorMessages);

        assertThat(validationErrorMessages.size(), is(1));
        assertThat(validationErrorMessages.get(0), is(format(ERROR_MESSAGE_MATURITY_DATE_LESSTHAN_TODAY,
                tradeWithMaturityDateInPast.getId(), tradeWithMaturityDateInPast.getMaturityDate(), Date.valueOf(LocalDate.now()))));
    }

    @Test
    public void shouldNotAddErrorMessageWhenMaturityDateIsInFuture() {
        Trade tradeWithFutureMaturityDate = trade();

        validator.validate(tradeWithFutureMaturityDate, validationErrorMessages);

        assertThat(validationErrorMessages.size(), is(0));
    }
}
