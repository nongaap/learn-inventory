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
    			return Season.Fall;
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
    	Item item = new StockedItem(shouldHave, Season.Summer);
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
    			return Season.Fall;
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
    	Item item = new StockedItem(shouldHave, Season.Summer);
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
    			return Season.Fall;
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
    	Item item = new StockedItem(shouldHave, Season.Summer);
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
    			return Season.Fall;
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
    	Item item = new StockedItem(shouldHave, Season.Summer);
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
    			return Season.Fall;
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
    	Item item = new StockedItem(shouldHave, Season.Summer);
    	final MarketingInfo mi = new MarketingTemplate(){
    		@Override
    		public boolean onSale(Item item) {
    			return itemOnSale;
    		}
    		
    		@Override
    		public Season season(LocalDate when) {
    			return Season.Fall;
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
    	Item item = new StockedItem(shouldHave, Season.Summer);
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
    			return Season.Fall;
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
    	final Season season = Season.Summer;
    	Item item = new StockedItem(shouldHave, season);
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

}
