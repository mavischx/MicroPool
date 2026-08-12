package com.micropool.shotservice;

import org.springframework.stereotype.Component;

@Component
public class ShotCalculator {

    public ShotOutcome calculate(int angle, int power) {
        int resultCode = (angle + power) % 10;

        if (resultCode == 0) {
            return ShotOutcome.FOUL;
        } else if (resultCode <= 4) {
            return ShotOutcome.MISS;
        } else if (resultCode <= 8) {
            return ShotOutcome.POT_ONE;
        } else {
            return ShotOutcome.POT_TWO;
        }
    }
}
