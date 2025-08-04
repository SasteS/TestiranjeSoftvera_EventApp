package com.example.demo.repository;

import com.example.demo.model.AgendaItem;
import com.example.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<AgendaItem, Long> {
}