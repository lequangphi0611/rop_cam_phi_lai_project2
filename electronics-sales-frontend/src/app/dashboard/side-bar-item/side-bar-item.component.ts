import { takeUntil, map, filter } from 'rxjs/operators';
import { Role } from './../../models/types/role.type';
import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { SideBarItem } from '../dashboard.component';
import { UserAuthenticatedService } from 'src/app/services/user-authenticated.service';
import { Subject } from 'rxjs';
import { ConfirmDialogComponent } from 'src/app/confirm/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-side-bar-item',
  templateUrl: './side-bar-item.component.html',
  styleUrls: ['../dashboard.component.css', './side-bar-item.component.css']
})
export class SideBarItemComponent implements OnInit, OnDestroy {

  @Input() sideBarItems: SideBarItem[];

  unscription$ = new Subject();

  role: Role;

  constructor(private userData: UserAuthenticatedService) { }

  ngOnInit() {
    this.userData
      .user$
      .pipe(
        takeUntil(this.unscription$),
        filter(user => user && true),
        map(user => user.role)
      ).subscribe(role => this.role = role);
  }

  accessRole(roles: Role[]) {
    return this.role && roles.includes(this.role);
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
