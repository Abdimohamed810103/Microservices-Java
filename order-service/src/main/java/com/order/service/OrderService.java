package com.order.service;

import com.order.model.Dalabka;
import com.order.model.OrderLineItems;
import com.order.modelDto.InventoryResponse;
import com.order.modelDto.OrderLineItemsDto;
import com.order.modelDto.OrderRequest;
import com.order.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private final WebClient webClient;

    public Dalabka placeOrder(OrderRequest orderRequest){
        Dalabka order = new Dalabka();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> lineItems = orderRequest.getOrderLineItemsDtos()
                .stream()
                .map(orderLineItems -> mapToOrder(orderLineItems)).collect(Collectors.toList());
        order.setOrderLineItemsList(lineItems);

        List<String> skuCode = order.getOrderLineItemsList().stream()
                .map(orderLineItems -> orderLineItems.getSkuCode()).collect(Collectors.toList());

        //check if the order is in the stock if not throw exception
        InventoryResponse[] response = webClient.get()
                .uri("http://localhost:8084/api/inventory",uriBuilder -> uriBuilder.queryParam("skucode", skuCode).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        boolean isInstock = Arrays.stream(response).allMatch(inventoryResponse -> inventoryResponse.isInStock());
        if(isInstock){
            return   orderRepo.save(order);

        }else{
            throw new IllegalArgumentException("This order is out of stock, please try again");
        }



    }

    private OrderLineItems mapToOrder(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
        return orderLineItems;

    }

    public List<Dalabka> getAllOrders() {
        return orderRepo.findAll();
    }
}
