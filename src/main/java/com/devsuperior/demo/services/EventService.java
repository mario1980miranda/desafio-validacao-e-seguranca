package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {

	@Autowired private EventRepository repository;
	@Autowired private CityRepository cityRepository;
	
	@Transactional
	public EventDTO insert(EventDTO dto) {
		
		try {
			
			City cityEntity = cityRepository.getReferenceById(dto.getCityId());
			
			Event entity = new Event();
			
			entity.setCity(cityEntity);
			entity.setDate(dto.getDate());
			entity.setName(dto.getName());
			entity.setUrl(dto.getUrl());
			
			entity = repository.save(entity);
			
			return new EventDTO(entity);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	@Transactional(readOnly = true)
	public Page<EventDTO> findAll(Pageable pageable) {
		
		final Page<Event> result = repository.findAll(pageable);
		
		return result.map(x -> new EventDTO(x));
	}
	
}
