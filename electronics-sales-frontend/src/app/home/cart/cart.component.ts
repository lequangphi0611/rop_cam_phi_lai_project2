import { Router } from '@angular/router';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { ProductService } from './../../services/product.service';
import { CartDataView } from './../../models/cart-item-data.view.model';
import { map, takeUntil, tap } from 'rxjs/operators';
import { CartDataService } from './../cart-data.service';
import { Observable, of, Subject } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['../home.component.css', './cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {
  cartItems: CartDataView[] = [];

  unscriptions$ = new Subject();

  constructor(
    private cartData: CartDataService,
    private productService: ProductService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cartData.cart$
      .pipe(
        takeUntil(this.unscriptions$),
        map(cart =>
          cart.cartItems.map(v => CartDataView.of(v, this.productService))
        )
      )
      .subscribe(v => (this.cartItems = v));
  }

  onQuantityChange(event, product: ProductView) {
    const quantity = event.target.value;
    this.cartData.set(product, quantity);
  }

  get total() {
    return this.cartItems.reduce((total, next) => total + next.total, 0);
  }

  goToViewProduct(idProduct: number) {
    this.router.navigate(['index', 'product', idProduct]);
  }

  remove(productId: number) {
    this.cartData.remove(productId);
  }

  ngOnDestroy() {
    this.unscriptions$.next();
    this.unscriptions$.complete();
  }
}
