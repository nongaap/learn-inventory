package com.cjpowered.learn.inventory;

import java.time.LocalDate;
import java.util.List;

public interface InventoryDatabase {

    /**
     * Fetch number on-hand.
     *
     * @param item
     *            item to query
     *
     * @return fetched value
     */
    int onHand(Item item);

    /**
     * Fetch on-sale status.
     *
     * @param item
     *            item to query
     *
     * @return fetched value
     */
    boolean onSale(Item item);

    /**
     * Fetch the season
     *
     * @param when
     *            date to query
     *
     * @return fetched value
     */
    Season season(LocalDate when);

    /**
     * Fetch list of all stocked items.
     *
     * @return fetched value
     */
    List<Item> stockItems();

}
