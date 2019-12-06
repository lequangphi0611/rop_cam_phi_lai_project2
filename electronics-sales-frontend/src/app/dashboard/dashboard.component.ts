import { ChangePasswordDialogComponent } from 'src/app/change-password/change-password-dialog.component';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Role } from './../models/types/role.type';
import { UserAuthenticatedService } from '../services/user-authenticated.service';
import { ConfirmDialogComponent } from '../confirm/confirm-dialog.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';

export interface SideBarItem {
  name: string;

  fontAwesomeClass?: string;

  links?: string[];

  roles?: Role[];
}

const DASHBOARD_ROLE = [Role.EMPLOYEE, Role.MANAGER];

const MANAGER_ROLE = [Role.MANAGER];

const SIDE_BAR_ITEMS: SideBarItem[] = [
  {
    name: 'Tổng quan',
    fontAwesomeClass: 'fas fa-home',
    links: ['overview'],
    roles: DASHBOARD_ROLE
  },
  {
    name: 'Hãng sản xuất',
    fontAwesomeClass: 'fas fa-handshake',
    links: [`manufacturer`],
    roles: DASHBOARD_ROLE
  },
  {
    name: 'Loại sản phẩm',
    fontAwesomeClass: 'fas fa-clipboard-list',
    links: [`category`],
    roles: DASHBOARD_ROLE
  },
  {
    name: 'Giảm Giá',
    fontAwesomeClass: 'fas fa-tags',
    links: [`discounts`],
    roles: DASHBOARD_ROLE
  },
  {
    name: 'Sản phẩm',
    fontAwesomeClass: 'fas fa-shopping-cart',
    links: [`product`],
    roles: DASHBOARD_ROLE
  },
  {
    name: 'Đơn hàng',
    fontAwesomeClass: 'fas fa-clipboard-list',
    links: [`transactions`],
    roles: DASHBOARD_ROLE
  },
  {
    name: 'Nhân viên',
    fontAwesomeClass: 'fas fa-user-friends',
    links: [`employees`],
    roles: MANAGER_ROLE
  }
];

const LOGIN_URL = '/index/login';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  userImage = '/assets/img/avatar_user.png';

  sideBarItems: SideBarItem[];

  constructor(public userData: UserAuthenticatedService, private dialog: MatDialog, private router: Router) {}

  ngOnInit() {
    this.sideBarItems = SIDE_BAR_ITEMS;
  }

  openLogoutConfirm() {
    ConfirmDialogComponent.open(
      this.dialog,
      {
        actionName: 'Đăng Xuất',
        message: 'Bạn có thực sự muốn đăng xuất ?',
        action: () => this.logout()
      }
    );
  }

  openChangePasswordForm() {
    const config: MatDialogConfig = {
      autoFocus : true,
      disableClose: false
    };
    this.dialog.open(ChangePasswordDialogComponent, config);
  }

  logout() {
    this.userData.clear();
    this.router.navigate([LOGIN_URL]);
  }
}
