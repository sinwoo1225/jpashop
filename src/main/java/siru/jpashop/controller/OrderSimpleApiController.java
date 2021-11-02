package siru.jpashop.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import siru.jpashop.domain.Address;
import siru.jpashop.domain.Order;
import siru.jpashop.domain.OrderSearch;
import siru.jpashop.domain.OrderStatus;
import siru.jpashop.repository.OrderRepository;
import siru.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import siru.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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

    /**
     * 지연로딩으로 인한 N + 1 문제발생
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString((new OrderSearch()));
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 패치조인 적용
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * dto로 적용
     * 원하는 값만 선택해 가져옴 -> 네트웤 용량 최적화(성능 미비)
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
