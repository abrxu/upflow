import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { CheckboxModule } from 'primeng/checkbox';
import { DividerModule } from 'primeng/divider';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { PasswordModule } from 'primeng/password';
import { AuthService } from '../../services/auth.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    FormsModule,
    CardModule,
    ButtonModule,
    DividerModule,
    InputTextModule,
    CheckboxModule,
    FloatLabelModule,
    PasswordModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  loginForm: FormGroup;
  isLoading: boolean = false;

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      rememberMe: [false]
    });
  }

  onLogin() {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.loginForm.disable();
    this.cdr.markForCheck();

    this.authService.login(this.loginForm.value).pipe(
      finalize(() => {
        this.isLoading = false;
        this.loginForm.enable();
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: (response) => {
        console.log('Login bem sucedido!', response);
      },
      error: (error) => {
        console.error('Erro no login:', error);
      }
    });
  }
}

