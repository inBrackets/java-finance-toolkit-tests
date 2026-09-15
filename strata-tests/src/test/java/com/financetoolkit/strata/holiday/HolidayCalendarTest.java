package com.financetoolkit.strata.holiday;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.HolidayCalendar;
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

@Feature("Strata - Holiday calendars")
@DisplayName("HolidayCalendar.isHoliday")
class HolidayCalendarTest {

    @DisplayName("Determines whether a given date is a holiday on a standard holiday calendar")
    @Description("""
        Given a standard HolidayCalendarId (e.g. USNY, GBLO) resolved against the standard reference data,
        isHoliday should correctly classify well-known dates, such as New Year's Day and an
        ordinary midweek business day, as holiday or business day.
        """)
    @ParameterizedTest(name = "Case {index}: calendar={0}, date={1}, expectedHoliday={2}")
    @MethodSource("generateTestData")
    void testIsHoliday(HolidayCalendarId calendarId, LocalDate date, boolean expectedHoliday) {
        boolean isHoliday = resolveAndCheck(calendarId, date);

        assertThat(isHoliday).isEqualTo(expectedHoliday);
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            // New Year's Day 2024 (a Monday) is a holiday in New York
            Arguments.of(HolidayCalendarIds.USNY, LocalDate.of(2024, 1, 1), true),
            // an ordinary Tuesday is a business day in New York
            Arguments.of(HolidayCalendarIds.USNY, LocalDate.of(2024, 1, 2), false),
            // Christmas Day 2024 (a Wednesday) is a holiday in London
            Arguments.of(HolidayCalendarIds.GBLO, LocalDate.of(2024, 12, 25), true),
            // a Saturday is always a holiday (weekend) on the EUTA (TARGET) calendar
            Arguments.of(HolidayCalendarIds.EUTA, LocalDate.of(2024, 3, 2), true)
        );
    }

    @Step("Resolve {calendarId} and check whether {date} is a holiday")
    private boolean resolveAndCheck(HolidayCalendarId calendarId, LocalDate date) {
        HolidayCalendar calendar = calendarId.resolve(ReferenceData.standard());
        return calendar.isHoliday(date);
    }
}
