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
    this.cartData = new Cart(cartInStorage.cartItems);
    this.cart.next(this.cartData);
  }

  push(cartItem: CartItem): void {
    this.cartData.push(cartItem);
    this.cart.next(this.cartData);
  }

  get CartData() {
    return this.cartData;
  }

}
