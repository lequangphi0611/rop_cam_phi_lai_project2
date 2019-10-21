import { AccountDto } from './../models/dtos/account.dto';
import { AuthenticatedResponse } from './../models/dtos/authenticated.response';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { map } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private static readonly LOGIN_REQUEST: string = '/api/login';

  private static readonly EXPRIED_TIME = 604800000;

  public static readonly USER_COOKIE_KEY = 'user';

  constructor(private http: HttpClient,
              private cookieService: CookieService) { }

  login(loginRequest: AccountDto): Observable<AuthenticatedResponse> {
    return this.http
      .post<AuthenticatedResponse>(UserService.LOGIN_REQUEST, loginRequest)
      .pipe(
        map(authentiatedResponse => {
          this.cookieService.set(
            UserService.USER_COOKIE_KEY,
            JSON.stringify(authentiatedResponse),
            UserService.EXPRIED_TIME
          );
          return authentiatedResponse;
        })
      );
  }

  getCurrentRole(): Observable<{role: string}> {
    return this.http.get<{role: string}>('/api/accounts/roles');
  }
}
