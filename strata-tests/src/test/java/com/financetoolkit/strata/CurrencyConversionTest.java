package com.financetoolkit.strata;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Feature("Strata - Currency conversion")
@DisplayName("CurrencyAmount.convertedTo")
class CurrencyConversionTest {

    @DisplayName("Converts a currency amount into another currency using a given FX rate")
    @Description("""
        Given an amount in a source currency and a fixed FX rate,
        converting it to a target currency should return the expected converted amount 
        and the target currency should be reflected in the result.
        """)
    @ParameterizedTest(name = "Case {index}: amount={0}, from={1}, to={2}, rate={3}, expected={4}")
    @MethodSource("generateTestData")
    void testConvertedTo(double amount, Currency from, Currency to, double rate, double expected) {
        CurrencyAmount converted = convert(amount, from, to, rate);
        verifyConvertedAmount(converted, to, expected);
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of(100d, Currency.USD, Currency.EUR, 0.90, 90d),
            Arguments.of(50d, Currency.EUR, Currency.USD, 1.10, 55d),
            Arguments.of(200d, Currency.GBP, Currency.USD, 1.25, 250d)
        );
    }

    @Step("Convert {amount} {from} to {to} using FX rate {rate}")
    private CurrencyAmount convert(double amount, Currency from, Currency to, double rate) {
        return CurrencyAmount.of(from, amount).convertedTo(to, rate);
    }

    @Step("Verify the converted amount equals {expected} {to}")
    private void verifyConvertedAmount(CurrencyAmount converted, Currency to, double expected) {
        assertThat(converted.getCurrency()).isEqualTo(to);
        assertThat(converted.getAmount()).isCloseTo(expected, within(1e-9));
    }
}
