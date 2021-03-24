package problems.reverse.service;

import org.springframework.transaction.annotation.Transactional;

import problems.reverse.dao.CartDao;
import problems.reverse.dao.ItemDao;
import problems.reverse.model.Cart;
import problems.reverse.model.Item;

public class CartService {
	private CartDao cartDao;
	private ItemDao itemDao;

	public CartService(CartDao cartDao, ItemDao itemDao) {
		this.cartDao = cartDao;
		this.itemDao = itemDao;
	}

	@Transactional
	public void addToCart(Cart cart, Item item) {
		item.setCart(cart);
		itemDao.save(item);
		cartDao.refresh(cart);
	}
}