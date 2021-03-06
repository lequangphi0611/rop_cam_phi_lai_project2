import { Router } from '@angular/router';
import { map, catchError, switchMap, tap } from 'rxjs/operators';
import { AuthInterceptorService } from './../auth/auth-interceptor.service';
import { CookieService } from 'ngx-cookie-service';
import { User } from './../models/view-model/user.view.model';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { UserService } from './user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class UserAuthenticatedService {
  userBehavior = new BehaviorSubject<User>(null);

  user$ = this.userBehavior.asObservable();

  constructor(
    private authenticatedService: AuthInterceptorService,
    private userService: UserService,
    private cookie: CookieService,
    private router: Router
  ) {}

  setUser(user: User) {
    this.userBehavior.next(user);
  }

  checkAuthenticateInServer(): Observable<boolean> {
    return this.userService.getCurrentUser().pipe(
      map(user => user != null),
      catchError(err => of(false))
    );
  }

  isAuthenticated(): Observable<boolean> {
    return of(this.authenticatedService.isAuthenticated()).pipe(
      switchMap(isAuthen => {
        if (isAuthen) {
          return this.checkAuthenticateInServer();
        }
        return of(false);
      })
    );
  }

  get value() {
    return this.userBehavior.value;
  }

  load(): void {
    this.userService.getCurrentUser().subscribe(
      user => this.setUser(user)
    );
  }

  clear(): void {
    this.setUser(null);
    this.cookie.delete('user');
  }
}
