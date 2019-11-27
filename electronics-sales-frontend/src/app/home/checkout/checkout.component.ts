import { map } from 'rxjs/operators';
import { CartItem } from './../../models/cart-item.model';
import { IProduct } from './../../models/view-model/product.view.model';
import { ProductService } from './../../services/product.service';
import { CartDataService } from './../cart-data.service';
import { Component, OnInit, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, AfterViewInit {
  checkoutItems: CheckoutItemView[] = [];


  constructor(
    private cartData: CartDataService,
    private productService: ProductService
  ) { }

  ngOnInit() {
    this.fetchCheckoutItems();
  }

  ngAfterViewInit(): void {

  }

  mapToCheckoutItem(cartItem: CartItem) {
    const { productId, quantity } = cartItem;
    return new CheckoutItemView(productId, quantity, this.productService);
  }

  fetchCheckoutItems() {
    this.cartData.cart$
      .pipe(
        map(cart =>
          cart.cartItems.map(cartItem => this.mapToCheckoutItem(cartItem))
        )
      )
      .subscribe(checkoutItems => (this.checkoutItems = checkoutItems));
  }

}

export class CheckoutItemView {
  product: IProduct = null;

  constructor(
    public productId: number,
    public quantity: number,
    public productService: ProductService
  ) {
    this.fetchIProduct();
  }

  fetchIProduct() {
    this.productService
      .getProductNative(this.productId)
      .subscribe(product => (this.product = product));
  }

  get total() {
    if (!this.product) {
      return 0;
    }
    return this.product.price * this.quantity;
  }
}
