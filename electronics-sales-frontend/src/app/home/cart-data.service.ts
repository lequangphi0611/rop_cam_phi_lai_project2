import { ProductView } from './../models/view-model/product.view.model';
import { filter } from 'rxjs/operators';
import { CartItem } from './../models/cart-item.model';
import { Cart } from './../models/cart.model';
import { BehaviorSubject } from 'rxjs';
import { Injectable, Inject } from '@angular/core';

import { LOCAL_STORAGE, StorageService } from 'ngx-webstorage-service';

@Injectable({
  providedIn: 'root'
})
export class CartDataService {

  static readonly STORAGE_KEY = 'YOUR_CART';

  private cartData = new Cart([]);

  private cart = new BehaviorSubject<Cart>(this.cartData);

  cart$ = this.cart.asObservable();

  constructor(
    @Inject(LOCAL_STORAGE) private localStorageService: StorageService
  ) {
    this.cart$
      .pipe(filter(c => c.cartItems.length > 0))
      .subscribe(() => this.saveCartToStorage());
  }

  getCartFromStorage() {
    return this.localStorageService.get(CartDataService.STORAGE_KEY);
  }

  saveCartToStorage(): void {
    console.log('save cart', this.cartData);
    this.localStorageService.set(CartDataService.STORAGE_KEY, this.cartData);
  }

  load(): void {
    const cartInStorage = this.getCartFromStorage() as {cartItems: CartItem[]};
    if (!cartInStorage) {
      return;
    }
    this.cartData = new Cart(cartInStorage.cartItems.map(cartItem => {
      cartItem.quantity = parseInt(`${cartItem.quantity}`);
      return cartItem;
    }));
    console.log('đây nè',  this.cartData)
    this.cart.next(this.cartData);
  }

  push(product: ProductView, quantity = 1): boolean {
    console.log(quantity);
    if (this.cartData.push(product, quantity)) {
      this.cart.next(this.cartData);
      return true;
    }
    return false;
  }

  set(product: ProductView, quantity = 1) {
    if (this.cartData.set(product, quantity)) {
      this.cart.next(this.cartData);
      return true;
    }

    return false;
  }

  remove(productId: number) {
    this.cartData.remove({productId, quantity : 1});
    this.cart.next(this.cartData);
  }

  get CartData() {
    return this.cartData;
  }

}
