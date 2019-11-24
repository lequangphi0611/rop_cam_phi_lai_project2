import { finalize, takeUntil } from 'rxjs/operators';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Page } from './../../../models/page.model';
import { Observable, of, BehaviorSubject, Subject } from 'rxjs';
import { ProductService } from './../../../services/product.service';
import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { ProductBanner } from '../home-content-default.component';

@Component({
  selector: 'app-show-product-content',
  templateUrl: './show-product-content.component.html',
  styleUrls: [
    '../../home.component.css',
    '../../../../assets/css/magiczoom.css',
  ],
})
export class ShowProductContentComponent implements OnInit, OnDestroy {
  @Input() productBanner: ProductBanner;

  products$ = new BehaviorSubject<ProductView[]>(null);

  unscription$ = new Subject();


  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService
      .fetchProduct(this.productBanner.option)
      .pipe(takeUntil(this.unscription$))
      .subscribe(products => this.products$.next(products));


  }

  ngOnDestroy() {
    this.products$.complete();
    this.unscription$.next();
    this.unscription$.complete();
  }
}
