package com.amisoft.tds.tradedatastore.service;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.repository.TradeRepository;
import com.amisoft.tds.tradedatastore.service.validator.ValidationException;
import com.amisoft.tds.tradedatastore.service.validator.Validator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.amisoft.tds.tradedatastore.TradeFixture.trade;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TradeServiceTest {

    private List<Validator> validators;

    @Mock
    private TradeRepository repository;
    @Mock
    private Validator<Trade> validator1;
    @Mock
    private Validator<Trade> validator2;

    @InjectMocks
    private TradeService tradeService;

    private ArgumentCaptor<List<String>> validator1ArgCaptor;
    private ArgumentCaptor<List<String>> validator2ArgCaptor;


    @Before
    public void setUp() {
        validators = Arrays.asList(validator1, validator2);
        tradeService.setValidators(validators);

        validator1ArgCaptor = ArgumentCaptor.forClass(List.class);
        validator2ArgCaptor = ArgumentCaptor.forClass(List.class);

    }

    @Test
    public void shouldInvokeRepositoryFindAllWhenFindAllServiceIsInvoked() {
        List<Trade> expected = Arrays.asList(trade());
        when(repository.findAll()).thenReturn(expected);

        List<Trade> actual = tradeService.findAll();

        assertThat(actual, is(expected));
        verify(repository).findAll();
    }

    @Test
    public void shouldSaveTradeSuccessfullyIfNoValidationErrors() {
        Trade trade = trade();

        tradeService.save(trade);

        verify(repository).save(trade);
        verify(validator1).validate(same(trade), validator1ArgCaptor.capture());
        verify(validator2).validate(same(trade), validator2ArgCaptor.capture());

        assertThat(validator1ArgCaptor.getValue().size(), is(0));
        assertThat(validator2ArgCaptor.getValue().size(), is(0));
    }

    @Test
    public void shouldThrowValidationExceptionWhenValidationErrorOccursInFirstValidator() {
        Trade trade = trade();
        String expectedErrorMessage = "My Test Error Message";
        doAnswer(invocationOnMock -> {
            ((List)invocationOnMock.getArgument(1)).add(expectedErrorMessage);
            return null;
        }).when(validator1).validate(eq(trade), anyList());

        try{
            tradeService.save(trade);
            Assert.fail("Should have thrown ValidationException here ");
        }catch (ValidationException ve){
            assertThat(ve.getErrorMessages().size(), is(1));
            assertThat(ve.getErrorMessages().get(0), is(expectedErrorMessage));
        }catch (Throwable t){
            Assert.fail("Expected ValidationException but got : " + t);
        }

        verify(validator1).validate(same(trade), validator1ArgCaptor.capture());
        verify(validator2).validate(same(trade), validator2ArgCaptor.capture());
        verifyNoInteractions(repository);

        assertThat(validator1ArgCaptor.getValue().size(), is(1));
        assertThat(validator2ArgCaptor.getValue().size(), is(1));
    }

    @Test
    public void shouldThrowValidationExceptionWhenValidationErrorOccursInSecondValidator() {
        Trade trade = trade();
        String expectedErrorMessage = "My Test Error Message";
        doAnswer(invocationOnMock -> {
            ((List)invocationOnMock.getArgument(1)).add(expectedErrorMessage);
            return null;
        }).when(validator2).validate(eq(trade), anyList());

        try{
            tradeService.save(trade);
            Assert.fail("Should have thrown ValidationException here ");
        }catch (ValidationException ve){
            assertThat(ve.getErrorMessages().size(), is(1));
            assertThat(ve.getErrorMessages().get(0), is(expectedErrorMessage));
        }catch (Throwable t){
            Assert.fail("Expected ValidationException but got : " + t);
        }

        verify(validator1).validate(same(trade), validator1ArgCaptor.capture());
        verify(validator2).validate(same(trade), validator2ArgCaptor.capture());
        verifyNoInteractions(repository);

        assertThat(validator1ArgCaptor.getValue().size(), is(1));
        assertThat(validator2ArgCaptor.getValue().size(), is(1));
    }

    @Test
    public void shouldThrowValidationExceptionWhenValidationErrorOccursInBothValidator() {
        Trade trade = trade();
        String expectedErrorMessage = "My Test Error Message 1";
        doAnswer(invocationOnMock -> {
            ((List)invocationOnMock.getArgument(1)).add(expectedErrorMessage);
            return null;
        }).when(validator1).validate(eq(trade), anyList());
        doAnswer(invocationOnMock -> {
            ((List)invocationOnMock.getArgument(1)).add(expectedErrorMessage);
            return null;
        }).when(validator2).validate(eq(trade), anyList());


        try{
            tradeService.save(trade);
            Assert.fail("Should have thrown ValidationException here ");
        }catch (ValidationException ve){
            assertThat(ve.getErrorMessages().size(), is(2));
            assertThat(ve.getErrorMessages().get(0), is(expectedErrorMessage));
            assertThat(ve.getErrorMessages().get(1), is(expectedErrorMessage));
        }catch (Throwable t){
            Assert.fail("Expected ValidationException but got : " + t);
        }

        verify(validator1).validate(same(trade), validator1ArgCaptor.capture());
        assertThat(validator1ArgCaptor.getValue().size(), is(2));
        verify(validator2).validate(same(trade), validator1ArgCaptor.capture());
        validator1ArgCaptor.getValue();
        validator1ArgCaptor.getAllValues();
        assertThat(validator1ArgCaptor.getValue().size(), is(2));
        assertThat(validator1ArgCaptor.getAllValues().size(), is(2));

        verifyNoInteractions(repository);
    }

}
