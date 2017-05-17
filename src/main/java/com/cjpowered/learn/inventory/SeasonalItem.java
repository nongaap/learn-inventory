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
		final boolean onSale = marketingInfo.onSale(this);
		final int toOrder;
		final int inSeasonAndOnSaleOrder;
		if(inSeason && onSale){
			inSeasonAndOnSaleOrder = ((wantOnHand * 2) - onHand) > (20 + wantOnHand - onHand) ? ((wantOnHand * 2) - onHand) : (20 + wantOnHand - onHand);
			toOrder = inSeasonAndOnSaleOrder > 0 ? inSeasonAndOnSaleOrder : 0; 
		} else if (inSeason) {
			toOrder = ((wantOnHand * 2) - onHand) > 0 ? (wantOnHand * 2) - onHand : 0;
		} else {
			toOrder = (wantOnHand - onHand) > 0 ? wantOnHand - onHand : 0;
		}
		return new Order(this, toOrder);
		
	}

}
