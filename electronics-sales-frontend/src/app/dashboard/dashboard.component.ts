import { Role } from './../models/types/role.type';
import { Component, OnInit } from '@angular/core';

export interface SideBarItem {

  name: string;

  fontAwesomeClass?: string;

  links?: string[];

  role?: Role;

}

const SIDE_BAR_ITEMS: SideBarItem[] = [
  {
    name: 'Tổng quan',
    fontAwesomeClass: 'fas fa-home',
    links: ['overview'],
  },
  {
    name: 'Sản phẩm',
    fontAwesomeClass: 'fas fa-shopping-cart',
    links: [`product`],
  },
  // {
  //   name: 'Nhà sản xuất',
  //   fontAwesomeClass: 'fas fa-handshake',
  //   links: [''],
  // },
  // {
  //   name: 'Tin tức, bài viết',
  //   fontAwesomeClass: 'fas fa-newspaper',
  //   links: ['']
  // },
  // {
  //   name: 'Nhà sản xuất',
  //   fontAwesomeClass: 'fas fa-handshake',
  //   links: [''],
  // },
  // {
  //   name: 'Nhà sản xuất',
  //   fontAwesomeClass: 'fas fa-handshake',
  //   links: [''],
  // }
];

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  sideBarItems: SideBarItem[];

  constructor() { }

  ngOnInit() {
    this.sideBarItems = SIDE_BAR_ITEMS;
  }

}
