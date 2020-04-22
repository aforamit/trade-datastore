package com.amisoft.tds.tradedatastore.service.validator;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.repository.TradeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.amisoft.tds.tradedatastore.TradeFixture.trade;
import static com.amisoft.tds.tradedatastore.service.validator.TradeVersionValidator.ERROR_MESSAGE_LOWER_VERSION_THAN_EXISTING;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TradeVersionValidatorTest {

    private List<String> validationErrorMessages;

    @Mock
    private TradeRepository repository;
    @InjectMocks
    private TradeVersionValidator validator;

    @Before
    public void setUp() {
        validationErrorMessages = new ArrayList<>();
    }

    @Test
    public void shouldNotAddErrorMessageWhenLatesTradeVersionIsHigherThanInDB() {
        Trade latest = trade();
        latest.getId().setVersion(3);
        when(repository.findMaxVersionById(latest.getId().getId())).thenReturn(2);

        validator.validate(latest, validationErrorMessages);

        assertThat(validationErrorMessages.size(), is(0));
        verify(repository).findMaxVersionById(latest.getId().getId());
    }

    @Test
    public void shouldNotAddErrorMessageWhenLatesTradeVersionIsOfSameVersionInDB() {
        Trade latest = trade();
        latest.getId().setVersion(3);
        when(repository.findMaxVersionById(latest.getId().getId())).thenReturn(3);

        validator.validate(latest, validationErrorMessages);

        assertThat(validationErrorMessages.size(), is(0));
        verify(repository).findMaxVersionById(latest.getId().getId());
    }

    @Test
    public void shouldAddErrorMessageWhenLatesTradeVersionIsLowerThanInDB() {
        Trade latest = trade();
        latest.getId().setVersion(2);
        when(repository.findMaxVersionById(latest.getId().getId())).thenReturn(3);

        validator.validate(latest, validationErrorMessages);

        assertThat(validationErrorMessages.size(), is(1));
        assertThat(validationErrorMessages.get(0), is(format(ERROR_MESSAGE_LOWER_VERSION_THAN_EXISTING, latest.getId().getId(), latest.getId().getVersion(), 3)));
        verify(repository).findMaxVersionById(latest.getId().getId());
    }
}
