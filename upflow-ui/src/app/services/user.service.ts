import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { LoginPayload, LoginResponse, UserCreationPayload } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiUrl = 'http://localhost:9090/api/v1';

  constructor(private readonly http: HttpClient) { }

  createUser(user: UserCreationPayload): Observable<any> {
    return this.http.post(`${this.apiUrl}/user`, user);
  }

  login(credentials: LoginPayload): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials);
  }

  loginWithGoogle(): Observable<LoginResponse> {
    // TODO: Implement Google OAuth integration
    // For now, return an error observable to prevent compilation issues
    return throwError(() => new Error('Google login not yet implemented'));

    // When implementing, it would look something like:
    // return this.http.post<LoginResponse>(`${this.apiUrl}/auth/google`, {});
  }

  loginWithMicrosoft(): Observable<LoginResponse> {
    // TODO: Implement Microsoft OAuth integration  
    // For now, return an error observable to prevent compilation issues
    return throwError(() => new Error('Microsoft login not yet implemented'));

    // When implementing, it would look something like:
    // return this.http.post<LoginResponse>(`${this.apiUrl}/auth/microsoft`, {});
  }
}