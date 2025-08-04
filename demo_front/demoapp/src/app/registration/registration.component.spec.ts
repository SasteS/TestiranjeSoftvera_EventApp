import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { fakeAsync, tick, flush } from '@angular/core/testing';

import { RegistrationComponent } from './registration.component';
import { UserService } from '../service/user/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let mockUserService: jasmine.SpyObj<UserService>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    mockUserService = jasmine.createSpyObj('UserService', ['createUser']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [RegistrationComponent],
      imports: [ReactiveFormsModule, FormsModule, MatSnackBarModule, BrowserAnimationsModule],
      providers: [
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form correctly', () => {
    expect(component.registrationForm).toBeTruthy();
    expect(component.registrationForm.controls['email']).toBeTruthy();
    expect(component.registrationForm.controls['password']).toBeTruthy();
    expect(component.registrationForm.controls['confirmPassword']).toBeTruthy();
  });

  it('should validate email field correctly', () => {
    const emailControl = component.registrationForm.controls['email'];
    emailControl.setValue('invalid-email');
    expect(emailControl.valid).toBeFalse();

    emailControl.setValue('valid@example.com');
    expect(emailControl.valid).toBeTrue();
  });

  it('should validate password strength', () => {
    const passwordControl = component.registrationForm.controls['password'];
    passwordControl.setValue('weakpass');
    expect(passwordControl.errors?.['weakPassword']).toBeTrue();

    passwordControl.setValue('StrongPass1!');
    expect(passwordControl.valid).toBeTrue();
  });

  it('should validate password match', () => {
    const passwordControl = component.registrationForm.controls['password'];
    const confirmPasswordControl = component.registrationForm.controls['confirmPassword'];

    passwordControl.setValue('Test123!');
    confirmPasswordControl.setValue('Different123!');
    expect(component.registrationForm.errors?.['mismatch']).toBeTruthy();

    confirmPasswordControl.setValue('Test123!');
    expect(component.registrationForm.errors).toBeNull();
  });

  it('should call createUser on valid form submission', fakeAsync(() => {
    mockUserService.createUser.and.returnValue(of({ message: 'User created successfully' }));

    component.onUserTypeChange({ target: { value: 'OD' } } as any);

    component.registrationForm.patchValue({
      userType: 'OD',
      email: 'test@example.com',
      password: 'StrongPass1!',
      confirmPassword: 'StrongPass1!',
      firstName: 'John',
      lastName: 'Doe',
      phone: '1234567890',
      address: '123 Main St'
    });

    component.registrationForm.updateValueAndValidity();
    fixture.detectChanges();

    console.log('Form valid?', component.registrationForm.valid);
    console.log('Errors:', component.registrationForm.errors);

    component.onSubmit();
    tick();
    flush(); // Ensure timers are cleared

    expect(mockUserService.createUser).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/event-management']);
  }));

  it('should show an error message if form submission fails', () => {
    // Spy on the `open` method of MatSnackBar
    const snackBarSpy = spyOn(TestBed.inject(MatSnackBar), 'open');
    
    // Mock the createUser method to throw an error
    mockUserService.createUser.and.returnValue(throwError(() => new Error('Error creating user')));
  
    // Populate the form with valid data
    component.registrationForm.setValue({
      userType: 'OD',
      email: 'test@example.com',
      password: 'StrongPass1!',
      confirmPassword: 'StrongPass1!',
      firstName: 'John',
      lastName: 'Doe',
      phone: '1234567890',
      address: '123 Main St',
      businessName: '',
      businessAddress: '',
      businessPhone: '',
      about: ''
    });
  
    // Call the submit method
    component.onSubmit();
  
    // Assert that the snackBar's open method was called with the expected arguments
    expect(snackBarSpy).toHaveBeenCalledWith(
      'Registration failed. Please try again.',
      'Close',
      { duration: 3000 }
    );
  });


  it('should reset optional fields when userType changes', () => {
    spyOn(component, 'updateValidators').and.callThrough();
  
    // Create an Event and set the target property
    const event = new Event('change');
    Object.defineProperty(event, 'target', { 
      writable: false, 
      value: { value: 'PUP' } 
    });
  
    // Simulate userType change
    component.onUserTypeChange(event);
  
    expect(component.isBusinessEntity).toBeTrue();
    expect(component.isOrganizer).toBeFalse();
    expect(component.updateValidators).toHaveBeenCalled();
  });
});
