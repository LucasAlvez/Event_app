package com.eventsapp.repository;

import com.eventsapp.models.Event;

import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {
   Event findById(long id);
}