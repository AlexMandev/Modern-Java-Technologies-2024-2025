package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.Game;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.DLC;

public class GameStore implements StoreAPI {
    private static final int promoCodesCount = 2;
    private static final String[] promoCodes = {"VAN40", "100YO"};
    private static BigDecimal[] promoCodesValues = {new BigDecimal("0.60"), BigDecimal.ZERO };
    private boolean[] usedPromoCodes = new boolean[promoCodesCount];
    private StoreItem[] availableItems;

    private int countMatchingItems(ItemFilter[] filters) {
        int count = 0;

        for(var item : availableItems) {
            boolean matches = true;

            for(var filter : filters) {
                if(!filter.matches(item)) {
                    matches = false;
                    break;
                }
            }

            if(matches)
                count++;
        }
        return count;
    }

    private int findPromoCodeIndex(String promoCode) {
        if(promoCode == null)
            return -1;

        int codeIndex = -1;
        for(int i = 0; i < promoCodes.length; i++) {
            if(promoCodes[i].equals(promoCode)) {
                codeIndex = i;
                break;
            }
        }
        return codeIndex;
    }

    public GameStore(StoreItem[] availableItems) {
        this.availableItems = availableItems;
    }

    public StoreItem[] getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(StoreItem[] availableItems) {
        this.availableItems = availableItems;
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] filters) {
        if(filters == null || filters.length == 0)
            return getAvailableItems();


        // counting to calculate the array size
        int count = countMatchingItems(filters);


        if(count == 0)
            return new StoreItem[0];

        StoreItem[] matchedItems = new StoreItem[count];
        int index = 0;
        for(var item : availableItems) {
            boolean matches = true;

            for(var filter : filters) {
                if(!filter.matches(item)) {
                    matches = false;
                    break;
                }
            }

            if(matches) {
                matchedItems[index++] = item;
                if(index == count)
                    return matchedItems;
            }

        }

        return matchedItems;
    }

    @Override
    public void applyDiscount(String promoCode)
    {
        int codeIndex = findPromoCodeIndex(promoCode);

        if(codeIndex == -1) // promo code not found
            return;

        if(usedPromoCodes[codeIndex]) // checking if promo code is already used
            return;


        for(var item : availableItems) { // applying promo code value
            BigDecimal originalPrice = item.getPrice();
            item.setPrice(originalPrice.multiply(promoCodesValues[codeIndex]).setScale(2));
            usedPromoCodes[codeIndex] = true;
        }
    }

    @Override
    public boolean rateItem(StoreItem item, double rating) {
        if(rating >= 1.0 && rating <= 5.0) {
            item.rate(rating);
            return true;
        }
        return false;
    }
}
