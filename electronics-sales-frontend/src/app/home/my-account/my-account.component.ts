import { LIST_ITEM_USER } from './../List-item-user';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css']
})
export class MyAccountComponent implements OnInit {

  listItem = LIST_ITEM_USER;

  constructor() { }

  ngOnInit() {
  }

}
