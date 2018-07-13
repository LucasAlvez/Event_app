package com.eventsapp.repository;

import com.eventsapp.models.Event;
import com.eventsapp.models.Guest;

import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, String> {
       Iterable<Guest> findByEvent(Event event);

       Guest findByRg(String rg);
}