import { MatSnackBar } from '@angular/material/snack-bar';
import { CartDataService } from './../cart-data.service';
import { map } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ProductView } from 'src/app/models/view-model/product.view.model';
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
    this.image$ = this.product.images$.pipe(map(images => images[0].data));
  }

  goToDetail(id: number): void {
    this.router.navigate([`/index/product/${id}`]);
  }

  addToCart(productId: number) {
    this.cartData.push({productId, quantity: 1});
    this.snackBar.open('Thêm vào giỏ thành công !', 'Đóng', {duration: 2000});
  }

  ngOnDestroy(): void {
  }
}
