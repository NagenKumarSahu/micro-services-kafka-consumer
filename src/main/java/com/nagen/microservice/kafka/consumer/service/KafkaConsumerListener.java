package com.nagen.microservice.kafka.consumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagen.microservice.kafka.consumer.entity.Order;
import com.nagen.microservice.kafka.consumer.entity.User;
import com.nagen.microservice.kafka.consumer.repository.OrderRepository;
import com.nagen.microservice.kafka.consumer.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerListener {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Value("${kafka.topic-name}")
	private String topicName;

	@KafkaListener(topics = "order-topic", groupId = "mygroupId")
	public void processOrder(String orderMessage) {
		log.info("Order Message:{}", orderMessage);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Order order = objectMapper.readValue(orderMessage, Order.class);

			if (order != null) {
				Long userId = order.getUserId();
				Optional<User> userOptional = userRepository.findById(userId);

				if (userOptional.isPresent()) {
					User user = userOptional.get();

					if (user.getBalance() > order.getOrderAmount()) {
						user.setBalance(user.getBalance() - order.getOrderAmount());
						order.setStatus("ORDERED");
						userRepository.save(user);
					}
				}
				orderRepository.save(order);
			}
		} catch (Exception ex) {
			log.error("Error occurred::{}", ex.getMessage());
		}

	}

}
