package com.example.demo.controller;

import com.example.demo.enums.EventType;
import com.example.demo.enums.PrivacyType;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        // Register JavaTimeModule for handling LocalDate
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Use ISO-8601 format
    }

    @Test
    void createEvent_validRequest_returnsOk() throws Exception {
        // Mock Event object
        User organizer = new User("John Doe", "Johnnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);

        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.of(2024, 12, 25),
                organizer,
                List.of(),
                List.of()
        );

        // Mock service behavior
        when(eventService.createEvent(any(Event.class))).thenReturn(true);

        // Serialize Event object to JSON
        String eventJson = objectMapper.writeValueAsString(event);

        // Perform POST request
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(content().string("true")); // Expect response body "true"
    }

    @Test
    void createEvent_nullEvent_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Empty JSON payload
                .andDo(print())
                .andExpect(status().isBadRequest()); // Expect HTTP 400
    }

    @Test
    void createEvent_missingFields_returnsBadRequest() throws Exception {
        // Event JSON with missing required fields
        String incompleteEventJson = "{\n" +
                "    \"name\": \"Tech Conference 2024\"\n" +
                "}";

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteEventJson))
                .andDo(print())
                .andExpect(status().isBadRequest()); // Expect HTTP 400
    }

    @Test
    void createEvent_serviceThrowsException_returnsInternalServerError() throws Exception {
        // Mock service to throw an exception
        when(eventService.createEvent(any(Event.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Mock Event object
        User organizer = new User("John Doe", "Johnnie Doe", "johndoe@example.com", "securepassword");
        organizer.setId(1L);

        Event event = new Event(
                "Tech Conference 2024",
                "A conference to discuss emerging technologies.",
                EventType.CONFERENCE,
                123,
                PrivacyType.OPEN,
                "Tech City Center, Room 101",
                LocalDate.of(2024, 12, 25),
                organizer,
                List.of(),
                List.of()
        );

        // Serialize Event object to JSON
        String eventJson = objectMapper.writeValueAsString(event);

        // Perform POST request
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isInternalServerError()); // Expect HTTP 500
    }
}
