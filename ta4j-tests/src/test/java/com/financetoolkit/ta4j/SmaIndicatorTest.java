package com.financetoolkit.ta4j;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("SMAIndicator")
class SmaIndicatorTest {

    @DisplayName("Computes the simple moving average over the last N close prices")
    @Description("""
        Given a bar series built from a sequence of close prices, the SMA indicator
        evaluated at the last bar for a given period should equal the arithmetic mean
        of the last 'barCount' close prices.
        """)
    @ParameterizedTest(name = "Case {index}: closePrices={0}, barCount={1}, expectedSma={2}")
    @MethodSource("generateTestData")
    void testSmaIndicator(double[] closePrices, int barCount, double expectedSma) {
        BarSeries series = buildBarSeries(closePrices);
        double actualSma = computeSma(series, barCount);

        verifySma(actualSma, expectedSma);
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5, 8d),
            Arguments.of(new double[]{10, 20, 30}, 3, 20d),
            Arguments.of(new double[]{2, 4, 6, 8}, 2, 7d)
        );
    }

    @Step("Build a bar series from close prices {closePrices}")
    private BarSeries buildBarSeries(double[] closePrices) {
        return buildSeries(closePrices);
    }

    @Step("Compute the {barCount}-period SMA at the last bar")
    private double computeSma(BarSeries series, int barCount) {
        SMAIndicator sma = new SMAIndicator(new ClosePriceIndicator(series), barCount);
        return sma.getValue(series.getEndIndex()).doubleValue();
    }

    @Step("Verify the SMA equals {expectedSma}")
    private void verifySma(double actualSma, double expectedSma) {
        assertThat(actualSma).isEqualTo(expectedSma);
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
