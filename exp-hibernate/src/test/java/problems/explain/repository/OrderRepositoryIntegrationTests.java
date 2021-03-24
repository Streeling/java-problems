package problems.explain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import problems.explain.domain.Order;
import problems.explain.domain.OrderItem;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-config.xml")
public class OrderRepositoryIntegrationTests {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @PersistenceContext
  private EntityManager entityManager;


  @BeforeEach
  void setUp() {
  }

  @Test
  @Transactional
  public void save_orderIsWithoutItems() {
    Order order = new Order();
    orderRepository.save(order);
    OrderItem orderItem = new OrderItem();

    orderItem.setOrder(order);
    orderItemRepository.save(orderItem);

    Order updatedOrder = entityManager.createQuery("from Order o join fetch o.items where o.id = :id", Order.class)
        .setParameter("id", order.getId())
        .getSingleResult();
    assertNull(updatedOrder.getItems(), "Wrong items");
  }

  @Test
  @Transactional
  public void save_noOrderFound() {
    Order order = new Order();
    orderRepository.save(order);

    assertThrows(NoResultException.class, () -> {
      entityManager.createQuery("from Order o join fetch o.items where o.id = :id", Order.class)
          .setParameter("id", order.getId())
          .getSingleResult();
    });
  }
}