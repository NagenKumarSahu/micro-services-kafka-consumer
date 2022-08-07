package com.nagen.microservice.kafka.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagen.microservice.kafka.consumer.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
