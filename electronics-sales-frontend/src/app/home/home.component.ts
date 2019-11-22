import { CartDataService } from './cart-data.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CartItem } from '../models/cart-item.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private cartData: CartDataService) {
   }

  ngOnInit() {
    this.cartData.load();
  }

}
