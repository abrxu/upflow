import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

// --- Import your Models and Services ---
import { DepartmentRole, UserAssociation } from '../../models/user.model';
import { UserService } from '../../services/user.service';

// --- Import Angular Material Modules ---
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule, // <-- Import for reactive forms
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule
  ],
  templateUrl: './user-create.component.html',
  styleUrls: ['./user-create.component.scss']
})
export class UserCreateComponent {
  // Inject services and FormBuilder using the modern `inject` function
  private fb = inject(FormBuilder);
  private userService = inject(UserService);

  // Define the form structure
  userForm: FormGroup;
  departmentRoles = Object.values(DepartmentRole);

  constructor() {
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      associations: this.fb.array([]) // Start with an empty array of associations
    });
  }

  // Helper to get the 'associations' FormArray for easy access in the template
  get associations(): FormArray {
    return this.userForm.get('associations') as FormArray;
  }

  // Method to create a new FormGroup for an association
  newAssociation(): FormGroup {
    return this.fb.group({
      departmentId: ['', Validators.required],
      role: [DepartmentRole.MEMBER, Validators.required]
    });
  }

  // Method to add a new association to the FormArray
  addAssociation() {
    this.associations.push(this.newAssociation());
  }

  // Method to remove an association from the FormArray at a specific index
  removeAssociation(index: number) {
    this.associations.removeAt(index);
  }

  // Method to handle form submission
  onSubmit() {
    if (this.userForm.invalid) {
      console.error('Form is invalid');
      return; // Stop if the form is invalid
    }

    console.log('Submitting form:', this.userForm.value);

    // Call the service to send the data to the backend
    this.userService.createUser(this.userForm.value).subscribe({
      next: (response) => {
        console.log('User created successfully!', response);
        // Here you would typically show a success message (e.g., a snackbar) and navigate away
        this.userForm.reset();
        this.associations.clear();
      },
      error: (error) => {
        console.error('Error creating user:', error);
        // Here you would show an error message to the user
      }
    });
  }
}