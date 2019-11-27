import { switchMap, map, catchError } from 'rxjs/operators';
import { UserService } from './services/user.service';
import { UserAuthenticatedService } from './services/user-authenticated.service';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Role } from './models/types/role.type';

@Injectable({
  providedIn: 'root'
})
export class DashboarGuardGuard implements CanActivate {

  static readonly DASHBOARD_ROLE = [Role.EMPLOYEE, Role.MANAGER];

  constructor(private userAuthenticated: UserAuthenticatedService,
              private userService: UserService,
              private route: Router ) {
  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.userAuthenticated.isAuthenticated()
      .pipe(
        switchMap(isAuth => {
          if (!isAuth) {
            return of(false);
          }
          return this.checkRoleDashboard();
        }),
        map((i) => {
          if (!i) {
            this.route.navigate(['index']);
            return false;
          }
          console.log('dashboard nè');
          return true;
        })
      );
  }

  checkRoleDashboard(): Observable<boolean> {
    return this.userService
      .getCurrentRole()
      .pipe(
        map(role => DashboarGuardGuard.DASHBOARD_ROLE.includes(role)),
        catchError(() => of(false))
      ) as Observable<boolean>;
  }

}
