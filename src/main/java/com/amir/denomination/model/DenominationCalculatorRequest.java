package com.amir.denomination.model;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DenominationCalculatorRequest {

    @Valid
    private List<DenominationQuantity> denominationQuantityList;
}
