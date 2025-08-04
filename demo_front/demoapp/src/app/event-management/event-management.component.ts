import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EventManagementService } from '../service/event/event.service';

@Component({
  selector: 'app-event-management',
  templateUrl: './event-management.component.html',
  styleUrls: ['./event-management.component.css']
})
export class EventManagementComponent {
  eventForm: FormGroup;
  selectedEventType: string = '';  // Tracks the selected event type
  eventCategories: { [key: string]: string[] } = {
    'CONFERENCE': ['Tech', 'Business', 'Education'],
    'WEDDING': ['Ceremony', 'Reception', 'Photography']
  };

  constructor(
    private fb: FormBuilder, 
    private snackBar: MatSnackBar,
    private eventService: EventManagementService  // Inject the service
  ) {
    this.eventForm = this.fb.group({
      type: ['', Validators.required],  // Changed from eventType to type
      services: [''],
      name: ['', Validators.required],
      description: ['', Validators.required],
      maxParticipants: [0, [Validators.required, Validators.min(1)]],
      privacyType: ['OPEN'],
      location: [''],
      date: ['', Validators.required],
      agenda: this.fb.array([]),
    });
  }

  get agenda(): FormArray {
    return this.eventForm.get('agenda') as FormArray;
  }

  addAgendaItem(): void {
    const agendaItem = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      location: ['', Validators.required],
    });
    this.agenda.push(agendaItem);
    console.log(this.agenda.value);
  }

  removeAgendaItem(index: number): void {
    this.agenda.removeAt(index);
  }

  // Handles the change in event type selection
  onEventTypeChange(): void {
    this.selectedEventType = this.eventForm.get('type')?.value;  // Changed from eventType to type
    this.eventForm.get('category')?.reset(); // Reset category when event type changes
  }

  // Get categories for the selected event type
  getCategoriesForEventType(eventType: string): string[] {
    return this.eventCategories[eventType] || [];
  }

  openSnackBar(message: string, action: string, isError: boolean = false) {
    this.snackBar.open(message, action, {
      duration: 3000,
      verticalPosition: 'top',
      panelClass: isError ? ['error-snackbar'] : ['success-snackbar'], // Apply styles based on success or error
    });
  }

  onSubmit(): void {
    if (this.eventForm.valid) {
      const eventData = { ...this.eventForm.value };
      
      // Check if categories are not null or empty
      if (eventData.services != null && eventData.services.length > 0) {
        // Map the categories to the desired structure
        eventData.services = eventData.services.map((service: string) => ({
          name: service,
          date: this.eventForm.get('date')?.value,
          availability: 'AVAILABLE'
        }));
        
        console.log('Mapped Categories:', eventData.services); // Check the mapped categories
      }

      // Ensure agenda is an empty array if no items are added
      if (!eventData.agenda) {
        eventData.agenda = []; // Default to an empty array
      }
  
      eventData.organizer = {
        id: 1,  // replace with logged-in user's ID
        name: "John Doe",  // replace with logged-in user's name
        username: "johndoe",  // replace with logged-in user's username
        email: "johndoe@example.com",  // replace with logged-in user's email
        password: "securepassword"
      };
      
      console.log("Sending Event Data:", eventData);
  
      // Send event data to backend via the service
      this.eventService.createEvent(eventData).subscribe(
        (response) => {
          console.log('Event Created:', response);  // Log the raw response here
          if (response === false)
            this.openSnackBar('Error creating event', 'Close');
          else
            this.openSnackBar('Event created successfully!', 'Close');
        },
        (error) => {
          console.error('Error creating event:', error);
          this.openSnackBar('Error creating event', 'Close', true);
        }
      );
    } else {
      this.openSnackBar('Error creating event', 'Close', true);
    }
  }
  
}
