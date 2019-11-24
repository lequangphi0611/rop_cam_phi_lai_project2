import { ProductParameterView } from './../../models/view-model/product-parameter.view';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Observable, pipe, of, Subscription, Subject, BehaviorSubject } from 'rxjs';
import { ProductService } from './../../services/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

  constructor(
    private productService: ProductService,
    private activedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.productId$ = this.activedRoute.paramMap.pipe(
      switchMap(params => of(+params.get('id')))
    );

    this.productId$.pipe(
      takeUntil(this.unscription$),
      switchMap(id => this.productService.getProduct(id))
    ).subscribe(product => this.product$.next(product));

    this.parameters$ = this.productId$.pipe(
      switchMap(id => this.productService.getParameters(id)),
    );

    this.productImages$ = this.product$.pipe(
      filter(product => product != null),
      switchMap(product => product.images$),
      map(images => images.map(image => image.data)));
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
    this.product$.complete();
  }
}
