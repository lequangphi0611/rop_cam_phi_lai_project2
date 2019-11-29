import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';


const TOKEN_PREFIX = 'Bearer';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private cookieService: CookieService) { }

  isAuthenticated(): boolean {
    const userCookie = this.cookieService.get('user');
    return userCookie != null && userCookie !== '' && userCookie !== undefined;
  }

  getToken(): string {
    return this.isAuthenticated() ? JSON.parse(this.cookieService.get('user')).token : null;
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    request = request.clone({
      setHeaders: {
        Authorization: `${TOKEN_PREFIX} ${this.getToken()}`
      }
    });

    return next.handle(request);
  }
}
