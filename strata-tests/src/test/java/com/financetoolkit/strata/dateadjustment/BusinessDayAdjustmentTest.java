package com.financetoolkit.strata.dateadjustment;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.BusinessDayConventions;
import com.opengamma.strata.basics.date.HolidayCalendarId;
import com.opengamma.strata.basics.date.HolidayCalendarIds;
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

@Feature("Strata - Date adjustments")
@DisplayName("BusinessDayAdjustment.adjust")
class BusinessDayAdjustmentTest {

    @DisplayName("Shifts a date onto a valid business day using a business day convention and holiday calendar")
    @Description("""
        Given a BusinessDayConvention (Following, Preceding, ModifiedFollowing) and a holiday calendar,
        adjusting a date that falls on a weekend or holiday should move it to the nearest valid
        business day as defined by that convention's rule.
        """)
    @ParameterizedTest(name = "Case {index}: convention={0}, calendar={1}, date={2}, expected={3}")
    @MethodSource("generateTestData")
    void testAdjust(BusinessDayConvention convention, HolidayCalendarId calendarId, LocalDate date, LocalDate expected) {
        LocalDate adjusted = adjustDate(convention, calendarId, date);

        assertThat(adjusted).isEqualTo(expected);
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            // Saturday 2024-01-06 -> following business day is Monday 2024-01-08
            Arguments.of(BusinessDayConventions.FOLLOWING, HolidayCalendarIds.USNY,
                LocalDate.of(2024, 1, 6), LocalDate.of(2024, 1, 8)),
            // Sunday 2024-01-07 -> preceding business day is Friday 2024-01-05
            Arguments.of(BusinessDayConventions.PRECEDING, HolidayCalendarIds.GBLO,
                LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 5)),
            // Saturday 2024-03-30 -> following would roll into April, so modified following rolls back
            // to the preceding business day; Friday 2024-03-29 is Good Friday (a EUTA holiday), so it
            // rolls back further to Thursday 2024-03-28
            Arguments.of(BusinessDayConventions.MODIFIED_FOLLOWING, HolidayCalendarIds.EUTA,
                LocalDate.of(2024, 3, 30), LocalDate.of(2024, 3, 28)),
            // a date that already falls on a business day is left unchanged
            Arguments.of(BusinessDayConventions.FOLLOWING, HolidayCalendarIds.USNY,
                LocalDate.of(2024, 1, 3), LocalDate.of(2024, 1, 3))
        );
    }

    @Step("Adjust {date} using {convention} and calendar {calendarId}")
    private LocalDate adjustDate(BusinessDayConvention convention, HolidayCalendarId calendarId, LocalDate date) {
        BusinessDayAdjustment adjustment = BusinessDayAdjustment.of(convention, calendarId);
        return adjustment.adjust(date, ReferenceData.standard());
    }
}
