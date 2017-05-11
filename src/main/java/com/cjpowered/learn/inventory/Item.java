package com.cjpowered.learn.inventory;

import java.time.LocalDate;

import com.cjpowered.learn.marketing.MarketingInfo;
import com.cjpowered.learn.marketing.Season;

public interface Item {

	Order createOrder(LocalDate when, InventoryDatabase database, MarketingInfo marketingInfo);

}
