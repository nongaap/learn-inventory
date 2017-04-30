package test.com.cjpowered.learn.inventory;

import java.time.LocalDate;
import java.util.List;

import com.cjpowered.learn.inventory.InventoryDatabase;
import com.cjpowered.learn.inventory.Item;
import com.cjpowered.learn.inventory.Season;

public class DatabaseTemplate implements InventoryDatabase {

    @Override
    public int onHand(final Item item) {
        throw new UnsupportedOperationException("NYI");
    }

    @Override
    public boolean onSale(final Item item) {
        throw new UnsupportedOperationException("NYI");
    }

    @Override
    public Season season(final LocalDate when) {
        throw new UnsupportedOperationException("NYI");
    }

    @Override
    public List<Item> stockItems() {
        throw new UnsupportedOperationException("NYI");
    }

}
