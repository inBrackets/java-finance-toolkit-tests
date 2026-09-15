package com.financetoolkit.strata.daycount;

import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.basics.date.DayCounts;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Feature("Strata - Day counts")
@DisplayName("DayCount.yearFraction")
class DayCountYearFractionTest {

    @DisplayName("Converts a date period into a year fraction for a given day count convention")
    @Description("""
        Given a start date, an end date and a DayCount convention (e.g. Act/360, Act/365F, 30/360 ISDA),
        yearFraction should return the numeric fraction of a year between the two dates
        as defined by that convention's algorithm.
        """)
    @ParameterizedTest(name = "Case {index}: dayCount={0}, start={1}, end={2}, expected={3}")
    @MethodSource("generateTestData")
    void testYearFraction(DayCount dayCount, LocalDate startDate, LocalDate endDate, double expected) {
        double yearFraction = computeYearFraction(dayCount, startDate, endDate);

        assertThat(yearFraction).isCloseTo(expected, within(1e-6));
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of(DayCounts.ACT_360, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 7, 1), 181d / 360),
            Arguments.of(DayCounts.ACT_365F, LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), 366d / 365),
            Arguments.of(DayCounts.THIRTY_360_ISDA, LocalDate.of(2024, 1, 15), LocalDate.of(2024, 7, 15), 0.5)
        );
    }

    @Step("Compute the {dayCount} year fraction between {startDate} and {endDate}")
    private double computeYearFraction(DayCount dayCount, LocalDate startDate, LocalDate endDate) {
        return dayCount.yearFraction(startDate, endDate);
    }
}
