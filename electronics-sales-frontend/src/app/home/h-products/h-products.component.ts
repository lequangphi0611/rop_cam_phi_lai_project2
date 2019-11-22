import { CategoryView } from './../../models/view-model/category.view.model';
import { Page } from './../../models/page.model';
import { ManufacturerView } from './../../models/view-model/manufacturer.view.model';
import { ManufacturerService } from './../../services/manufacturer.service';
import { ActivatedRoute } from '@angular/router';
import { takeUntil, filter, distinct, map, switchMap } from 'rxjs/operators';
import { ProductView } from './../../models/view-model/product.view.model';
import { FetchProductOption } from './../../models/fetch-product-option.model';
import { BehaviorSubject, Subject, Observable, of } from 'rxjs';
import { ProductService } from './../../services/product.service';
import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-h-products',
  templateUrl: './h-products.component.html',
  styleUrls: ['../home.component.css', './h-products.component.css'],
})
export class HProductsComponent implements OnInit, AfterViewInit, OnDestroy {
  static readonly DEFAULT_PAGE_SIZE = 8;

  static readonly MIN_PAGE_INDEX = 0;

  readonly defaultOption = {
    manufacturersId: [],
    size: HProductsComponent.DEFAULT_PAGE_SIZE,
    page: HProductsComponent.MIN_PAGE_INDEX,
  };

  fetchProductChange$ = new BehaviorSubject<boolean>(false);

  unscription$ = new Subject();

  products: ProductView[] = [];

  pageIndex$ = new BehaviorSubject<number>(HProductsComponent.MIN_PAGE_INDEX);

  maxPageSize = HProductsComponent.DEFAULT_PAGE_SIZE;

  categoryId$: Observable<number> = of(null);

  manufacturer$: Observable<ManufacturerView[]>;

  category$: Observable<CategoryView>;

  productNameQuery$: Observable<string>;

  fetchproductOption: FetchProductOption = {...this.defaultOption };

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private categoryService: CategoryService,
    private manufacturerService: ManufacturerService
  ) {}

  ngOnInit() {
    this.categoryId$ = this.route.queryParams.pipe(
      filter(params => params.categoryId != null),
      map(params => params.categoryId)
    );

    this.productNameQuery$ = this.route.queryParams.pipe(
      filter(params => params.nameQuery != null),
      map(params => params.nameQuery)
    );

    this.fetchProductChange$
      .pipe(filter(v => v))
      .subscribe(() => this.fetchProducts());

    this.categoryId$.pipe(takeUntil(this.unscription$)).subscribe(v => {
      this.initFetchOption();
      this.fetchproductOption.manufacturersId = [];
      this.fetchproductOption.categoriesId = [v];
      this.fetchProductChange$.next(true);
    });

    this.category$ = this.categoryId$
      .pipe(
        takeUntil(this.unscription$),
        filter(v => v != null),
        switchMap(id => this.categoryService.findById(id))
      );

    this.productNameQuery$.pipe(takeUntil(this.unscription$)).subscribe(v => {
      this.initFetchOption();
      this.fetchproductOption.search = v;
      this.fetchProductChange$.next(true);
    });

    this.pageIndex$
      .pipe(takeUntil(this.unscription$), distinct())
      .subscribe(page => {
        this.fetchproductOption.page = page;
        this.fetchProductChange$.next(true);
      });

    this.fetchManufacturers();
  }

  initFetchOption() {
    this.fetchproductOption.page = HProductsComponent.MIN_PAGE_INDEX;
  }

  fetchManufacturers() {
    this.manufacturer$ = this.categoryId$.pipe(
      switchMap(value => {
        if (!value) {
          return this.manufacturerService.fetchAll();
        }
        return this.categoryService.getManufacturersBy(value);
      })
    );
  }

  ngAfterViewInit() {
    this.fetchProducts();
  }

  get manufacturerIdsOption() {
    return this.fetchproductOption.manufacturersId;
  }

  manufacturerSelectChange(event: boolean, manufacturerId: number) {
    if (event) {
      this.manufacturerIdsOption.push(manufacturerId);
    } else {
      const index = this.manufacturerIdsOption.indexOf(manufacturerId);
      this.manufacturerIdsOption.splice(index, 1);
    }
    this.fetchproductOption.page = HProductsComponent.MIN_PAGE_INDEX;
    this.fetchProductChange$.next(true);
  }

  fetchProducts() {
    this.productService
      .fetchProduct(this.fetchproductOption)
      .pipe(takeUntil(this.unscription$))
      .subscribe(v => (this.products = v));
    this.productService
      .countProduct(this.fetchproductOption)
      .pipe(takeUntil(this.unscription$))
      .subscribe(v => (this.maxPageSize = v));
  }

  get pageIndex() {
    return this.fetchproductOption.page;
  }

  get pageSize() {
    return this.fetchproductOption.size;
  }

  get maxPage() {
    let maxPage = this.maxPageSize / this.pageSize;
    if (this.maxPageSize % this.pageSize !== 0) {
      maxPage--;
    }
    return maxPage;
  }

  hasNext() {
    return this.pageIndex < this.maxPage;
  }

  hasPrev() {
    return this.pageIndex > 0;
  }

  next() {
    this.fetchproductOption.page++;
    this.fetchProductChange$.next(true);
  }

  prev() {
    this.fetchproductOption.page--;
    this.fetchProductChange$.next(true);
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
    this.fetchProductChange$.complete();
  }
}
