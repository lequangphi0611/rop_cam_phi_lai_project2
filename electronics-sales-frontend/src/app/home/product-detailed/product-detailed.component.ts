import { Cart } from './../../models/cart.model';
import { CartDataService } from './../cart-data.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductParameterView } from './../../models/view-model/product-parameter.view';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Observable, pipe, of, Subscription, Subject, BehaviorSubject, merge, zip } from 'rxjs';
import { ProductService } from './../../services/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap, map, filter, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-product-detailed',
  templateUrl: './product-detailed.component.html',
  styleUrls: ['./product-detailed-component.css'],
})
export class ProductDetailedComponent implements OnInit, OnDestroy {
  productId$: Observable<number>;

  unscription$ = new Subject();

  product$ = new BehaviorSubject<ProductView>(null);

  productImages$: Observable<string[]>;

  parameters$: Observable<ProductParameterView[]>;

  cartRemaining = 0;

  constructor(
    private productService: ProductService,
    private activedRoute: ActivatedRoute,
    private snackBar: MatSnackBar,
    private cartData: CartDataService,
    private router: Router
  ) {}

  ngOnInit() {
    this.productId$ = this.activedRoute.paramMap.pipe(
      takeUntil(this.unscription$),
      switchMap(params => of(+params.get('id')))
    );

    this.productId$.pipe(
      takeUntil(this.unscription$),
      switchMap(id => this.productService.getProduct(id))
    ).subscribe(product => this.product$.next(product));

    this.parameters$ = this.productId$.pipe(
      takeUntil(this.unscription$),
      switchMap(id => this.productService.getParameters(id)),
    );

    this.productImages$ = this.product$.pipe(
      takeUntil(this.unscription$),
      filter(product => product != null),
      switchMap(product => product.images$),
      map(images => images.map(image => image.data)));

    zip(this.cartData.cart$, this.productId$)
      .pipe(takeUntil(this.unscription$))
      .subscribe(v => {
        const cart: Cart = v[0];
        const productId = v[1];
        const index = cart.indexOf({productId, quantity: 1});
        if (index < 0) {
          this.cartRemaining = 0;
          return;
        }

        this.cartRemaining = cart.cartItems[index].quantity;
      });
  }

  addToCart(product: ProductView) {
    if (!this.cartData.push(product)) {
      this.snackBar.open('Thêm vào giỏ hàng không thành công', 'đóng', {duration: 2000});
      return;
    }
    const snackbar = this.snackBar.open('Thêm vào giỏ hàng thành công', 'Xem giỏ hàng', {duration: 2000});
    snackbar.onAction().subscribe(() => this.router.navigate(['index', 'cart']));
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
    this.product$.complete();
  }
}
