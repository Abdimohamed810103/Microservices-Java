package com.order.repository;

import com.order.model.Dalabka;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo  extends JpaRepository<Dalabka, Long> {
}
