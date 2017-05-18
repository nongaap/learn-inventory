package test.com.cjpowered.learn.inventory;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.cjpowered.learn.inventory.InventoryDatabase;
import com.cjpowered.learn.inventory.InventoryManager;
import com.cjpowered.learn.inventory.Item;
import com.cjpowered.learn.inventory.Order;
import com.cjpowered.learn.inventory.SeasonalItem;
import com.cjpowered.learn.inventory.StockedItem;
import com.cjpowered.learn.inventory.ace.AceInventoryManager;
import com.cjpowered.learn.marketing.MarketingInfo;
import com.cjpowered.learn.marketing.Season;

/*
 * We need to keep items in stock to prevent back orders. See the README.md
 * for the requirements.
 *
 */

public class InventoryTest {

    @Test
    public void whenNoStockItemsDoNotOrder() {
        // given
    	final InventoryDatabase db = new DatabaseTemplate(){
    		@Override
    		public List<Item> stockItems() {
    			return Collections.emptyList();
    		}
    	};
    	final LocalDate today = LocalDate.now();
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);

        // when
        final List<Order> actual = im.getOrders(today);

        // then
        assertTrue(actual.isEmpty());

    }
    
    @Test
    public void orderEnoughStock(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final InventoryDatabase db = new DatabaseTemplate(){
    		@Override
    		public int onHand(Item item) {
    			// TODO Auto-generated method stub
    			return onHand;
    		}
    		@Override
    		public List<Item> stockItems() {
    			return Collections.singletonList(item);
    		}
    	};
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	assertEquals(1, actual.size());
    	assertEquals(item, actual.get(0).item);
    	assertEquals(shouldHave - onHand, actual.get(0).quantity);
    	
    }
    
    @Test
    public void orderEnoughStockRefactored(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	Order expectedOrder = new Order(item, shouldHave - onHand);
    	Set<Order> expected = Collections.singleton(expectedOrder);
    	assertEquals(expected, new HashSet<>(actual));
    	
    }
    
    @Test
    public void tooMuchStock(){
    	// given
    	final int onHand = 20;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final InventoryDatabase db = new DatabaseTemplate(){
    		@Override
    		public int onHand(Item item) {
    			// TODO Auto-generated method stub
    			return onHand;
    		}
    		@Override
    		public List<Item> stockItems() {
    			return Collections.singletonList(item);
    		}
    	};
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	assertEquals(0, actual.size());
    }
    
    @Test
    public void enoughStock(){
    	// given
    	final int onHand = 16;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final InventoryDatabase db = new DatabaseTemplate(){
    		@Override
    		public int onHand(Item item) {
    			// TODO Auto-generated method stub
    			return onHand;
    		}
    		@Override
    		public List<Item> stockItems() {
    			return Collections.singletonList(item);
    		}
    	};
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	assertEquals(0, actual.size());
    }
    
    @Test
    public void itemOnSale() {
        // given
    	final boolean itemOnSale = true;
    	final int shouldHave = 30;
    	final int unitsPerPackage = 1;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final MarketingInfo mi = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return itemOnSale;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};

        // when
        final boolean actual = mi.onSale(item);

        // then
        assertTrue(actual);

    }
    
    @Test
    public void orderOnSaleItem(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return true;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	Order expectedOrder = new Order(item, 20 + shouldHave - onHand);
    	Set<Order> expected = Collections.singleton(expectedOrder);
    	assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void orderSeasonalItemInSeason(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final Season season = Season.Summer;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new SeasonalItem(shouldHave, season, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return season;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	Order expectedOrder = new Order(item, (2 * shouldHave) - onHand);
    	Set<Order> expected = Collections.singleton(expectedOrder);
    	assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void orderSeasonalItemInSeasonAndOnSale(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final Season season = Season.Summer;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new SeasonalItem(shouldHave, season, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return true;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return season;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	Order expectedOrder = new Order(item, 20 + shouldHave - onHand);
    	Set<Order> expected = Collections.singleton(expectedOrder);
    	assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void dontOrderSeasonalItemInSeasonAndOnSale(){
    	// given
    	final int onHand = 50;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final Season season = Season.Summer;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new SeasonalItem(shouldHave, season, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return true;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return season;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	assertEquals(0, actual.size());
    }
    
    @Test
    public void onlyOrderFirstDayOfMonth(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final boolean orderFirstDayOfMonthOnly = true;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.of(2017,5,2);
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	assertEquals(0, actual.size());
    	
    }
    
    @Test
    public void onlyOrderFirstDayOfMonthSeasonal(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 1;
    	final Season season = Season.Summer;
    	final boolean orderFirstDayOfMonthOnly = true;
    	Item item = new SeasonalItem(shouldHave, season, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return season;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.of(2017,5,2);
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	assertEquals(0, actual.size());
    }
    
    @Test
    public void orderEnoughStockWhenPackageContainsMultipleUnits(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 4;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	Order expectedOrder = new Order(item, 8);
    	Set<Order> expected = Collections.singleton(expectedOrder);
    	assertEquals(expected, new HashSet<>(actual));
    	
    }
    
    @Test
    public void orderEnoughStockWhenPackageContainsMultipleUnitsAndItemOnSale(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 4;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new StockedItem(shouldHave, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return true;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Spring;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	Order expectedOrder = new Order(item, 28);
    	Set<Order> expected = Collections.singleton(expectedOrder);
    	assertEquals(expected, new HashSet<>(actual));
    	
    }
    
    @Test
    public void orderEnoughSeasonalStockWhenPackageContainsMultipleUnits(){
    	// given
    	final int onHand = 10;
    	final int shouldHave = 16;
    	final int unitsPerPackage = 4;
    	final Season season = Season.Summer;
    	final boolean orderFirstDayOfMonthOnly = false;
    	Item item = new SeasonalItem(shouldHave, season, unitsPerPackage, orderFirstDayOfMonthOnly);
    	final HashMap<Item, Integer> store = new HashMap<>();
    	store.put(item,  onHand);
    	final InventoryDatabase db = new FakeDatabase(store);
    	final MarketingInfo marketingInfo = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return false;
    		}
    		@Override
    		public Season season(LocalDate when) {
    			return season;
    		}
    	};
    	final InventoryManager im = new AceInventoryManager(db, marketingInfo);
    	final LocalDate today = LocalDate.now();
    	
    	// when
    	final List<Order> actual = im.getOrders(today);
    	
    	// then
    	Order expectedOrder = new Order(item, 24);
    	Set<Order> expected = Collections.singleton(expectedOrder);
    	assertEquals(expected, new HashSet<>(actual));
    	
    }
    
    // not on sale and not in season, multiple packages
    //on sale and in seasonal, multiple packages

}
