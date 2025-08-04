package com.example.demo.e2e.test;

import com.example.demo.e2e.page.ManageEventPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ManageEventPageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private ManageEventPage manageEventPage;

    @BeforeEach
    public void setUp() {
        // Initialize the WebDriver and open the browser
        driver = new ChromeDriver();  // You can use WebDriverManager to manage the driver
        driver.get("http://localhost:4200/event-management");  // Replace with your local or deployed URL

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Initialize the page object
        manageEventPage = new ManageEventPage(driver);
    }

    @AfterEach
    public void tearDown() {
        // Close the browser after each test
        driver.quit();
    }

    @Test
    public void testSelectEventType() {
        // Test selecting an event type
        manageEventPage.selectEventType("CONFERENCE");
        WebElement selectedEventType = driver.findElement(By.id("type"));
        assertEquals("CONFERENCE", selectedEventType.getAttribute("value"));
    }

    @Test
    public void testSelectCategories() {
        // Test selecting categories based on event type
        manageEventPage.selectEventType("CONFERENCE");
        String[] expectedCategories = {"Tech", "Business", "Education"};
        manageEventPage.selectCategories(expectedCategories);

        // Assuming categories are rendered and selected in a multiple dropdown
        WebElement categoriesDropdown = driver.findElement(By.id("categories"));
        assertTrue(categoriesDropdown.getText().contains("Tech"));
        assertTrue(categoriesDropdown.getText().contains("Business"));
    }

    @Test
    public void testEnterEventDetails() {
        // Test entering event details into the form
        manageEventPage.enterEventName("Tech Conference");
        manageEventPage.enterEventDescription("A conference on the latest in tech.");
        manageEventPage.enterMaxParticipants(100);
        manageEventPage.selectPrivacyType("OPEN");
        manageEventPage.enterLocation("San Francisco");
        manageEventPage.selectEventDate("2024-12-25");

        // Verify that values are set correctly
        assertEquals("Tech Conference", manageEventPage.getEventName());
        assertEquals("A conference on the latest in tech.", manageEventPage.getEventDescription());
        assertEquals("100", manageEventPage.getMaxParticipants());
        assertEquals("OPEN", manageEventPage.getPrivacyType());
        assertEquals("San Francisco", manageEventPage.getLocation());
        assertEquals("2024-12-25", manageEventPage.getEventDate());
    }

    @Test
    public void testAddAgendaItem() {
        // Add an agenda item
        manageEventPage.addAgendaItemWithData(
                "Opening Ceremony",
                "The event opens with a keynote speech.",
                "21:00", // 02:10 02:20
                "22:00",
                "Main Hall"
        );


        // Wait for the activity name input field to be populated
        String agendaItemNameInput = manageEventPage.getAgendaItemName();
        System.out.println("agenda name: " + agendaItemNameInput);
        assertEquals("Opening Ceremony", agendaItemNameInput);

        String agendaItemLocationInput = manageEventPage.getAgendaItemLocation();
        System.out.println("agenda location: " + agendaItemLocationInput);
        assertEquals("Main Hall", agendaItemLocationInput);

        String agendaItemDescriptionInput = manageEventPage.getAgendaItemDescription();
        System.out.println("agenda description: " + agendaItemDescriptionInput);
        assertEquals("The event opens with a keynote speech.", agendaItemDescriptionInput);

        String agendaItemStartTimeInput = manageEventPage.getAgendaItemStartTime();
        System.out.println("agenda start time: " + agendaItemStartTimeInput);
        assertEquals("21:00", agendaItemStartTimeInput);

        String agendaItemEndTimeInput = manageEventPage.getAgendaItemEndTime();
        System.out.println("agenda end time: " + agendaItemEndTimeInput);
        assertEquals("22:00", agendaItemEndTimeInput);
    }

    @Test
    public void testSubmitEventForm() {
        // Test submitting the event form
        manageEventPage.enterEventName("Tech Conference");
        manageEventPage.enterEventDescription("A conference on the latest in tech.");
        manageEventPage.enterMaxParticipants(100);
        manageEventPage.selectPrivacyType("OPEN");
        manageEventPage.enterLocation("San Francisco");
        manageEventPage.selectEventDate("2024-12-25");

        // Mock form submission
        manageEventPage.submitEvent();

//        System.out.println("SnackBar: " + manageEventPage.getSnackBar());
//        WebElement successMessage = driver.findElement(By.cssSelector(".success-snackbar"));
//        assertNotNull(successMessage);
//        assertTrue(successMessage.getText().contains("Event created successfully"));
    }

    @Test
    public void testFormValidation() {
        // Test form validation (empty fields should show validation errors)
        manageEventPage.submitEvent();

        // Check if required fields show validation errors
        // WebElement eventNameInput = driver.findElement(By.id("name"));
        // WebElement maxParticipantsInput = driver.findElement(By.id("maxParticipants"));
        assertTrue(manageEventPage.getEventNameRequiredAttribute().equals("true")); //eventNameInput.getAttribute("required").equals("true"));
        assertTrue(manageEventPage.getMaxParticipantsRequiredAttribute().equals("true")); // maxParticipantsInput.getAttribute("required").equals("true"));
    }
}
