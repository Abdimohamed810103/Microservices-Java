package com.inventory;

import com.inventory.repository.InventoryReposistory;
import com.inventory.model.Inventory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
	@Bean
  public CommandLineRunner loadData(InventoryReposistory inventoryReposistory){
	return args -> {
		Inventory inventory = Inventory.builder()
				.skuCode("Iphone_13")
				.quantity(10)
				.build();

		Inventory inventory2 = Inventory.builder()
				.skuCode("Iphone_13_red")
				.quantity(0)
				.build();
		inventoryReposistory.save(inventory2);
		inventoryReposistory.save(inventory);

	};

  }
}
