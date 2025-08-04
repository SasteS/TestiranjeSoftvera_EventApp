package com.example.demo.model;

import com.example.demo.enums.EventType;
import com.example.demo.enums.PrivacyType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    private PrivacyType privacyType;

    private String location;
    private LocalDate date;

    @ManyToOne
    @JsonBackReference  // Prevents infinite recursion in bidirectional relationships
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AgendaItem> agenda = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>();

    public Event() {}

    public Event(String name, String description, EventType type, Integer maxParticipants, PrivacyType privacyType,
                 String location, LocalDate date, User organizer, List<AgendaItem> agenda, List<Service> services) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.maxParticipants = maxParticipants;
        this.privacyType = privacyType;
        this.location = location;
        this.date = date;
        this.organizer = organizer;
        this.agenda = agenda;
        this.services = services;
    }

    // Getters and Setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public PrivacyType getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(PrivacyType privacyType) {
        this.privacyType = privacyType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<AgendaItem> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<AgendaItem> agenda) {
        this.agenda = agenda;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        StringBuilder agendaString = new StringBuilder("[\n");
        for (AgendaItem agendaItem : agenda) {
            agendaString.append(agendaItem.toString()).append(",\n");
        }
        agendaString.append("]");

        StringBuilder servicesString = new StringBuilder("[\n");
        for (Service service : services) {
            servicesString.append(service.toString()).append(",\n");
        }
        servicesString.append("]");

        return "Event{" +
                "\n\tid=" + id +
                ",\n\tname='" + name + '\'' +
                ",\n\tdescription='" + description + '\'' +
                ",\n\ttype=" + type +
                ",\n\tmaxParticipants=" + maxParticipants +
                ",\n\tprivacyType=" + privacyType +
                ",\n\tlocation='" + location + '\'' +
                ",\n\tdate=" + date +
                ",\n\torganizer=" + organizer.getId() +
                ",\n\tagenda=" + agendaString +
                ",\n\tservices=" + servicesString +
                '}';
    }
}
