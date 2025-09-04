import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserCreationPayload } from '../models/user.model';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:9090/api/v1/user';

  constructor(private http: HttpClient) { }

  createUser(user: UserCreationPayload): Observable<any> {
    return this.http.post(this.apiUrl, user);
  }
}
