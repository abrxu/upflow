import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { LoginPayload } from '../models/user.model';
import { delay, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:9190/api/v1/user/login'

  login(payload: LoginPayload): Observable<any> {
    return this.http.post(this.apiUrl, payload);
  }

}
