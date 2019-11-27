import { Role } from './../models/types/role.type';
import { UserDto } from './../models/dtos/user.dto';
import { User } from './../models/view-model/user.view.model';
import { AccountDto } from './../models/dtos/account.dto';
import { AuthenticatedResponse } from './../models/dtos/authenticated.response';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { map, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private static readonly LOGIN_REQUEST: string = '/api/login';

  private static readonly REGISTER_REQUEST: string = 'api/register';

  private static readonly EXPRIED_TIME = 604800000;

  public static readonly USER_COOKIE_KEY = 'user';

  constructor(private http: HttpClient, private cookieService: CookieService) {}

  mapUser(user: User): User {
    user.birthday = new Date(user.birthday);
    return user;
  }

  login(loginRequest: AccountDto): Observable<AuthenticatedResponse> {
    return this.http
      .post<AuthenticatedResponse>(UserService.LOGIN_REQUEST, loginRequest)
      .pipe(
        map(authentiatedResponse => {
          this.onLogin(authentiatedResponse);
          return authentiatedResponse;
        })
      );
  }

  onLogin(auth: AuthenticatedResponse) {
    this.cookieService.delete(UserService.USER_COOKIE_KEY);
    this.cookieService.set(
      UserService.USER_COOKIE_KEY,
      JSON.stringify(auth),
      UserService.EXPRIED_TIME,
      '/'
    );
  }

  register(user: UserDto): Observable<AuthenticatedResponse> {
    return this.http
      .post<AuthenticatedResponse>(UserService.REGISTER_REQUEST, user)
      .pipe(
        map(auth => {
          this.onLogin(auth);
          return auth;
        })
      );
  }

  getCurrentRole(): Observable<Role> {
    return this.http.get<{ role: string }>('/api/accounts/roles').pipe(
      map(result => result.role.toUpperCase()),
      map(roleStr => Role[roleStr])
    );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>('/api/accounts/current')
    .pipe(map(this.mapUser));
  }

  existsByUsername(username: string): Observable<boolean> {
    return this.http.head<any>(`/api/accounts/username/${username}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }
}
