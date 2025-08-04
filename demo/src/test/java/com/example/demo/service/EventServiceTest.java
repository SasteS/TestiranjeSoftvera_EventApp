package com.example.demo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.enums.Availability;
import com.example.demo.enums.EventType;
import com.example.demo.enums.PrivacyType;
import com.example.demo.model.AgendaItem;
import com.example.demo.model.Event;
import com.example.demo.model.Service;
import com.example.demo.model.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceRepository serviceRepository;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventService(eventRepository, userRepository, serviceRepository);
    }

    @Test
    void testCreateEvent_validEvent() {
        // Mock existing user
        User organizer = new User("John Doe", "Johnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);  // Ensure ID is set
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        // Create agenda without setting Event reference
        List<AgendaItem> agenda = List.of(
                new AgendaItem("Keynote Speech", "Opening speech by the CEO.",
                        LocalTime.of(10, 0), LocalTime.of(11, 0), "Main Hall")
        );

        // Create services
        List<Service> services = List.of(
                new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE)
        );
        when(serviceRepository.findByName("TEST_SERVICE")).thenReturn(List.of());

        // Event without circular reference
        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.now(),
                organizer,
                agenda,
                services
        );

        // Mock repository behavior
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        boolean result = eventService.createEvent(event);

        // Assertions
        assertTrue(result, "The event should be created successfully.");

        // Verify interactions
        verify(eventRepository, times(3)).save(any(Event.class));
        verify(userRepository, times(1)).findById(1L);
        verify(serviceRepository, times(1)).findByName("TEST_SERVICE");
    }

    @Test
    void testCreateEvent_organizerNotFound() {
        // Mock existing user
        User organizer = new User("John Doe", "Johnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);  // Ensure ID is set
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Create agenda without setting Event reference
        List<AgendaItem> agenda = List.of(
                new AgendaItem("Keynote Speech", "Opening speech by the CEO.",
                        LocalTime.of(10, 0), LocalTime.of(11, 0), "Main Hall")
        );

        // Create services
        List<Service> services = List.of(
                new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE)
        );

        // Event without circular reference
        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.now(),
                organizer,
                agenda,
                services
        );

        assertFalse(eventService.createEvent(event), "Organizer not found.");

        // Verify interactions
        verify(userRepository, times(1)).findById(anyLong());
        verify(serviceRepository, never()).findByName(anyString());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testCreateEvent_serviceNotAvailable() {
        // Mock existing user
        User organizer = new User("John Doe", "Johnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);  // Ensure ID is set
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        // Create agenda without setting Event reference
        List<AgendaItem> agenda = List.of(
                new AgendaItem("Keynote Speech", "Opening speech by the CEO.",
                        LocalTime.of(10, 0), LocalTime.of(11, 0), "Main Hall")
        );

        // Create services with unavailable status
        List<Service> services = List.of(
                new Service("TEST_SERVICE", LocalDate.now(), Availability.UNAVAILABLE)
        );

        // Event without circular reference
        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.now(),
                organizer,
                agenda,
                services
        );

        // Call the method to create the event and assert the service is unavailable
        assertFalse(eventService.createEvent(event), "Service is not available at this time.");

        // Verify interactions with the repositories
        verify(userRepository, times(1)).findById(anyLong());
        verify(serviceRepository, never()).findByName("TEST_SERVICE");
        verify(eventRepository, never()).save(any(Event.class));
    }


    @Test
    void testCreateEvent_serviceAlreadyBooked() {
        // Mock existing user
        User organizer = new User("John Doe", "Johnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);  // Ensure ID is set
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        // Create agenda without setting Event reference
        List<AgendaItem> agenda = List.of(
                new AgendaItem("Keynote Speech", "Opening speech by the CEO.",
                        LocalTime.of(10, 0), LocalTime.of(11, 0), "Main Hall")
        );

        // Create services with unavailable status
        List<Service> services = List.of(
                new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE)
        );
        when(serviceRepository.findByName("TEST_SERVICE")).thenReturn(services);

        // Event without circular reference
        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.now(),
                organizer,
                agenda,
                services
        );

        assertFalse(eventService.createEvent(event), "Service is already booked for the selected time.");

        // Verify interactions
        verify(userRepository, times(1)).findById(anyLong());
        verify(serviceRepository, times(1)).findByName(anyString());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testCreateEvent_hasPassedBookingDeadline() {
        // Mock existing user
        User organizer = new User("John Doe", "Johnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);  // Ensure ID is set
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        // Create agenda without setting Event reference
        List<AgendaItem> agenda = List.of(
                new AgendaItem("Keynote Speech", "Opening speech by the CEO.",
                        LocalTime.of(10, 0), LocalTime.of(11, 0), "Main Hall")
        );

        // Create services with unavailable status
        List<Service> services = List.of(
                new Service("TEST_SERVICE", LocalDate.of(2024, 11, 20), Availability.AVAILABLE)
        );
        when(serviceRepository.findByName("TEST_SERVICE")).thenReturn(List.of());

        // Event without circular reference
        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.of(2024, 11, 30),
                organizer,
                agenda,
                services
        );

        assertFalse(eventService.createEvent(event), "Booking deadline has passed for the event date.");

        // Verify interactions
        verify(userRepository, times(1)).findById(anyLong());
        verify(serviceRepository, times(1)).findByName(anyString());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testCreateEvent_unexpectedError() {
        // Mock existing user
        User organizer = new User("John Doe", "Johnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);  // Ensure ID is set
        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        // Create agenda without setting Event reference
        List<AgendaItem> agenda = List.of(
                new AgendaItem("Keynote Speech", "Opening speech by the CEO.",
                        LocalTime.of(10, 0), LocalTime.of(11, 0), "Main Hall")
        );

        // Create services with unavailable status
        List<Service> services = List.of(
                new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE)
        );
        when(serviceRepository.findByName("TEST_SERVICE")).thenReturn(List.of());

        // Event without circular reference
        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.now(),
                organizer,
                agenda,
                services
        );
        when(userRepository.findById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));

        boolean result = eventService.createEvent(event);

        assertFalse(result);

        // Verify interactions
        verify(userRepository, times(1)).findById(anyLong());
        verify(serviceRepository, never()).findByName(anyString());
        verify(eventRepository, never()).save(any(Event.class));
    }

    // Test isServiceAvailable method
    @Test
    void testIsServiceAvailable() {
        Service availableService = new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE);
        Service unavailableService = new Service("TEST_SERVICE", LocalDate.now(), Availability.UNAVAILABLE);

        assertTrue(eventService.isServiceAvailable(availableService), "Service should be available");
        assertFalse(eventService.isServiceAvailable(unavailableService), "Service should not be available");
    }

    // Test isServiceAlreadyBooked method
    @Test
    void testIsServiceAlreadyBooked() {
        Service service = new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE);
        when(serviceRepository.findByName("TEST_SERVICE")).thenReturn(List.of(service));

        Service duplicateService = new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE);
        assertTrue(eventService.isServiceAlreadyBooked(duplicateService), "Service should be already booked for the selected date");

        // Test when the service is not already booked
        Service newService = new Service("TEST_SERVICE", LocalDate.now().plusDays(5), Availability.AVAILABLE);
        when(serviceRepository.findByName("TEST_SERVICE")).thenReturn(List.of(newService));
        when(serviceRepository.findByName("TEST_SERVICE")).thenReturn(List.of(service));
        assertFalse(eventService.isServiceAlreadyBooked(newService), "Service should not be already booked for the selected date");
    }

    // Test hasPassedBookingDeadline method
    @Test
    void testHasPassedBookingDeadline() {
        Service pastService = new Service("TEST_SERVICE", LocalDate.of(2023, 12, 25), Availability.AVAILABLE);
        Service futureService = new Service("TEST_SERVICE", LocalDate.now().plusDays(5), Availability.AVAILABLE);

        assertTrue(eventService.hasPassedBookingDeadline(pastService), "Booking deadline has passed for the service date");
        assertFalse(eventService.hasPassedBookingDeadline(futureService), "Booking deadline has not passed for the service date");
    }
}
