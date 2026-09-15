package com.financetoolkit.ta4j;

import io.qameta.allure.Feature;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.averages.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("ta4j - SMA indicator")
class SmaIndicatorTest {

    @ParameterizedTest(name = "Case {index}: closePrices={0}, barCount={1}, expectedSma={2}")
    @MethodSource("generateTestData")
    void testSmaIndicator(double[] closePrices, int barCount, double expectedSma) {
        BarSeries series = buildSeries(closePrices);
        SMAIndicator sma = new SMAIndicator(new ClosePriceIndicator(series), barCount);

        double actualSma = sma.getValue(series.getEndIndex()).doubleValue();

        assertThat(actualSma).isEqualTo(expectedSma);
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5, 8d),
            Arguments.of(new double[]{10, 20, 30}, 3, 20d),
            Arguments.of(new double[]{2, 4, 6, 8}, 2, 7d)
        );
    }

    private static BarSeries buildSeries(double[] closePrices) {
        BarSeries series = new BaseBarSeriesBuilder().withName("test-series").build();
        Instant endTime = Instant.parse("2024-01-01T00:00:00Z");

        for (double closePrice : closePrices) {
            series.barBuilder()
                .timePeriod(Duration.ofDays(1))
                .endTime(endTime)
                .openPrice(closePrice)
                .highPrice(closePrice)
                .lowPrice(closePrice)
                .closePrice(closePrice)
                .volume(0)
                .add();

            endTime = endTime.plus(Duration.ofDays(1));
        }

        return series;
    }
}
