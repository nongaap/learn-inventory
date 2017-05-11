package com.cjpowered.learn.inventory;

import java.time.LocalDate;

import com.cjpowered.learn.marketing.MarketingInfo;
import com.cjpowered.learn.marketing.Season;

public class SeasonalItem implements Item {

	private final Season season;
	
	private final int wantOnHand;
	
	public SeasonalItem(final int wantOnHand, final Season season) {
		this.season = season;
		this.wantOnHand = wantOnHand;
	}
	
	@Override
	public Order createOrder(final LocalDate when, InventoryDatabase database, MarketingInfo marketingInfo) {
		final int onHand = database.onHand(this);
		final boolean inSeason = season.equals(marketingInfo.season(when));
		final int toOrder;
		if (inSeason) {
			toOrder = (wantOnHand * 2) - onHand;
		} else {
			toOrder = wantOnHand - onHand;
		}
		return new Order(this, toOrder);
		
	}

}
