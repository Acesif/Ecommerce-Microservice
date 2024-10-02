package com.asif.orderservice.service;

import com.asif.orderservice.dto.OrderLineItemsDto;
import com.asif.orderservice.dto.OrderRequest;
import com.asif.orderservice.model.Order;
import com.asif.orderservice.model.OrderLineItems;
import com.asif.orderservice.repository.OrderRepository;
import dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final WebClient webClient;

    private final OrderRepository orderRepository;

    public Boolean placeOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderRequest.
                        getOrderLineItemsDtoList()
                        .stream()
                        .map(this::mapFromDto)
                        .toList()
                )
                .build();

        List<String> skucodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skucodes)
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponses != null;

        Map<String, InventoryResponse> inventoryMap = Arrays.stream(inventoryResponses)
                .collect(Collectors.toMap(InventoryResponse::getSkuCode, response -> response));

        boolean allItemsAvailable = order.getOrderLineItemsList().stream()
                .allMatch(orderLineItem -> {
                    InventoryResponse inventoryResponse = inventoryMap.get(orderLineItem.getSkuCode());
                    return orderLineItem.getQuantity() <= inventoryResponse.getAvailableQuantity();
                });

        if(allItemsAvailable){
            orderRepository.save(order);
            return true;
        } else {
            log.error("Product not found in inventory");
            return false;
        }
    }
    private OrderLineItems mapFromDto(OrderLineItemsDto orderLineItemsDto){
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
