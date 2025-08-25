package com.amir.denomination;

import com.amir.denomination.model.DenominationDifference;
import com.amir.denomination.model.DenominationQuantity;
import com.amir.denomination.param.AppParams;
import com.amir.denomination.service.DenominationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DenominationServiceTest {

    AutoCloseable openMocks;

    @Mock
    AppParams appParams;

    @InjectMocks
    DenominationService denominationService;

    @BeforeEach
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(appParams.getSupportedDenominations()).thenReturn(List.of(new BigDecimal("200.00"),
                new BigDecimal("100.00"),
                new BigDecimal("50.00"),
                new BigDecimal("20.00"),
                new BigDecimal("10.00"),
                new BigDecimal("5.00"),
                new BigDecimal("2.00"),
                new BigDecimal("1.00"),
                new BigDecimal("0.50"),
                new BigDecimal("0.20"),
                new BigDecimal("0.10"),
                new BigDecimal("0.05"),
                new BigDecimal("0.02"),
                new BigDecimal("0.01")));
    }

    @AfterEach
    public void closeUp() throws Exception {
        openMocks.close();
    }

    @ParameterizedTest
    @MethodSource("calculateDenominationsQuantityUseCases")
    void testCalculateDenominationsQuantity(
            BigDecimal amount, List<DenominationQuantity> expected) {
        List<DenominationQuantity> result = denominationService.calculateDenominationsQuantity(amount);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> calculateDenominationsQuantityUseCases() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.01"), List.of(createDenominationQuantity("0.01", 1))),
                Arguments.of(new BigDecimal("0.02"), List.of(createDenominationQuantity("0.02", 1))),
                Arguments.of(new BigDecimal("0.05"), List.of(createDenominationQuantity("0.05", 1))),
                Arguments.of(new BigDecimal("0.10"), List.of(createDenominationQuantity("0.10", 1))),
                Arguments.of(new BigDecimal("0.20"), List.of(createDenominationQuantity("0.20", 1))),
                Arguments.of(new BigDecimal("0.50"), List.of(createDenominationQuantity("0.50", 1))),
                Arguments.of(new BigDecimal("1.00"), List.of(createDenominationQuantity("1.00", 1))),
                Arguments.of(new BigDecimal("2.00"), List.of(createDenominationQuantity("2.00", 1))),
                Arguments.of(new BigDecimal("5.00"), List.of(createDenominationQuantity("5.00", 1))),
                Arguments.of(new BigDecimal("10.00"), List.of(createDenominationQuantity("10.00", 1))),
                Arguments.of(new BigDecimal("20.00"), List.of(createDenominationQuantity("20.00", 1))),
                Arguments.of(new BigDecimal("50.00"), List.of(createDenominationQuantity("50.00", 1))),
                Arguments.of(new BigDecimal("100.00"), List.of(createDenominationQuantity("100.00", 1))),
                Arguments.of(new BigDecimal("200.00"), List.of(createDenominationQuantity("200.00", 1))),

                Arguments.of(new BigDecimal("999.99"), List.of(createDenominationQuantity("200.00", 4),
                        createDenominationQuantity("100.00", 1),
                        createDenominationQuantity("50.00", 1),
                        createDenominationQuantity("20.00", 2),
                        createDenominationQuantity("5.00", 1),
                        createDenominationQuantity("2.00", 2),
                        createDenominationQuantity("0.50", 1),
                        createDenominationQuantity("0.20", 2),
                        createDenominationQuantity("0.05", 1),
                        createDenominationQuantity("0.02", 2)))
        );
    }

    @ParameterizedTest
    @MethodSource("getDenominationDifferenceUseCases")
    void testGetDenominationDifference(
            List<DenominationQuantity> newList, List<DenominationQuantity> oldList,
            List<DenominationDifference> expected) {
        List<DenominationDifference> result = denominationService.getDenominationDifference(newList, oldList);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> getDenominationDifferenceUseCases() {
        return Stream.of(
                Arguments.of(List.of(createDenominationQuantity("0.02", 1)),
                        List.of(createDenominationQuantity("0.01", 1)),
                        List.of(createDenominationDifference("0.02", "+1"),
                                createDenominationDifference("0.01", "-1"))),

                Arguments.of(List.of(createDenominationQuantity("200.00", 1),
                                createDenominationQuantity("20.00", 1),
                                createDenominationQuantity("10.00", 1),
                                createDenominationQuantity("2.00", 2),
                                createDenominationQuantity("0.20", 1),
                                createDenominationQuantity("0.02", 1),
                                createDenominationQuantity("0.01", 1)),
                        List.of(createDenominationQuantity("20.00", 2),
                                createDenominationQuantity("5.00", 1),
                                createDenominationQuantity("0.20", 1),
                                createDenominationQuantity("0.10", 1),
                                createDenominationQuantity("0.02", 1)),
                        List.of(createDenominationDifference("200.00", "+1"),
                                createDenominationDifference("20.00", "-1"),
                                createDenominationDifference("10.00", "+1"),
                                createDenominationDifference("5.00", "-1"),
                                createDenominationDifference("2.00", "+2"),
                                createDenominationDifference("0.20", "0"),
                                createDenominationDifference("0.10", "-1"),
                                createDenominationDifference("0.02", "0"),
                                createDenominationDifference("0.01", "+1")))
        );
    }

    private static DenominationQuantity createDenominationQuantity(String denomination, int quantity) {
        return DenominationQuantity.builder()
                .denomination(denomination)
                .quantity(quantity)
                .build();
    }

    private static DenominationDifference createDenominationDifference(String denomination, String difference) {
        return DenominationDifference.builder()
                .denomination(denomination)
                .difference(difference)
                .build();
    }

}
