import { Component, OnInit, Input } from '@angular/core';
import { SideBarItem } from '../dashboard.component';

@Component({
  selector: 'app-side-bar-item',
  templateUrl: './side-bar-item.component.html',
  styleUrls: ['../dashboard.component.css', './side-bar-item.component.css']
})
export class SideBarItemComponent implements OnInit {

  @Input() sideBarItems: SideBarItem[];

  constructor() { }

  ngOnInit() {
  }

}
