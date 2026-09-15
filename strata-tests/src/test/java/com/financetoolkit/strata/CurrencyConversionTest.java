package com.financetoolkit.strata;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import io.qameta.allure.Feature;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Feature("Strata - Currency conversion")
class CurrencyConversionTest {

    @ParameterizedTest(name = "Case {index}: amount={0}, from={1}, to={2}, rate={3}, expected={4}")
    @MethodSource("generateTestData")
    void testConvertedTo(double amount, Currency from, Currency to, double rate, double expected) {
        CurrencyAmount converted = CurrencyAmount.of(from, amount).convertedTo(to, rate);

        assertThat(converted.getCurrency()).isEqualTo(to);
        assertThat(converted.getAmount()).isCloseTo(expected, within(1e-9));
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of(100d, Currency.USD, Currency.EUR, 0.90, 90d),
            Arguments.of(50d, Currency.EUR, Currency.USD, 1.10, 55d),
            Arguments.of(200d, Currency.GBP, Currency.USD, 1.25, 250d)
        );
    }
}
