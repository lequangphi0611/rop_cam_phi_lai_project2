import { CartItem } from 'src/app/models/cart-item.model';
import { map } from 'rxjs/operators';
import { ProductService } from './../../services/product.service';
import { CartDataService } from './../cart-data.service';
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CartDataView } from 'src/app/models/cart-item-data.view.model';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, AfterViewInit {
  checkoutItems: CartDataView[] = [];


  constructor(
    private cartData: CartDataService,
    private productService: ProductService
  ) { }

  ngOnInit() {
    this.fetchCheckoutItems();
  }

  ngAfterViewInit(): void {

  }

  fetchCheckoutItems() {
    this.cartData.cart$
      .pipe(
        map(cart => cart.cartItems),
        map(cartItems => cartItems.map(cartItem => CartDataView.of(cartItem, this.productService)))
      )
      .subscribe(checkoutItems => (this.checkoutItems = checkoutItems));
  }

}
