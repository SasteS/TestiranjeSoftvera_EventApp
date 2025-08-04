package com.example.demo.e2e.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ManageEventPage {

    // Event Type dropdown
    @FindBy(id = "type")
    private WebElement eventTypeDropdown;

    // Event Categories dropdown (visible if event type is selected)
    @FindBy(id = "categories")
    private WebElement eventCategoriesDropdown;

    // Event Name input field
    @FindBy(id = "name")
    private WebElement eventNameInput;

    // Event Description textarea
    @FindBy(id = "description")
    private WebElement eventDescriptionTextarea;

    // Max Participants input field
    @FindBy(id = "maxParticipants")
    private WebElement maxParticipantsInput;

    // Privacy Type dropdown
    @FindBy(id = "privacyType")
    private WebElement privacyTypeDropdown;

    // Location input field
    @FindBy(id = "location")
    private WebElement locationInput;

    // Event Date input field
    @FindBy(id = "date")
    private WebElement eventDateInput;

    // Agenda section
    @FindBy(css = "div[formArrayName='agenda']")
    private WebElement agendaSection;

    // Submit button
    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(css = "input#activityName")
    private WebElement activityNameInput;

    @FindBy(css = "input#activityLocation")
    private WebElement activityLocationInput;

    @FindBy(css = "textarea#activityDescription")
    private WebElement activityDescriptionInput;

    @FindBy(css = "input#startTime")
    private WebElement activityStartTimeInput;

    @FindBy(css = "input#endTime")
    private WebElement activityEndTimeInput;

    @FindBy(css = ".success-snackbar")
    private WebElement snackBar;

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor to initialize page elements using PageFactory
    public ManageEventPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Methods to interact with the elements
    public void selectEventType(String eventType) {
        eventTypeDropdown.sendKeys(eventType);  // select event type from dropdown
    }

    public void selectCategories(String[] categories) {
        // Assuming categories are available in a multiple select dropdown
        for (String category : categories) {
            eventCategoriesDropdown.sendKeys(category);  // select multiple categories
        }
    }

    public void enterEventName(String eventName) {
        eventNameInput.sendKeys(eventName);
    }

    public void enterEventDescription(String description) {
        eventDescriptionTextarea.sendKeys(description);
    }

    public void enterMaxParticipants(int maxParticipants) {
        maxParticipantsInput.clear();
        maxParticipantsInput.sendKeys(String.valueOf(maxParticipants));
    }

    public void selectPrivacyType(String privacyType) {
        privacyTypeDropdown.sendKeys(privacyType);
    }

    public void enterLocation(String location) {
        locationInput.clear();
        locationInput.sendKeys(location);
    }

    public void selectEventDate(String date) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='" + date + "';", eventDateInput);

//        eventDateInput.clear();
//        eventDateInput.sendKeys(date);
    }

    // Getter Methods for Verifying Values
    public String getEventName() {
        return eventNameInput.getAttribute("value");
    }
    public String getEventNameRequiredAttribute() {
        return eventNameInput.getAttribute("required");
    }

    public String getEventDescription() {
        return eventDescriptionTextarea.getAttribute("value");
    }

    public String getMaxParticipants() {
        return maxParticipantsInput.getAttribute("value");
    }

    public String getMaxParticipantsRequiredAttribute() {
        return maxParticipantsInput.getAttribute("required");
    }

    public String getPrivacyType() {
        return privacyTypeDropdown.getAttribute("value");
    }

    public String getLocation() {
        return locationInput.getAttribute("value");
    }

    public String getEventDate() {
        return eventDateInput.getAttribute("value");
    }

    // Method to add an agenda item
    public void addAgendaItemWithData(String name, String description, String startTime, String endTime, String location) {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Add Activity')]")));
        addButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activityName")));

        WebElement agendaItemNameInput =  driver.findElement(By.id("activityName")); // wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[formcontrolname='activityName']")));
        WebElement agendaItemDescriptionTextarea = driver.findElement(By.id("activityDescription"));
        WebElement agendaItemStartTimeInput = driver.findElement(By.id("startTime"));
        WebElement agendaItemEndTimeInput = driver.findElement(By.id("endTime"));
        WebElement agendaItemLocationInput = driver.findElement(By.id("activityLocation"));

        agendaItemNameInput.sendKeys(name);
        agendaItemDescriptionTextarea.sendKeys(description);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('startTime').value='" + startTime + "';");
        js.executeScript("document.getElementById('endTime').value='" + endTime + "';");
        agendaItemLocationInput.sendKeys(location);
    }

    public String getAgendaItemName() {
        return activityNameInput.getAttribute("value");
    }

    public String getAgendaItemLocation() {
        return activityLocationInput.getAttribute("value");
    }

    public String getAgendaItemDescription() {
        return activityDescriptionInput.getAttribute("value");
    }

    public String getAgendaItemStartTime() {
        return activityStartTimeInput.getAttribute("value");
    }

    public String getAgendaItemEndTime() {
        return activityEndTimeInput.getAttribute("value");
    }

    public String getSnackBar() {
        return snackBar.getAttribute("value");
    }

//
//    public void addAgendaItem(String title, String description, String startTime, String endTime, String location) {
//        // Click the "Add Activity" button
//        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Add Activity')]")));
//        addButton.click();
//
//        // Wait for the newly added agenda item to appear
//        WebElement activityNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
//
//        // Fill in the fields
//        activityNameField.sendKeys(title);
//
//        WebElement descriptionField = driver.findElement(By.cssSelector("textarea[formcontrolname='description']"));
//        descriptionField.sendKeys(description);
//
//        WebElement startTimeField = driver.findElement(By.cssSelector("input[formcontrolname='startTime']"));
//        startTimeField.sendKeys(startTime);
//
//        WebElement endTimeField = driver.findElement(By.cssSelector("input[formcontrolname='endTime']"));
//        endTimeField.sendKeys(endTime);
//
//        WebElement locationField = driver.findElement(By.cssSelector("input[formcontrolname='location']"));
//        locationField.sendKeys(location);
//    }


    // Method to submit the event form
    public void submitEvent() {
        submitButton.click();
    }
}
