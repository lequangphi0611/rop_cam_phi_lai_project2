import { Role } from './models/types/role.type';
import { filter, map, tap } from 'rxjs/operators';
import { UserAuthenticatedService } from './services/user-authenticated.service';
import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
  Router
} from '@angular/router';
import { Observable } from 'rxjs';

const MANAGER_ROLE = Role.MANAGER;

const DASHBOARD_URL = '/dashboard';

@Injectable({
  providedIn: 'root'
})
export class ManagerGuard implements CanActivate {
  constructor(
    private userAuthenticatedData: UserAuthenticatedService,
    private router: Router
  ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.userAuthenticatedData.user$.pipe(
      map(user => user.role),
      map(role => role === MANAGER_ROLE),
      tap(isManager => {
        if (!isManager) {
          this.router.navigate([DASHBOARD_URL]);
        }
      })
    );
  }
}
