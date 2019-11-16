import { Router } from '@angular/router';
import { CategoryView } from './../../../models/view-model/category.view.model';
import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { CategoryService } from './../../../services/category.service';
import { ManufacturerService } from './../../../services/manufacturer.service';
import { DiscountView } from './../../../models/view-model/discount.view';
import { catchError, finalize, map, takeUntil } from 'rxjs/operators';
import { FetchProductOption } from './../../../models/fetch-product-option.model';
import { Observable, BehaviorSubject, of, Subject } from 'rxjs';
import { ProductView } from './../../../models/view-model/product.view.model';
import { Component, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { DataSource } from '@angular/cdk/table';
import { CollectionViewer } from '@angular/cdk/collections';
import { ProductService } from 'src/app/services/product.service';
import { ProductSortType } from 'src/app/models/types/product-sort-type.type';
import { SortType } from 'src/app/models/types/sort-type.type';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-products-data',
  templateUrl: './products-data.component.html',
  styleUrls: ['./products-data.component.css'],
})
export class ProductsDataComponent implements OnInit, OnDestroy {
  dataSource: ProductsDataSource;

  unSubscription$ = new Subject();

  @Output() onEditClicked = new EventEmitter<any>(true);

  @Output() onDeleted = new EventEmitter<any>(true);

  displayedColumns = [
    'image',
    'name',
    'price',
    'quantity',
    'manufacturer',
    'categories',
    'edit',
    'delete',
  ];

  pageNumber = 0;

  elementSize = 100;

  readonly defaultFetchOption: FetchProductOption = {
    page: this.pageNumber,
    size: this.elementSize,
    productSortType: ProductSortType.TIME,
    sortType: SortType.DESC,
  };

  constructor(
    private productService: ProductService,
    private manufacturerService: ManufacturerService,
    private router: Router,
    private snackbar: MatSnackBar
  ) {}

  ngOnInit() {
    this.dataSource = new ProductsDataSource(
      this.productService,
      this.manufacturerService
    );
    this.dataSource.init(this.defaultFetchOption);
  }

  goToEdit(product: ProductDataView) {
    this.onEditClicked.emit(product);
  }

  delete(id: number) {
    this.productService
    .deleteProduct(id)
    .pipe(takeUntil(this.unSubscription$))
    .subscribe(() => {
      this.snackbar.open('Xóa thành công', 'Đóng', {
        duration: 2000
      });
      this.dataSource.loadProducts(this.defaultFetchOption);
      this.onDeleted.emit();
    });
  }

  ngOnDestroy() {
    this.unSubscription$.next();
    this.unSubscription$.complete();
  }

  trackById(index: number, item: ProductDataView) {
    return item.id;
  }
}

export class ProductsDataSource implements DataSource<ProductDataView> {
  private productSubject = new BehaviorSubject<ProductDataView[]>([]);

  private loadingSubject = new BehaviorSubject<boolean>(false);

  loading$ = this.loadingSubject.asObservable();

  constructor(
    private productService: ProductService,
    private manufacturerService: ManufacturerService
  ) {}

  connect(
    collectionViewer: CollectionViewer
  ): Observable<ProductDataView[] | readonly ProductDataView[]> {
    return this.productSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.productSubject.complete();
    this.loadingSubject.complete();
  }

  fetchProduct(option: FetchProductOption): Observable<ProductDataView[]> {
    return this.productService.fetchProduct(option).pipe(
      map(productViews => {
        return productViews.map(productView =>
          ProductDataView.build(
            productView,
            this.manufacturerService,
            this.productService
          )
        );
      })
    );
  }

  loadProducts(option: FetchProductOption) {
    this.fetchProduct(option).subscribe(products =>
      this.productSubject.next(products)
    );
  }

  init(option: FetchProductOption) {
    this.loadingSubject.next(true);
    this.fetchProduct(option)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe(products => this.productSubject.next(products));
  }

}

export class ProductDataView extends ProductView {
  public manufacturer$: Observable<ManufacturerView>;

  public categories$: Observable<CategoryView[]>;

  imageData$: Observable<string>;

  private constructor(
    public id: number,
    public productName: string,
    public price: number,
    public quantity: number,
    private manufacturerService: ManufacturerService,
    public productService: ProductService,
    public createdTime?: Date,
    public updatedTime?: Date,
    public manufacturerId?: number,
    public discount?: DiscountView
  ) {
    super(
      id,
      productName,
      price,
      quantity,
      productService,
      createdTime,
      updatedTime,
      manufacturerId,
      discount
    );
    this.manufacturer$ = this.manufacturerService.getManufacturerBy(
      this.manufacturerId
    );
    this.categories$ = this.productService.getCategories(this.id);
    this.imageData$ = this.images$.pipe(map(images => images[0].data));
  }

  static build(
    product: ProductView,
    manufacturerService: ManufacturerService,
    productService: ProductService
  ) {
    return new ProductDataView(
      product.id,
      product.productName,
      product.price,
      product.quantity,
      manufacturerService,
      productService,
      product.createdTime,
      product.updatedTime,
      product.manufacturerId,
      product.discount
    );
  }
}
