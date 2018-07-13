package com.eventsapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventsapp.models.Users;

public interface UserRepository extends CrudRepository<Users, String>{

	Users findByLogin(String login);
}
