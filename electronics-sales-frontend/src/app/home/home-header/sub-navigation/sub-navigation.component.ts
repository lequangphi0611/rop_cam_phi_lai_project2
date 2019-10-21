import { Component, OnInit, Input } from '@angular/core';

export interface NavItemData {

  name: string;

  link: string;

}

@Component({
  selector: 'app-sub-navigation',
  templateUrl: './sub-navigation.component.html',
  styleUrls: ['./sub-navigation.component.css']
})
export class SubNavigationComponent implements OnInit {

  @Input() navItems: NavItemData[] = [];

  constructor() { }

  ngOnInit() {
  }

}
