package problems.reverse.service;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import problem.reverse.model.Cart;
import problem.reverse.model.Items;

import java.util.HashSet;
import java.util.Set;

public class CartServiceIT {

	@Test
	@Transactional
	public void test() {
		Cart cart1 = new Cart();
		Cart cart2 = new Cart();

		Items item1 = new Items(cart1);
		Items item2 = new Items(cart2);
		Set<Items> itemsSet = new HashSet<Items>();
		itemsSet.add(item1);
		itemsSet.add(item2);
		cart1.setItems(itemsSet); // wrong!
	}
}
