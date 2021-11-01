package siru.jpashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import siru.jpashop.domain.Order;
import siru.jpashop.domain.OrderSearch;
import siru.jpashop.repository.OrderRepository;

import java.util.List;

/**
 * XToOne
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * 엔티티를 응답객체로 반환했을때 문제점
     * 1. 양방향 연관관계로 인한 stackoverflow
     * 2. jackson lib가 hibernate proxy를 parsing 할 수 없음 -> hibernete5 lib로 해결가능
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }
}
