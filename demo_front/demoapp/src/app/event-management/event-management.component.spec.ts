// import { ComponentFixture, TestBed } from '@angular/core/testing';

// import { EventManagementComponent } from './event-management.component';

// describe('EventManagementComponent', () => {
//   let component: EventManagementComponent;
//   let fixture: ComponentFixture<EventManagementComponent>;

//   beforeEach(async () => {
//     await TestBed.configureTestingModule({
//       declarations: [EventManagementComponent]
//     })
//     .compileComponents();

//     fixture = TestBed.createComponent(EventManagementComponent);
//     component = fixture.componentInstance;
//     fixture.detectChanges();
//   });

//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });
// });

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventManagementComponent } from './event-management.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { EventManagementService } from '../service/event/event.service';

describe('EventManagementComponent', () => {
  let component: EventManagementComponent;
  let fixture: ComponentFixture<EventManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EventManagementComponent],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        MatSnackBarModule,
        HttpClientTestingModule  // mocks HTTP requests in EventManagementService
      ],
      providers: [EventManagementService]
    }).compileComponents();

    fixture = TestBed.createComponent(EventManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
