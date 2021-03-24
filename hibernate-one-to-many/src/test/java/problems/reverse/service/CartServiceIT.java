package problems.reverse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-config.xml")
public class CartServiceIT {

	@Autowired
	@Qualifier("cartService")
	private CartService cartService;

	@BeforeEach
	void setUp() {
	}

	@Test
	public void search() {
		cartService.someMethod();
	}

	@Test
	@Transactional
	public void test() {
//		Cart cart1 = new Cart();
//		Cart cart2 = new Cart();
//
//		Items item1 = new Items(cart1);
//		Items item2 = new Items(cart2);
//		Set<Items> itemsSet = new HashSet<Items>();
//		itemsSet.add(item1);
//		itemsSet.add(item2);
//		cart1.setItems(itemsSet); // wrong!
	}
}
