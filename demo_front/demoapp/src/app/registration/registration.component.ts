import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../service/user/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
  animations: []
})
export class RegistrationComponent implements OnInit {
  registrationForm!: FormGroup;
  isOrganizer = false;
  isBusinessEntity = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.initializeForm();
  }

  initializeForm() {
    this.registrationForm = this.fb.group({
      userType: [null, Validators.required],
      email: ['', [Validators.required, Validators.email]], // Built-in email validation
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]],
      confirmPassword: ['', Validators.required],

      // Organizer Fields
      firstName: [''],
      lastName: [''],
      phone: ['', [Validators.pattern(/^(\+?\d{1,3}[- ]?)?\d{10}$/)]], // Phone number regex validation
      address: [''],

      // Business Entity Fields
      businessName: [''],
      businessAddress: [''],
      businessPhone: ['', [Validators.pattern(/^(\+?\d{1,3}[- ]?)?\d{10}$/)]], // Phone number regex validation
      about: ['']
    }, {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(form: FormGroup): ValidationErrors | null {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { mismatch: true };
  }

  passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.value;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (password && (!hasUpperCase || !hasLowerCase || !hasNumber || !hasSpecialChar)) {
      return { weakPassword: true };
    }
    return null;
  }

  onUserTypeChange(event: Event) {
    const userType = (event.target as HTMLSelectElement).value;

    // Reset the form but keep the selected userType
    const userTypeValue = userType;
    this.registrationForm.reset();
    this.registrationForm.get('userType')?.setValue(userTypeValue);

    // Update flags
    this.isOrganizer = userType === 'OD';
    this.isBusinessEntity = userType === 'PUP';

    // Apply conditional validators
    this.updateValidators();
  }

  updateValidators() {
    // Clear all validators
    this.clearValidators();

    if (this.isOrganizer) {
      this.registrationForm.get('firstName')?.setValidators([Validators.required]);
      this.registrationForm.get('lastName')?.setValidators([Validators.required]);
      this.registrationForm.get('phone')?.setValidators([Validators.required, Validators.pattern(/^(\+?\d{1,3}[- ]?)?\d{10}$/)]);
      this.registrationForm.get('address')?.setValidators([Validators.required]);
    } else if (this.isBusinessEntity) {
      this.registrationForm.get('businessName')?.setValidators([Validators.required]);
      this.registrationForm.get('businessAddress')?.setValidators([Validators.required]);
      this.registrationForm.get('businessPhone')?.setValidators([Validators.required, Validators.pattern(/^(\+?\d{1,3}[- ]?)?\d{10}$/)]);
      this.registrationForm.get('about')?.setValidators([Validators.required]);
    }

    // Update form validators
    this.registrationForm.updateValueAndValidity();
  }

  clearValidators() {
    // Clear validators for all optional fields
    const fieldsToClear = ['firstName', 'lastName', 'phone', 'address', 'businessName', 'businessAddress', 'businessPhone', 'about'];
    fieldsToClear.forEach(field => {
      this.registrationForm.get(field)?.clearValidators();
      this.registrationForm.get(field)?.updateValueAndValidity();
    });
  }

  onFileSelect(event: any) {
    const file = event.target.files[0];
    console.log('Selected file:', file);
  }

  onSubmit() {
    if (this.registrationForm.valid) {
      console.log('Form Data:', this.registrationForm.value);

      // Call the UserService to create a user
      this.userService.createUser(this.registrationForm.value).subscribe({
        next: (response) => {
          console.log('User created successfully:', response);
          this.snackBar.open('Registration successful!', 'Close', { duration: 3000 });
          
          // Navigate to the event-management page
          this.router.navigate(['/event-management']);
        },
        error: (error) => {
          console.error('Error creating user:', error);
          this.snackBar.open('Registration failed. Please try again.', 'Close', { duration: 3000 });
        }
      });
    } else {
      this.snackBar.open('Please fill in all required fields.', 'Close', { duration: 3000 });
    }
  }
}
