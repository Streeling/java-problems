package problems.explain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import problems.explain.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
  // Insert some custom methods
}