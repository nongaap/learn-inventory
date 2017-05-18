package test.com.cjpowered.learn.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cjpowered.learn.inventory.InventoryDatabase;
import com.cjpowered.learn.inventory.Item;

public class FakeDatabase implements InventoryDatabase {

	private final Map<Item, Integer> dataStore;
	private final Map<Item, Integer> onOrderStore;
	
	public FakeDatabase(Map<Item, Integer> dataStore, Map<Item, Integer> onOrderStore) {
		this.dataStore = dataStore;
		this.onOrderStore = onOrderStore;
	}
	
	public FakeDatabase(Map<Item, Integer> dataStore) {
		this(dataStore, Collections.emptyMap());
	}
	
	@Override
	public int onHand(Item item) {
		return dataStore.get(item);
	}

	@Override
	public List<Item> stockItems() {
		final Set<Item> keys = dataStore.keySet();
		return new ArrayList<>(keys);
	}
	
	@Override
	public int onOrder(Item item) {
		if(onOrderStore.containsKey(item)) {
			return onOrderStore.get(item);
		} else {
			return 0;
		}
		
	}

}
