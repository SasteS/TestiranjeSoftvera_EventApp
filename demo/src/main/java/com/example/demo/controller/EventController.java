package com.example.demo.controller;

import com.example.demo.dto.EventResponse;
import com.example.demo.model.Event;
import com.example.demo.service.EventService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Boolean> createEvent(@RequestBody Event event) {
        System.out.println("Received event: " + event.getName());

        try {
            if (event == null) {
                return ResponseEntity.badRequest().body(null);  // Return bad request if the event is null
            }

            // Ensure the event object is not null
            if (event.getName() == null || event.getType() == null) {
                return ResponseEntity.badRequest().body(null);  // Additional checks for required fields
            }

            // Save the event
            //EventResponse createdEvent = eventService.createEvent(event);
            Boolean response = eventService.createEvent(event);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getEvent(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(eventService.getEventById(id).toString());
    }
}
