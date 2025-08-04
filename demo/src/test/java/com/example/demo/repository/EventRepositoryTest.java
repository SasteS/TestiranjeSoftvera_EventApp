package com.example.demo.repository;

import com.example.demo.enums.Availability;
import com.example.demo.enums.EventType;
import com.example.demo.enums.PrivacyType;
import com.example.demo.model.AgendaItem;
import com.example.demo.model.Event;
import com.example.demo.model.Service;
import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        // Create and save a user (organizer)
        testUser = new User("John Doe", "johndoe", "johndoe@example.com", "securepassword");
        userRepository.save(testUser);

        // Create and save an event with the test user as the organizer
        List<AgendaItem> agenda = List.of(
                new AgendaItem("Keynote Speech", "Opening speech by the CEO.",
                        LocalTime.of(10, 0), LocalTime.of(11, 0), "Main Hall")
        );

        // Create services for the event
        List<Service> services = List.of(
                new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE)
        );

        // Create the Event with the mock organizer (set the organizer in the Event constructor)
        testEvent = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.now(),
                testUser,  // Set the organizer here
                agenda,
                services
        );
        eventRepository.save(testEvent);
    }

    @Test
    void testFindByOrganizer() {
        // Test the findByOrganizer method
        List<Event> events = eventRepository.findByOrganizer(testUser);
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(testEvent.getName(), events.get(0).getName());
        assertEquals(testUser.getId(), events.get(0).getOrganizer().getId());
    }

    @Test
    void testFindByOrganizer_noResults() {
        // Test when no events are found for a different organizer
        User anotherUser = new User("Jane Doe", "janedoe", "janedoe@example.com", "anotherpassword");
        userRepository.save(anotherUser);

        List<Event> events = eventRepository.findByOrganizer(anotherUser);
        assertTrue(events.isEmpty());
    }
}