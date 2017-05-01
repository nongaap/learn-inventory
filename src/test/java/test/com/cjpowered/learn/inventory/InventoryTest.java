package test.com.cjpowered.learn.inventory;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.cjpowered.learn.inventory.InventoryDatabase;
import com.cjpowered.learn.inventory.InventoryManager;
import com.cjpowered.learn.inventory.Item;
import com.cjpowered.learn.inventory.Order;
import com.cjpowered.learn.inventory.ace.AceInventoryManager;

/*
 * We need to keep items in stock to prevent back orders.
 *
 * 1. For each item we stock, order enough to bring the quantity on hand to its specified inventory level
 * 2. When an item goes on sale, keep an additional 20 items on hand
 * 3. For seasonal items, keep double the normal inventory on hand during the high-demand period
 * 4. When the required inventory level is above normal, use the highest calculated level
 * 5. some items can only be ordered on the first day of the month
 * 6. some items can only be ordered in packages containing multiple items
 */

public class InventoryTest {

    @Test
    public void whenNoStockItemsDoNotOrder() {
        // given
        @SuppressWarnings("unused")
        final InventoryDatabase fakeDb = new DatabaseTemplate() {
            @Override
            public List<Item> stockItems() {
                return Collections.emptyList();
            }
        };
        final InventoryManager im = new AceInventoryManager();

        // when
        final LocalDate today = LocalDate.now();
        final List<Order> actual = im.getOrders(today);

        // then
        assertTrue(actual.isEmpty());

    }

}
