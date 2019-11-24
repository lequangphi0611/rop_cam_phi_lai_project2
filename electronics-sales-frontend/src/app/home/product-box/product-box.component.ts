import { ProductView } from './../../models/view-model/product.view.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CartDataService } from './../cart-data.service';
import { map, filter } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { CartItem } from 'src/app/models/cart-item.model';

@Component({
  selector: 'app-product-box',
  templateUrl: './product-box.component.html',
  styleUrls: ['./product-box.component.css']
})
export class ProductBoxComponent implements OnInit, OnDestroy {

  @Input() product: ProductView;

  subcription: Subscription;

  image$: Observable<string>;

  constructor(
    private router: Router,
    private productService: ProductService,
    private cartData: CartDataService,
    private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.image$ = this.product.images$.pipe(
      filter(v => v.length > 0),
      map(images => images[0].data)
    );
  }

  goToDetail(id: number): void {
    this.router.navigate([`/index/product/${id}`]);
  }

  addToCart(product: ProductView) {
    if (this.cartData.push(product, 1)) {
      const snackbar = this.snackBar.open('Thêm vào giỏ thành công !', 'Xem giỏ hàng', {duration: 2000});
      snackbar.onAction().subscribe(() => {
        this.router.navigate(['index', 'cart']);
      });
      return;
    }
    this.snackBar.open('Thêm vào giỏ không thành công !', 'Đóng', {duration: 2000});
  }

  ngOnDestroy(): void {
  }
}
