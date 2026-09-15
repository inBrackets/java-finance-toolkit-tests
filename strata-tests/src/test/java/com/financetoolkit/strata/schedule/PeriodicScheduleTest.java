package com.financetoolkit.strata.schedule;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.date.BusinessDayConventions;
import com.opengamma.strata.basics.date.HolidayCalendarIds;
import com.opengamma.strata.basics.schedule.Frequency;
import com.opengamma.strata.basics.schedule.PeriodicSchedule;
import com.opengamma.strata.basics.schedule.Schedule;
import com.opengamma.strata.basics.schedule.StubConvention;
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

@Feature("Strata - Schedules")
@DisplayName("PeriodicSchedule.createSchedule")
class PeriodicScheduleTest {

    @DisplayName("Builds a periodic schedule and generates the expected number of regular periods")
    @Description("""
        Given a start date, end date and periodic frequency (with a business day adjustment and,
        where applicable, a stub convention), createSchedule should expand the definition into
        the expected number of schedule periods.
        """)
    @ParameterizedTest(name = "Case {index}: start={0}, end={1}, frequency={2}, expectedPeriods={3}")
    @MethodSource("generateTestData")
    void testCreateSchedule(LocalDate startDate, LocalDate endDate, Frequency frequency, int expectedPeriods) {
        Schedule schedule = buildSchedule(startDate, endDate, frequency);

        assertThat(schedule.size()).isEqualTo(expectedPeriods);
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            // 2024-01-01 to 2025-01-01, quarterly (P3M) => 4 regular periods
            Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), Frequency.P3M, 4),
            // 2024-01-01 to 2024-07-01, monthly (P1M) => 6 regular periods
            Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 7, 1), Frequency.P1M, 6),
            // 2024-01-01 to 2025-01-01, semi-annual (P6M) => 2 regular periods
            Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), Frequency.P6M, 2)
        );
    }

    @Step("Build a {frequency} schedule from {startDate} to {endDate}")
    private Schedule buildSchedule(LocalDate startDate, LocalDate endDate, Frequency frequency) {
        PeriodicSchedule definition = PeriodicSchedule.builder()
            .startDate(startDate)
            .endDate(endDate)
            .frequency(frequency)
            .businessDayAdjustment(BusinessDayAdjustment.of(BusinessDayConventions.MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
            .stubConvention(StubConvention.NONE)
            .build();

        return definition.createSchedule(ReferenceData.standard());
    }
}
