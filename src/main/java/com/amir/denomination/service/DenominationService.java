package com.amir.denomination.service;

import com.amir.denomination.model.DenominationDifference;
import com.amir.denomination.model.DenominationQuantity;
import com.amir.denomination.param.AppParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.amir.denomination.util.MathUtil.getDividingTimes;

@Slf4j
@Service
@RequiredArgsConstructor
public class DenominationService {

    private final int DENOMINATION_SCALE = 2;

    private final AppParams appParams;

    /**
     * Calculates how many of each denomination makes up for given amount.
     * The largest denomination should always be taken first.
     * For example, the amount 200.00 should always provide one note of 200.00, rather than 2 notes of 100.00
     * @param amount the amount for calculation
     * @return list of all denominations that make up the given amount, along with its quantities.
     */
    public List<DenominationQuantity> calculateDenominationsQuantity(BigDecimal amount) {
        List<BigDecimal> supportedDenominations = appParams.getSupportedDenominations();

        List<DenominationQuantity> result = new ArrayList<>();
        for (BigDecimal currentDenomination : supportedDenominations) {
            int quantity = getDividingTimes(amount, currentDenomination);
            if (quantity > 0) {
                result.add(DenominationQuantity.builder()
                        .denomination(currentDenomination.setScale(DENOMINATION_SCALE).toPlainString()) //make sure the string would appear in the correct scale
                        .quantity(quantity)
                        .build());
                //reduce the extracted denomination from amount, so will not be used again in next loops
                //for example, amount was 500.00 and denomination 200.00 was extracted with quantity 2,
                //then amount = 300.00 - 200.00 * 2 = 100.00
                amount = (amount.subtract(currentDenomination.multiply(BigDecimal.valueOf(quantity))));
            }
        }
        return result;
    }

    /**
     * Calculates the difference in denominations between two lists.
     * Showcases how the denominations have changed from the previous (old) list
     * @param newList the new list used for the difference
     * @param oldList the old list used for the difference
     * @return list of all denominations that make up each list, while displaying the change between the two lists
     */
    public List<DenominationDifference> getDenominationDifference(List<DenominationQuantity> newList,
                                                                  List<DenominationQuantity> oldList) {

        List<BigDecimal> supportedDenominations = appParams.getSupportedDenominations();

        List<DenominationDifference> result = new ArrayList<>();
        supportedDenominations.forEach(currentDenomination -> {
            //check if currentDenomination is used in either list
            Optional<DenominationQuantity> newDenominationQuantityOptional = newList.stream().filter(x ->
                    new BigDecimal(x.getDenomination()).equals(currentDenomination.setScale(DENOMINATION_SCALE))).findAny();
            Optional<DenominationQuantity> oldDenominationQuantityOptional = oldList.stream().filter(x ->
                    new BigDecimal(x.getDenomination()).equals(currentDenomination.setScale(DENOMINATION_SCALE))).findAny();

        if (newDenominationQuantityOptional.isPresent() || oldDenominationQuantityOptional.isPresent()) {
            int newDenominationQuantity = getQuantityValue(newDenominationQuantityOptional);
            int oldDenominationQuantity = getQuantityValue(oldDenominationQuantityOptional);

            int difference = newDenominationQuantity - oldDenominationQuantity;

            result.add(DenominationDifference.builder()
                            .denomination(currentDenomination.setScale(DENOMINATION_SCALE).toPlainString())
                            .difference(difference > 0 ? "+" + difference : Integer.toString(difference))
                    .build());
        }
    });

        return result;
    }

    /**
     * This method assumes that an Optional could be empty, but a quantity value is still required for calculation.
     * Extracts the quantity if present, and returning 0 if not
     * @param denominationQuantityOptional the optional object that contains the quantity value
     * @return quantity value of given optional object
     */
    private int getQuantityValue(Optional<DenominationQuantity> denominationQuantityOptional) {
        return denominationQuantityOptional.map(DenominationQuantity::getQuantity).orElse(0);
    }
}
