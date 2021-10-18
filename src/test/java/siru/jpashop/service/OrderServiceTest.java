package siru.jpashop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import siru.jpashop.domain.Address;
import siru.jpashop.domain.Member;
import siru.jpashop.domain.Order;
import siru.jpashop.domain.OrderStatus;
import siru.jpashop.domain.item.Book;
import siru.jpashop.domain.item.Item;
import siru.jpashop.exception.NotEnoughStockException;
import siru.jpashop.repository.OrderRepository;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상풍주문() throws Exception {
        // given
        Member member = createMember();

        Item book = createBook("Jpa", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, findOrder.getStatus());
        assertEquals(1, findOrder.getOrderItems().size());
        assertEquals(10000 * orderCount, findOrder.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강남", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("jpa", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals(10, book.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("Jpa", 10000, 10);

        int orderCount = 11;

        // when
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));

        // then

    }
}