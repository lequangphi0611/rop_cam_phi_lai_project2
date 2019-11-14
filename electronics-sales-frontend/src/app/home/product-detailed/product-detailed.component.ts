import { ProductParameterView } from './../../models/view-model/product-parameter.view';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Observable, pipe, of, Subscription } from 'rxjs';
import { ProductService } from './../../services/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { switchMap, map } from 'rxjs/operators';

@Component({
  selector: 'app-product-detailed',
  templateUrl: './product-detailed.component.html',
  styleUrls: ['./product-detailed-component.css'],
})
export class ProductDetailedComponent implements OnInit, OnDestroy {
  productId$: Observable<number>;

  product$: Observable<ProductView>;

  productImages$: Observable<string[]>;

  parameters$: Observable<ProductParameterView[]>;

  constructor(
    private productService: ProductService,
    private activedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.productId$ = this.activedRoute.paramMap.pipe(
      switchMap(params => of(+params.get('id')))
    );

    this.product$ = this.productId$.pipe(
      switchMap(id => this.productService.getProduct(id))
    );

    this.parameters$ = this.productId$.pipe(
      switchMap(id => this.productService.getParameters(id)),
    );

    this.productImages$ = this.product$.pipe(switchMap(product => product.images$),
      map(images => images.map(image => image.data)));
  }

  ngOnDestroy(): void {
  }
}
