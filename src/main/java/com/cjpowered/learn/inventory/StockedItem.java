package com.cjpowered.learn.inventory;

import java.time.LocalDate;
import java.util.Optional;

import com.cjpowered.learn.marketing.MarketingInfo;

public class StockedItem implements Item {

	private final int wantOnHand;
	
	private final int unitsPerPackage;
	
	private final boolean orderFirstDayOfMonthOnly;
	
	public StockedItem(int wantOnHand, int unitsPerPackage, boolean orderFirstDayOfMonthOnly) {
		this.wantOnHand = wantOnHand;
		this.unitsPerPackage = unitsPerPackage;
		this.orderFirstDayOfMonthOnly = orderFirstDayOfMonthOnly;
	}

	@Override
	public Order createOrder(final LocalDate when, final InventoryDatabase database, final MarketingInfo marketingInfo) {
		
		final int onHand = database.onHand(this);
		final int onOrder = database.onOrder(this);
		final boolean onSale = marketingInfo.onSale(this);
		final boolean firstDayOfMonth = when.getDayOfMonth() == 1;
		final boolean orderAllowed = orderFirstDayOfMonthOnly ? firstDayOfMonth : true;
		final int toOrder;
		if (orderAllowed){
			if (onSale) {
				double packagesToOrder = Math.ceil((double)(wantOnHand + 20 - onHand - onOrder)/(double) unitsPerPackage);
				toOrder = (wantOnHand + 20 - onHand - onOrder) > 0 ?  (int) packagesToOrder * unitsPerPackage  : 0;
			} else {
				double packagesToOrder = Math.ceil((double)(wantOnHand - onHand - onOrder)/(double) unitsPerPackage);
				toOrder = (wantOnHand - onHand - onOrder) > 0? (int) packagesToOrder * unitsPerPackage : 0;
			}
		} else {
			toOrder = 0;
		}
		return new Order(this, toOrder);
	}

}
