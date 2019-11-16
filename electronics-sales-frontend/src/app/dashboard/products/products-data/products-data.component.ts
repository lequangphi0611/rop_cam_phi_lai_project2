import { CategoryService } from './../../../services/category.service';
import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import {
  AfterViewInit,
  Component,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { finalize, map, takeUntil, tap } from 'rxjs/operators';
import { ProductSortType } from 'src/app/models/types/product-sort-type.type';
import { SortType } from 'src/app/models/types/sort-type.type';
import { ProductService } from 'src/app/services/product.service';
import { FetchProductOption } from './../../../models/fetch-product-option.model';
import { CategoryView } from './../../../models/view-model/category.view.model';
import { DiscountView } from './../../../models/view-model/discount.view';
import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { ProductView } from './../../../models/view-model/product.view.model';
import { ManufacturerService } from './../../../services/manufacturer.service';
import { ImportProductsComponent } from './../import-products/import-products.component';

@Component({
  selector: 'app-products-data',
  templateUrl: './products-data.component.html',
  styleUrls: ['./products-data.component.css'],
})
export class ProductsDataComponent implements OnInit, OnDestroy, AfterViewInit {
  dataSource: ProductsDataSource;

  unSubscription$ = new Subject();

  pageNumber = 0;

  elementSize = 5;

  maxSize = 0;

  readonly defaultFetchOption: FetchProductOption = {
    page: this.pageNumber,
    size: this.elementSize,
    productSortType: ProductSortType.TIME,
    sortType: SortType.DESC,
  };

  fetchOption = new BehaviorSubject<FetchProductOption>(
    this.defaultFetchOption
  );

  fetchOption$ = this.fetchOption.asObservable();

  currentOption: FetchProductOption = { ...this.defaultFetchOption };

  categories$: Observable<CategoryView[]>;

  @Output() onEditClicked = new EventEmitter<any>(true);

  @Output() onDeleted = new EventEmitter<any>(true);

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  displayedColumns = [
    'image',
    'name',
    'price',
    'quantity',
    'manufacturer',
    'categories',
    'importQuantity',
    'edit',
    'delete',
  ];

  constructor(
    private productService: ProductService,
    private manufacturerService: ManufacturerService,
    private router: Router,
    private snackbar: MatSnackBar,
    private dialog: MatDialog,
    private categoryService: CategoryService
  ) {}

  ngOnInit() {
    this.fetchCategories();

    this.fetchOption
      .pipe(takeUntil(this.unSubscription$))
      .subscribe(fetchOption => {
        console.log(fetchOption);
        this.fetchMaxSize(fetchOption);
      });

    this.dataSource = new ProductsDataSource(
      this.productService,
      this.manufacturerService
    );
    this.dataSource.init(this.currentOption);
  }

  ngAfterViewInit() {
    this.paginator.page.pipe(tap(() => this.loadProductsPage())).subscribe();
  }

  fetchCategories() {
    this.categories$ = this.categoryService.fetchCategories();
  }

  fetchMaxSize(option: FetchProductOption) {
    this.productService
      .countProduct(option)
      .pipe(takeUntil(this.unSubscription$))
      .subscribe(v => (this.maxSize = v));
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
          duration: 2000,
        });
        this.dataSource.loadProducts(this.currentOption);
        this.onDeleted.emit();
      });
  }

  openDialog(product: any) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = true;

    dialogConfig.minWidth = "40rem";

    dialogConfig.data = {
      product,
    };

    const dialogRef = this.dialog.open(ImportProductsComponent, dialogConfig);

    dialogRef.componentInstance.onSaveSuccess
      .pipe(takeUntil(this.unSubscription$))
      .subscribe(() => this.dataSource.loadProducts(this.currentOption));
  }

  trackById(index: number, item: ProductDataView) {
    return item.id;
  }

  loadProductsPage() {
    this.currentOption.page = this.paginator.pageIndex;
    this.currentOption.size = this.paginator.pageSize;
    this.dataSource.loadProducts(this.currentOption);
  }

  onChangeCategory(categoryId: number) {
    this.currentOption.categoriesId = categoryId === 0 ? [] : [categoryId];
    this.fetchOption.next(this.currentOption);
    this.dataSource.loadProducts(this.currentOption);
  }

  ngOnDestroy() {
    this.unSubscription$.next();
    this.unSubscription$.complete();
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
