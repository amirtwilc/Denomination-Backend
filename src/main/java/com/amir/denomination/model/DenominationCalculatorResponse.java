package com.amir.denomination.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DenominationCalculatorResponse {

    private List<DenominationQuantity> denominationQuantityList;
    private List<DenominationDifference> denominationDifferenceList;
}
