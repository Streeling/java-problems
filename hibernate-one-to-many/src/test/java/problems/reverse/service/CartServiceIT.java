package problems.reverse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import problems.reverse.dao.CartDao;
import problems.reverse.dao.ItemDao;
import problems.reverse.model.Cart;
import problems.reverse.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-config.xml")
public class CartServiceIT {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartDao cartDao;

	@Autowired
	private ItemDao itemDao;

	@BeforeEach
	@Transactional
	void setUp() {
		itemDao.deleteAll();
		cartDao.deleteAll();
	}

	@Test
	@Transactional
	public void addToCart_assignsItemsToCart() {
		Cart cart1 = new Cart();
		cartDao.saveAndFlush(cart1);
		Item item1 = new Item();
		Item item2 = new Item();

		cartService.addToCart(cart1, item1);
		cartService.addToCart(cart1, item2);

		List<Item> savedItems = itemDao.findAll();
		assertEquals(2, savedItems.size(), "Wrong number of items in database");
		Cart updatedCart = cartDao.findById(cart1.getId());
		assertEquals(2, updatedCart.getItems().size(), "Wrong number of items in cart1");
	}

	@Test
	@Transactional
	public void addToCart_assignsItemsToCarts() {
		Cart cart1 = new Cart();
		cartDao.saveAndFlush(cart1);
		Cart cart2 = new Cart();
		cartDao.saveAndFlush(cart2);
		Item item1 = new Item();
		Item item2 = new Item();

		cartService.addToCart(cart1, item1);
		cartService.addToCart(cart2, item2);

		Cart updatedCart1 = cartDao.findById(cart1.getId());
		Cart updatedCart2 = cartDao.findById(cart2.getId());
		assertEquals(1, updatedCart1.getItems().size(), "Wrong number of items in cart1");
		assertEquals(1, updatedCart2.getItems().size(), "Wrong number of items in cart2");
		assertEquals(2, itemDao.findAll().size(), "Wrong number of items in database");
	}
}