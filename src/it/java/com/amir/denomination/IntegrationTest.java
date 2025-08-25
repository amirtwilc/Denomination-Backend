package com.amir.denomination;

import com.amir.denomination.model.DenominationCalculatorRequest;
import com.amir.denomination.model.DenominationDifference;
import com.amir.denomination.model.DenominationQuantity;
import com.amir.denomination.model.DenominationCalculatorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testDecimalMin_whenZero() {
        BigDecimal amount = BigDecimal.ZERO;
        ResponseEntity<String> response = restTemplate.postForEntity("/denomination/v1/calculate/" + amount, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testDecimalMin_whenNegative() {
        BigDecimal amount = new BigDecimal("-1");
        ResponseEntity<String> response = restTemplate.postForEntity("/denomination/v1/calculate/" + amount, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testDigits_whenMoreThanAllowed() {
        BigDecimal amount = new BigDecimal("1.001");
        ResponseEntity<String> response = restTemplate.postForEntity("/denomination/v1/calculate/" + amount, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCalculation_whenJustAmount() throws JsonProcessingException {
        BigDecimal amount = new BigDecimal("312.24");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/denomination/v1/calculate/" + amount,
                null, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        DenominationCalculatorResponse response = mapper.readValue(responseEntity.getBody(), DenominationCalculatorResponse.class);
        assertNull(response.getDenominationDifferenceList());

        List<DenominationQuantity> resultList = response.getDenominationQuantityList();
        checkDenominationQuantityElement(resultList.get(0), "200.00", 1);
        checkDenominationQuantityElement(resultList.get(1), "100.00", 1);
        checkDenominationQuantityElement(resultList.get(2), "10.00", 1);
        checkDenominationQuantityElement(resultList.get(3), "2.00", 1);
        checkDenominationQuantityElement(resultList.get(4), "0.20", 1);
        checkDenominationQuantityElement(resultList.get(5), "0.02", 2);
        assertEquals(6, resultList.size());
    }

    @Test
    public void testCalculation_whenAlsoDifference() throws JsonProcessingException {
        BigDecimal amount = new BigDecimal("9999.88");
        DenominationCalculatorRequest request = DenominationCalculatorRequest.builder()
                .denominationQuantityList(List.of(
                        DenominationQuantity.builder()
                                .denomination("1.00")
                                .quantity(1)
                        .build(),
                        DenominationQuantity.builder()
                        .denomination("0.50")
                        .quantity(1)
                        .build()))
                .build();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/denomination/v1/calculate/" + amount,
                request, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        DenominationCalculatorResponse response = mapper.readValue(responseEntity.getBody(), DenominationCalculatorResponse.class);

        List<DenominationQuantity> resultList = response.getDenominationQuantityList();
        checkDenominationQuantityElement(resultList.get(0), "200.00", 49);
        checkDenominationQuantityElement(resultList.get(1), "100.00", 1);
        checkDenominationQuantityElement(resultList.get(2), "50.00", 1);
        checkDenominationQuantityElement(resultList.get(3), "20.00", 2);
        checkDenominationQuantityElement(resultList.get(4), "5.00", 1);
        checkDenominationQuantityElement(resultList.get(5), "2.00", 2);
        checkDenominationQuantityElement(resultList.get(6), "0.50", 1);
        checkDenominationQuantityElement(resultList.get(7), "0.20", 1);
        checkDenominationQuantityElement(resultList.get(8), "0.10", 1);
        checkDenominationQuantityElement(resultList.get(9), "0.05", 1);
        checkDenominationQuantityElement(resultList.get(10), "0.02", 1);
        checkDenominationQuantityElement(resultList.get(11), "0.01", 1);
        assertEquals(12, resultList.size());

        List<DenominationDifference> differenceList = response.getDenominationDifferenceList();
        checkDenominationDifferenceElement(differenceList.get(0), "200.00", "+49");
        checkDenominationDifferenceElement(differenceList.get(1), "100.00", "+1");
        checkDenominationDifferenceElement(differenceList.get(2), "50.00", "+1");
        checkDenominationDifferenceElement(differenceList.get(3), "20.00", "+2");
        checkDenominationDifferenceElement(differenceList.get(4), "5.00", "+1");
        checkDenominationDifferenceElement(differenceList.get(5), "2.00", "+2");
        checkDenominationDifferenceElement(differenceList.get(6), "1.00", "-1");
        checkDenominationDifferenceElement(differenceList.get(7), "0.50", "0");
        checkDenominationDifferenceElement(differenceList.get(8), "0.20", "+1");
        checkDenominationDifferenceElement(differenceList.get(9), "0.10", "+1");
        checkDenominationDifferenceElement(differenceList.get(10), "0.05", "+1");
        checkDenominationDifferenceElement(differenceList.get(11), "0.02", "+1");
        checkDenominationDifferenceElement(differenceList.get(12), "0.01", "+1");
        assertEquals(13, differenceList.size());
    }

    private void checkDenominationQuantityElement(DenominationQuantity actual, String expectedDenomination, int expectedQuantity) {
        assertEquals(expectedDenomination, actual.getDenomination());
        assertEquals(expectedQuantity, actual.getQuantity());
    }

    private void checkDenominationDifferenceElement(DenominationDifference actual, String expectedDenomination, String expectedDifference) {
        assertEquals(expectedDenomination, actual.getDenomination());
        assertEquals(expectedDifference, actual.getDifference());
    }
}
