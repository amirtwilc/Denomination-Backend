package com.amir.denomination.controller;

import com.amir.denomination.api.DenominationApi;
import com.amir.denomination.model.DenominationCalculatorRequest;
import com.amir.denomination.model.DenominationDifference;
import com.amir.denomination.model.DenominationQuantity;
import com.amir.denomination.model.DenominationCalculatorResponse;
import com.amir.denomination.service.DenominationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DenominationController implements DenominationApi {

    private final DenominationService denominationService;

    /**
     * Calculates the denominations that make up given amount.
     * If a previous calculation is passed, the calculator also returns a difference calculation between both results
     * @param amount the amount for denomination calculation
     * @param denominationCalculatorRequest optional object that contains a previous calculation
     * @return the calculation(s) result(s)
     */

    @Override
    public ResponseEntity<DenominationCalculatorResponse> calculateDenominations(BigDecimal amount,
                                                                                 DenominationCalculatorRequest denominationCalculatorRequest) {
        log.info("Started with amount = {}", amount);
        List<DenominationQuantity> quantityResultList = denominationService.calculateDenominationsQuantity(amount);

        List<DenominationDifference> differenceResultList = null;
        if (shouldCalculateDifference(denominationCalculatorRequest)) {
            differenceResultList = denominationService.getDenominationDifference(quantityResultList,
                    denominationCalculatorRequest.getDenominationQuantityList());
        }

        return ResponseEntity.ok(DenominationCalculatorResponse.builder()
                        .denominationQuantityList(quantityResultList)
                        .denominationDifferenceList(differenceResultList)
                .build());
    }

    private boolean shouldCalculateDifference(DenominationCalculatorRequest request) {
        return Objects.nonNull(request) &&
                !CollectionUtils.isEmpty(request.getDenominationQuantityList());
    }
}
