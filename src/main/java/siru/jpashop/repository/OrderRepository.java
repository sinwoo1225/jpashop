package siru.jpashop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import siru.jpashop.domain.Order;
import siru.jpashop.domain.OrderSearch;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        return em.createQuery("select o from Order o join o.member m", Order.class)
                .setMaxResults(1000)
                .getResultList();
    }
}
