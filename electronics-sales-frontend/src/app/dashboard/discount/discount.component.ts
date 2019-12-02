import { ProductView } from 'src/app/models/view-model/product.view.model';
import { DiscountType } from './../../models/types/discount.type';
import { tap, map, switchMap, takeUntil } from 'rxjs/operators';
import { Page } from './../../models/page.model';
import { Observable, BehaviorSubject, Subject } from 'rxjs';
import { DiscountView } from './../../models/view-model/discount.view';
import { DataSource } from '@angular/cdk/table';
import { Component, OnInit, OnDestroy, ViewChild, AfterViewInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiscountFormDialogComponent } from './discount-form-dialog/discount-form-dialog.component';
import {
  DiscountService,
  DiscountFetchOption,
  DEFAULT_DISCOUNT_FETCH_OPTION
} from 'src/app/services/discount.service';
import { DiscountDataComponent } from './discount-data/discount-data.component';

@Component({
  selector: 'app-discount',
  templateUrl: './discount.component.html',
  styleUrls: ['./discount.component.css']
})
export class DiscountComponent implements OnInit, OnDestroy, AfterViewInit {

  @ViewChild(DiscountDataComponent, {static: true}) discountDataComponent: DiscountDataComponent;

  readonly datasource = DiscountDataSource.create(this.discountService);

  private unscriptions$ = new Subject();

  discountFetchOption$ = new BehaviorSubject<DiscountFetchOption>({
    ...DEFAULT_DISCOUNT_FETCH_OPTION
  });

  currentDiscountOption: DiscountFetchOption;

  constructor(
    private dialog: MatDialog,
    private discountService: DiscountService
  ) {}

  ngOnInit() {
    this.discountFetchOption$
      .subscribe(option => {
        this.currentDiscountOption = option;
        this.datasource.load(option);
      });


  }

  ngAfterViewInit() {
    this.discountDataComponent.deleteElement
      .pipe(takeUntil(this.unscriptions$))
      .subscribe(() => this.discountFetchOption$.next(this.currentDiscountOption));

    this.discountDataComponent.pageChange
      .pipe(
        takeUntil(this.unscriptions$),
        map(page => {
          const {pageIndex, pageSize} = page;
          return {
            page: pageIndex,
            size: pageSize
          } as DiscountFetchOption;
        }),
        map(option => this.mergeOption(this.currentDiscountOption, option))
      )
      .subscribe(option => this.discountFetchOption$.next(option));
  }

  mergeOption(ob1: DiscountFetchOption, ob2: DiscountFetchOption) {
    return {...ob1, ...ob2};
  }

  openAddForm() {
    const discountDialog = DiscountFormDialogComponent.open(this.dialog);
    discountDialog.componentInstance.saveSuccess
      .pipe(takeUntil(this.unscriptions$))
      .subscribe(() => this.discountFetchOption$.next(this.currentDiscountOption));
  }

  onEditSuccess() {
    this.discountFetchOption$.next(this.currentDiscountOption);
  }

  ngOnDestroy(): void {
    this.discountFetchOption$.complete();
    this.unscriptions$.next();
    this.unscriptions$.complete();
  }
}

export class DiscountDataSource extends DataSource<DiscountDataView> {
  private discount = new BehaviorSubject<DiscountDataView[]>([]);

  private page: Page<DiscountView>;

  constructor(private discountService: DiscountService) {
    super();
  }

  static create(dicountService: DiscountService) {
    return new DiscountDataSource(dicountService);
  }

  get totalElements() {
    return this.page ? this.page.totalElements : 0;
  }

  connect(): Observable<DiscountDataView[] | readonly DiscountDataView[]> {
    return this.discount.asObservable();
  }

  disconnect(): void {
    this.discount.complete();
  }

  load(option?: DiscountFetchOption) {
    this.discountService
      .fetchAll(option)
      .pipe(
        tap(page => (this.page = page)),
        map(page => page.content),
        map(discounts => discounts.map(discount => DiscountDataView.of(discount, this.discountService))),
      )
      .subscribe(discounts => this.discount.next(discounts));
  }
}

export class DiscountDataView implements DiscountView {

  products: ProductView[] = [];

  constructor(
    public id: number,
    public discountType: DiscountType,
    public discountValue: number,
    public startedTime: Date,
    public productCount: number,
    private discountService: DiscountService
  ) {
    this.fetchProducts();
  }

  static of(discount: DiscountView, discountService: DiscountService) {
    const {id, discountType, discountValue, startedTime, productCount} = discount;
    return new DiscountDataView(id, discountType, discountValue, startedTime, productCount, discountService);
  }

  fetchProducts() {
    this.discountService.getProductsByDiscountId(this.id)
      .subscribe(products => this.products = products);
  }
}
