package com.cjpowered.learn.inventory;

import java.time.LocalDate;

import com.cjpowered.learn.marketing.MarketingInfo;
import com.cjpowered.learn.marketing.Season;

public class SeasonalItem implements Item {

	private final Season season;
	
	private final int wantOnHand;
	
	private final int unitsPerPackage;
	
	private final boolean orderFirstDayOfMonthOnly;
	
	public SeasonalItem(final int wantOnHand, final Season season, int unitsPerPackage, final boolean orderFirstDayOfMonthOnly) {
		this.season = season;
		this.wantOnHand = wantOnHand;
		this.unitsPerPackage = unitsPerPackage;
		this.orderFirstDayOfMonthOnly = orderFirstDayOfMonthOnly;
	}
	
	@Override
	public Order createOrder(final LocalDate when, InventoryDatabase database, MarketingInfo marketingInfo) {
		final int onHand = database.onHand(this);
		final boolean inSeason = season.equals(marketingInfo.season(when));
		final boolean onSale = marketingInfo.onSale(this);
		final boolean firstDayOfMonth = when.getDayOfMonth() == 1;
		final boolean orderAllowed = orderFirstDayOfMonthOnly ? firstDayOfMonth : true;
		final int toOrder;
		final int inSeasonAndOnSaleOrder;
		if (orderAllowed){
			if(inSeason && onSale){
				double packagesToOrderInSeason = Math.ceil((double)((wantOnHand * 2) - onHand)/(double) unitsPerPackage);
				double packagesToOrderOnSale = Math.ceil((double)(wantOnHand + 20 - onHand)/(double) unitsPerPackage);
				inSeasonAndOnSaleOrder = (int) packagesToOrderInSeason > (int) packagesToOrderOnSale ? (int) packagesToOrderInSeason : (int) packagesToOrderOnSale;
				toOrder = inSeasonAndOnSaleOrder > 0 ? inSeasonAndOnSaleOrder * unitsPerPackage : 0; 
			} else if (inSeason) {
				double packagesToOrder = Math.ceil((double)((wantOnHand * 2) - onHand)/(double) unitsPerPackage);
				toOrder = ((wantOnHand * 2) - onHand) > 0 ? (int) packagesToOrder * unitsPerPackage : 0;
			} else {
				double packagesToOrder = Math.ceil((double)(wantOnHand - onHand)/(double) unitsPerPackage);
				toOrder = (wantOnHand - onHand) > 0 ? (int) packagesToOrder * unitsPerPackage : 0;
			}
		}  else {
			toOrder = 0;
		}
		return new Order(this, toOrder);
		
	}

}
