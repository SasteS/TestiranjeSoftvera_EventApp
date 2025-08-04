package com.example.demo.repository;

import com.example.demo.enums.Availability;
import com.example.demo.model.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ServiceRepositoryTest {

    @Autowired
    private ServiceRepository serviceRepository;

    private Service testService;

    @BeforeEach
    void setUp() {
        // Set up a sample service
        testService = new Service("TEST_SERVICE", LocalDate.now(), Availability.AVAILABLE);
        serviceRepository.save(testService);
    }

    @Test
    void testFindByName() {
        // Test the findByName method
        List<Service> services = serviceRepository.findByName("TEST_SERVICE");
        assertNotNull(services);
        assertEquals(1, services.size());
        assertEquals("TEST_SERVICE", services.get(0).getName());
    }

    @Test
    void testFindByName_noResults() {
        // Test when no services are found
        List<Service> services = serviceRepository.findByName("Nonexistent Service");
        assertTrue(services.isEmpty());
    }
}