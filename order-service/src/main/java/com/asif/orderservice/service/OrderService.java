package com.asif.orderservice.service;

import com.asif.orderservice.dto.OrderLineItemsDto;
import com.asif.orderservice.dto.OrderRequest;
import com.asif.orderservice.model.Order;
import com.asif.orderservice.model.OrderLineItems;
import com.asif.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderRequest.
                        getOrderLineItemsDtoList()
                        .stream()
                        .map(this::mapFromDto)
                        .toList()
                )
                .build();
        orderRepository.save(order);
    }
    private OrderLineItems mapFromDto(OrderLineItemsDto orderLineItemsDto){
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
