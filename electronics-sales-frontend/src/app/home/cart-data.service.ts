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

  private cart = new BehaviorSubject<Cart>(null);

  cart$ = this.cart.asObservable();

  constructor(
    @Inject(LOCAL_STORAGE) private localStorageService: StorageService
  ) {
    this.cart$
      .pipe(filter(cart => cart != null))
      .subscribe(() => this.saveCartToStorage());
  }

  initCart() {
    this.cartData = new Cart([]);
  }

  getCartFromStorage() {
    return this.localStorageService.get(CartDataService.STORAGE_KEY);
  }

  saveCartToStorage(): void {
    this.localStorageService.set(CartDataService.STORAGE_KEY, this.cartData);
  }

  load(): void {
    const cartInStorage = this.getCartFromStorage() as {cartItems: CartItem[]};
    if (!cartInStorage) {
      return;
    }
    this.cartData = new Cart(cartInStorage.cartItems.map(cartItem => {
      // tslint:disable-next-line: radix
      cartItem.quantity = parseInt(`${cartItem.quantity}`);
      return cartItem;
    }));
    this.cart.next(this.cartData);
  }

  push(product: ProductView, quantity = 1): boolean {
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

  clear() {
    this.initCart();
    this.cart.next(this.cartData);
    this.localStorageService.remove(CartDataService.STORAGE_KEY);
  }

  get CartData() {
    return this.cartData;
  }

}
