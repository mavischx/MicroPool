package com.micropool.shotservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShotCalculatorTest {

    private final ShotCalculator calculator = new ShotCalculator();

    @ParameterizedTest
    @CsvSource({
        // (angle + power) % 10 = 0 is FOUL
        "10, 20, FOUL",
        "0, 100, FOUL",
        "150, 50, FOUL",
        // (angle + power) % 10 = 1-4 is MISS
        "10, 21, MISS",
        "10, 22, MISS",
        "10, 23, MISS",
        "10, 24, MISS",
        // (angle + power) % 10 = 5-8 is POT_ONE
        "10, 25, POT_ONE",
        "10, 26, POT_ONE",
        "10, 27, POT_ONE",
        "10, 28, POT_ONE",
        // (angle + power) % 10 = 9 is POT_TWO
        "37, 72, POT_TWO",
        "10, 29, POT_TWO",
        "0, 99, POT_TWO"
    })
    void shouldReturnCorrectOutcome(int angle, int power, ShotOutcome expected) {
        assertEquals(expected, calculator.calculate(angle, power));
    }

    @Test
    void shouldBeDeterministic() {
        ShotOutcome first = calculator.calculate(37, 72);
        ShotOutcome second = calculator.calculate(37, 72);
        assertEquals(first, second);
    }

    @Test
    void shouldHandleBoundaryValues() {
        // Minimum valid inputs
        assertEquals(ShotOutcome.MISS, calculator.calculate(0, 1));
        // Maximum valid inputs: (359 + 100) % 10 = 9
        assertEquals(ShotOutcome.POT_TWO, calculator.calculate(359, 100));
    }
}
