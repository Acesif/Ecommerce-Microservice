package com.asif.orderservice.service;

import com.asif.orderservice.config.WebClientConfig;
import com.asif.orderservice.dto.InventoryResponse;
import com.asif.orderservice.dto.OrderLineItemsDto;
import com.asif.orderservice.dto.OrderRequest;
import com.asif.orderservice.model.Order;
import com.asif.orderservice.model.OrderLineItems;
import com.asif.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final WebClient webClient;

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

        List<String> skucodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skucodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean result = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if(result){
            orderRepository.save(order);
        } else {
            log.error("Product not found in inventory");
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
