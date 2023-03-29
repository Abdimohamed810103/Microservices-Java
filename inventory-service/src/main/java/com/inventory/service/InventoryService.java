package com.inventory.service;

import com.inventory.dto.InventoryResponse;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryReposistory;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class InventoryService {

    private  final InventoryReposistory inventoryReposistory;
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        List<InventoryResponse> inventoryResponseList = inventoryReposistory.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> mapToDto(inventory)).collect(Collectors.toList());
        return inventoryResponseList;
    }

    private InventoryResponse mapToDto(Inventory inventory) {
        InventoryResponse inventoryResponse = InventoryResponse
                .builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
        return inventoryResponse;
    }
}
