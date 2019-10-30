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

  productImages: string[] = [];

  subcription: Subscription;

  parameters: Observable<ProductParameterView[]>;

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

    this.subcription = this.product$.subscribe(
      product =>
        (this.productImages = product.imageIds.map(
          imageId => `/api/images/${imageId}`
        ))
    );

    this.parameters = this.productId$.pipe(
      switchMap(id => this.productService.getParameters(id))
    );
  }

  ngOnDestroy(): void {
    this.subcription.unsubscribe();
  }
}
