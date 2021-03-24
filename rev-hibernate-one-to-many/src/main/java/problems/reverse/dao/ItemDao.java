package problems.reverse.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import problems.reverse.model.Item;

public class ItemDao {
  @PersistenceContext
  private EntityManager entityManager;

  public void save(Item item) {
    entityManager.persist(item);
    entityManager.flush();
  }

  public void saveAndFlush(Item item) {
    entityManager.persist(item);
    entityManager.flush();
  }

  public void deleteAll() {
    entityManager.createQuery("delete from Item").executeUpdate();
  }

  public List<Item> findAll() {
    return entityManager.createQuery("from Item", Item.class).getResultList();
  }
}