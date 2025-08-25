package com.amir.denomination.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppParams {
    @NotEmpty
    private List<BigDecimal> supportedDenominations;
}
