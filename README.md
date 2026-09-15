[![Run tests](https://github.com/inBrackets/java-finance-toolkit-tests/actions/workflows/tests.yml/badge.svg)](https://github.com/inBrackets/java-finance-toolkit-tests/actions/workflows/tests.yml)

A multi-module Gradle project for testing popular Java fintech / quant libraries.

See all reports: [Allure reports index](https://inbrackets.github.io/java-finance-toolkit-tests/)

## Modules

| Module | Library under test | Allure report |
|---|---|---|
| [`strata-tests`](strata-tests) | [OpenGamma Strata](https://github.com/OpenGamma/Strata) | [Report](https://inbrackets.github.io/java-finance-toolkit-tests/strata-tests/) |
| [`ta4j-tests`](ta4j-tests) | [ta4j](https://github.com/ta4j/ta4j) | [Report](https://inbrackets.github.io/java-finance-toolkit-tests/ta4j-tests/) |

The root project holds the common dependencies shared by every module (JUnit 5, Lombok, AssertJ, Allure). Each child module only adds the dependency for the library it tests.

### strata-tests

Tests are organized by topic, mirroring the [Strata "basics" documentation](https://strata.opengamma.io/docs/):

| Package | Topic | Docs |
|---|---|---|
| `currency` | FX conversion (`CurrencyAmount.convertedTo`) | |
| `daycount` | Day count conventions (`DayCount.yearFraction`) | [Day counts](https://strata.opengamma.io/day_counts) |
| `holiday` | Holiday calendars (`HolidayCalendar.isHoliday`) | [Holidays](https://strata.opengamma.io/holidays) |
| `dateadjustment` | Business day adjustments (`BusinessDayAdjustment.adjust`) | [Date adjustments](https://strata.opengamma.io/date_adjustments) |
| `schedule` | Periodic schedules (`PeriodicSchedule.createSchedule`) | [Schedules](https://strata.opengamma.io/schedules) |
| `value` | Value adjustments (`ValueAdjustment.adjust`) | [Value adjustments](https://strata.opengamma.io/value_adjustments) |
