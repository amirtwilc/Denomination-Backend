package com.amir.denomination.api;

import com.amir.denomination.model.DenominationCalculatorRequest;
import com.amir.denomination.model.DenominationCalculatorResponse;
import com.amir.denomination.validator.CustomDecimalMin;
import com.amir.denomination.validator.CustomDigits;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Validated
@CrossOrigin //So this service could be called from front-end
@RequestMapping("/denomination/v1")
public interface DenominationApi {

    @PostMapping("/calculate/{amount}")
    ResponseEntity<DenominationCalculatorResponse> calculateDenominations(@PathVariable
                                                    @Valid @NotNull
                                                    @CustomDecimalMin
                                                    @CustomDigits
                                                    BigDecimal amount,
                                                    @RequestBody(required = false) @Valid
                                                    DenominationCalculatorRequest denominationCalculatorRequest);
}
