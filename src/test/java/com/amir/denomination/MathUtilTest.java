package com.amir.denomination;

import com.amir.denomination.model.DenominationQuantity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static com.amir.denomination.util.MathUtil.getDividingTimes;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathUtilTest {

    @ParameterizedTest
    @MethodSource("dividingTimesUseCases")
    void testGetDividingTimes(
            BigDecimal number, BigDecimal divingValue, int expected) {
        assertEquals(expected, getDividingTimes(number, divingValue));
    }

    private static Stream<Arguments> dividingTimesUseCases() {
        return Stream.of(
                Arguments.of(new BigDecimal("10"), new BigDecimal("2"), 5),
                Arguments.of(new BigDecimal("0.02"), new BigDecimal("0.01"), 2),
                Arguments.of(new BigDecimal("0.05"), new BigDecimal("0.10"), 0),
                Arguments.of(new BigDecimal("10"), new BigDecimal("0.10"), 100)
        );
    }
}
