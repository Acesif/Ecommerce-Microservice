package com.asif.orderservice.controller;

import com.asif.orderservice.dto.OrderRequest;
import com.asif.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest){
        Boolean result = orderService.placeOrder(orderRequest);
        if(result){
            return new ResponseEntity<>("Order successfully placed", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Item not inventory are not of sufficient quantity", HttpStatus.BAD_REQUEST);
    }
}
