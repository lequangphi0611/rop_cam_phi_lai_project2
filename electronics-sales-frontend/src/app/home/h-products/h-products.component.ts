import { FormControl } from '@angular/forms';
import { CategoryView } from './../../models/view-model/category.view.model';
import { Page } from './../../models/page.model';
import { ManufacturerView } from './../../models/view-model/manufacturer.view.model';
import { ManufacturerService } from './../../services/manufacturer.service';
import { ActivatedRoute } from '@angular/router';
import {
  takeUntil,
  filter,
  distinct,
  map,
  switchMap,
  throttleTime,
  debounceTime,
  distinctUntilChanged
} from 'rxjs/operators';
import { ProductView } from './../../models/view-model/product.view.model';
import { FetchProductOption } from './../../models/fetch-product-option.model';
import { BehaviorSubject, Subject, Observable, of, fromEvent } from 'rxjs';
import { ProductService } from './../../services/product.service';
import {
  Component,
  OnInit,
  AfterViewInit,
  OnDestroy,
  ViewChild,
  ElementRef
} from '@angular/core';
import { CategoryService } from 'src/app/services/category.service';
import { FetchProductType } from 'src/app/models/types/fetch-product-type.type';
import { ProductSortType } from 'src/app/models/types/product-sort-type.type';
import { SortType } from 'src/app/models/types/sort-type.type';

const FETCH_OPTIONS = [
  {
    name: 'Mới nhất',
    option: {
      productSortType: ProductSortType.TIME,
      sortType: SortType.DESC
    },
    selected: true
  },
  {
    name: 'Giá từ cao đến thấp',
    option: {
      productSortType: ProductSortType.PRICE,
      sortType: SortType.DESC
    }
  },
  {
    name: 'Giá từ thấp đến cao',
    option: {
      productSortType: ProductSortType.PRICE,
      sortType: SortType.ASC
    }
  },
  {
    name: 'Giảm giá',
    option: {
      fetchProductType: FetchProductType.DISCOUNT
    }
  }
];

@Component({
  selector: 'app-h-products',
  templateUrl: './h-products.component.html',
  styleUrls: ['../home.component.css', './h-products.component.css']
})
export class HProductsComponent implements OnInit, AfterViewInit, OnDestroy {
  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private categoryService: CategoryService,
    private manufacturerService: ManufacturerService
  ) {}

  get manufacturerIdsOption() {
    return this.fetchproductOption.manufacturersId;
  }

  get pageIndex() {
    return this.fetchproductOption.page;
  }

  get pageSize() {
    return this.fetchproductOption.size;
  }

  get maxPage() {
    if (this.maxPageSize <= this.pageSize) {
      return 0;
    }

    return Math.floor(this.maxPageSize / this.pageSize);
  }

  static readonly DEFAULT_PAGE_SIZE = 6;

  static readonly MIN_PAGE_INDEX = 0;

  @ViewChild('fromPriceInput', { static: true }) fromPriceInput: ElementRef;

  @ViewChild('toPriceInput', { static: true }) toPriceInput: ElementRef;

  readonly defaultOption: FetchProductOption = {
    manufacturersId: [],
    size: HProductsComponent.DEFAULT_PAGE_SIZE,
    page: HProductsComponent.MIN_PAGE_INDEX,
    fetchType: FetchProductType.SELLING,
    productSortType: ProductSortType.TIME,
    sortType: SortType.DESC
  };

  fetchProductChange$ = new BehaviorSubject<boolean>(false);

  unscription$ = new Subject();

  products: ProductView[] = [];

  pageIndex$ = new BehaviorSubject<number>(HProductsComponent.MIN_PAGE_INDEX);

  maxPageSize = HProductsComponent.DEFAULT_PAGE_SIZE;

  categoryId$: Observable<number> = of(null);

  manufacturer$: Observable<ManufacturerView[]>;

  category: CategoryView;

  productNameQuery$: Observable<string>;

  fetchproductOption: FetchProductOption = { ...this.defaultOption };

  readonly optionsSelect = FETCH_OPTIONS;

  fetchOptionSelect = new FormControl(this.optionsSelect[0].option);

  ngOnInit() {
    this.fetchProducts();
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

    this.categoryId$.pipe(
      takeUntil(this.unscription$),
      filter(v => v != null),
      switchMap(id => this.categoryService.findById(id))
    ).subscribe(category => this.category = category);

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

    this.fetchOptionSelect
      .valueChanges
      .pipe(
        map(value => {
          const {productSortType, sortType, fetchProductType} = value;
          return {
            fetchType: fetchProductType,
            productSortType,
            sortType,
          } as FetchProductOption;
        }),
        map(option => this.mergeOption(option))
      )
      .subscribe(() => this.fetchProductChange$.next(true));
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
    this.manufacturer$.subscribe(console.log);
  }

  ngAfterViewInit() {

    fromEvent<any>(this.fromPriceInput.nativeElement, 'input')
      .pipe(
        map(event => event.target.value),
        debounceTime(1000),
        distinctUntilChanged()
      )
      .subscribe(fromPrice => {
        this.fetchproductOption.fromPrice = fromPrice;
        this.fetchProductChange$.next(true);
      });

    fromEvent<any>(this.toPriceInput.nativeElement, 'input')
      .pipe(
        map(event => event.target.value),
        debounceTime(1000),
        distinctUntilChanged()
      )
      .subscribe(toPrice => {
        this.fetchproductOption.toPrice = toPrice;
        this.fetchProductChange$.next(true);
      });

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

  mergeOption(ob: FetchProductOption = {}) {
    this.fetchproductOption = {...this.fetchproductOption, ...ob};
  }

  compareOption(ob1, ob2) {
    const {productSortType: productSortType1, sortType: sortType1, fetchProductType: fetchProductType1} = ob1;
    const {productSortType: productSortType2, sortType: sortType2, fetchProductType: fetchProductType2} = ob2;

    if(fetchProductType1 && fetchProductType2 && fetchProductType1 === fetchProductType2) {
      return true;
    }

    return productSortType1 && productSortType2
      && sortType1 && sortType2
      && productSortType1 === productSortType2
      && sortType1 === sortType2;
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
    this.fetchProductChange$.complete();
  }
}
