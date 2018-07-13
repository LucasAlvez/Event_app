package com.eventsapp.repository;

import com.eventsapp.models.Role;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String>{

    Role findByNameRole(String nameRole);
}