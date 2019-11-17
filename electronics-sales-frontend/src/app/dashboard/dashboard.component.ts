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
    name: 'Hãng sản xuất',
    fontAwesomeClass: 'fas fa-handshake',
    links: [`manufacturer`],
  },
  {
    name: 'Loại sản phẩm',
    fontAwesomeClass: 'fas fa-clipboard-list',
    links: [`category`],
  },
  {
    name: 'Sản phẩm',
    fontAwesomeClass: 'fas fa-shopping-cart',
    links: [`product`],
  },
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
