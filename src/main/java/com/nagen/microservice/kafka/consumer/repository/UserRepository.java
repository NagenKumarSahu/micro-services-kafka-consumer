package com.nagen.microservice.kafka.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagen.microservice.kafka.consumer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
