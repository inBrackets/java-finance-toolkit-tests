package com.financetoolkit.strata.value;

import com.opengamma.strata.basics.value.ValueAdjustment;
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

@Feature("Strata - Value adjustments")
@DisplayName("ValueAdjustment.adjust")
class ValueAdjustmentTest {

    @DisplayName("Applies a value adjustment (replace, delta amount, delta multiplier, multiplier) to a base value")
    @Description("""
        Given a base value and one of the four ValueAdjustment types,
        applying the adjustment should return the expected resulting value:
        Replace substitutes the value, DeltaAmount adds a fixed amount,
        DeltaMultiplier adds a percentage of the base value, and Multiplier scales the base value.
        """)
    @ParameterizedTest(name = "Case {index}: baseValue={0}, adjustment={1}, expected={2}")
    @MethodSource("generateTestData")
    void testAdjust(double baseValue, ValueAdjustment adjustment, double expected) {
        double adjusted = applyAdjustment(adjustment, baseValue);

        assertThat(adjusted).isCloseTo(expected, within(1e-9));
    }

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            // initial value = 200, step up by a fixed amount of +20 => 220
            Arguments.of(200d, ValueAdjustment.ofDeltaAmount(20), 220d),
            // initial value = 220, step up by +10% of the base value => 220 + 22 = 242
            Arguments.of(220d, ValueAdjustment.ofDeltaMultiplier(0.1), 242d),
            // initial value = 100, scale by a factor of 1.5 => 150
            Arguments.of(100d, ValueAdjustment.ofMultiplier(1.5), 150d),
            // initial value = 123, replaced outright with 999
            Arguments.of(123d, ValueAdjustment.ofReplace(999), 999d)
        );
    }

    @Step("Apply the {adjustment} adjustment to base value {baseValue}")
    private double applyAdjustment(ValueAdjustment adjustment, double baseValue) {
        return adjustment.adjust(baseValue);
    }
}
