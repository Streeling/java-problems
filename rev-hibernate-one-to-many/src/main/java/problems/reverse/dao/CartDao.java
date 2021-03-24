package problems.reverse.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import problems.reverse.model.Cart;

public class CartDao {
  @PersistenceContext
  private EntityManager entityManager;

  public void save(Cart cart) {
    entityManager.persist(cart);
  }

  public void saveAndFlush(Cart cart) {
    entityManager.persist(cart);
    entityManager.flush();
  }

  public Cart findById(long id) {
    return entityManager.createQuery("from Cart c left join fetch c.items i where c.id = :id", Cart.class).setParameter("id", id).getSingleResult();
  }

  public void deleteAll() {
    entityManager.createQuery("delete from Cart").executeUpdate();
  }

  public List<Cart> findAll() {
    return entityManager.createQuery("from Cart", Cart.class).getResultList();
  }

  public void refresh(Cart cart) {
    entityManager.refresh(cart);
  }
}
