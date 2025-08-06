package com.example.demo.service;

import com.example.demo.dto.EventResponse;
import com.example.demo.enums.Availability;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository, ServiceRepository serviceRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.serviceRepository = serviceRepository;
    }

    public Boolean createEvent(Event event) {
//        System.out.println("Event to be created: " + event.toString());
        System.out.println("Event to be created: " + event.getName());// + ", By User: " + event.getOrganizer().getId());

        try {
            // Validate organizer existence
            User organizer = userRepository.findById(event.getOrganizer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

            event.setOrganizer(organizer);

            // Validate services availability
            for (var service : event.getServices()) {
                if (!isServiceAvailable(service)) {
                    throw new IllegalArgumentException("Service " + service.getName() + " is not available at this time.");
                }
                if (isServiceAlreadyBooked(service)) {
                    throw new IllegalArgumentException("Service " + service.getName() + " is already booked for the selected time.");
                }
                if (hasPassedBookingDeadline(service)) {
                    throw new IllegalArgumentException("Booking deadline has passed for the event date.");
                }
            }

            // Save the event with its organizer
            Event savedEvent = eventRepository.save(event);

            // Link agenda items to the saved event
            if (savedEvent.getAgenda() != null) {
                savedEvent.getAgenda().forEach(agenda -> agenda.setEvent(savedEvent));
                eventRepository.save(savedEvent);
            }

            // Link service items to the saved event
            if (savedEvent.getServices() != null) {
                savedEvent.getServices().forEach(service -> service.setEvent(savedEvent));
                eventRepository.save(savedEvent);
            }

            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error while creating the event: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public List<Event> getEventsByOrganizer(User organizer) {
        return eventRepository.findByOrganizer(organizer);
    }

    public Event getEventById(Long id) throws ChangeSetPersister.NotFoundException {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    public EventResponse mapToEventResponse(Event event) {
        EventResponse eventResponse = new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getType(),
                event.getMaxParticipants(),
                event.getPrivacyType(),
                event.getLocation(),
                event.getDate(),
                event.getOrganizer(),
                event.getAgenda(),
                event.getServices()
        );

        return eventResponse;
    }

    protected Boolean isServiceAvailable(com.example.demo.model.Service service) {
        return service.getAvailability().equals(Availability.AVAILABLE);
    }

    protected Boolean isServiceAlreadyBooked(com.example.demo.model.Service service) {
        List<com.example.demo.model.Service> services = serviceRepository.findByName(service.getName());

        for (com.example.demo.model.Service inspectedService : services) {
            if (service.getDate().equals(inspectedService.getDate()))
                return true;
        }
        return false;
    }

    protected Boolean hasPassedBookingDeadline(com.example.demo.model.Service service) {
        return service.getDate().isBefore(LocalDate.now());
    }
}
