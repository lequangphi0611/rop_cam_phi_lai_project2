import { CheckoutItemView } from './../checkout.component';
import { Component, Input, OnInit } from '@angular/core';
import { ProductService } from './../../../services/product.service';
import { CartDataService } from './../../cart-data.service';

@Component({
  selector: 'app-checkout-items',
  templateUrl: './checkout-items.component.html',
  styleUrls: ['./checkout-items.component.css']
})
export class CheckoutItemsComponent implements OnInit {
  @Input() checkoutItems: CheckoutItemView[] = [];

  constructor(
    private cartData: CartDataService,
    private productService: ProductService
  ) {}

  ngOnInit() {
  }

  get sumTotal() {
    return this.checkoutItems.reduce(
      (total, currentValue) => total + currentValue.total,
      0
    );
  }
}


