import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { map, takeUntil, tap } from 'rxjs/operators';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { CategoryService } from 'src/app/services/category.service';
import { CollectionViewer } from '@angular/cdk/collections';
import { Observable, BehaviorSubject, Subject, of } from 'rxjs';
import { DataSource } from '@angular/cdk/table';
import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  AfterViewInit,
  EventEmitter,
  Output,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-category-data-table',
  templateUrl: './category-data-table.component.html',
  styleUrls: ['./category-data-table.component.css'],
})
export class CategoryDataTableComponent
  implements OnInit, OnDestroy, AfterViewInit {
  dataSoure: CategoryDataSource;

  unscription$ = new Subject<any>();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  @Output() editClicked: EventEmitter<CategoryDataView>;

  readonly defaultDataSourceOption = {
    page: 0,
    size: 5,
    sortBy: 'id',
    sortType: 'desc',
  };

  displayedColumns = [
    'categoryName',
    'productCount',
    'manufacturer',
    'parameterType',
    'edit',
    'delete',
  ];

  maxPageSize = 0;

  constructor(
    private categoryService: CategoryService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) {
    this.editClicked = new EventEmitter(true);
  }

  ngOnInit() {
    this.dataSoure = new CategoryDataSource(this.categoryService);
    this.dataSoure.load(this.defaultDataSourceOption);
    this.categoryService
      .fetchCategoriesBy()
      .pipe(
        takeUntil(this.unscription$),
        map(v => v.length)
      )
      .subscribe(r => (this.maxPageSize = r));
  }

  ngAfterViewInit() {
    this.paginator.page
      .pipe(
        takeUntil(this.unscription$),
        tap(() => this.loadCategoriesByPaginator())
      )
      .subscribe();
  }

  loadCategoriesByPaginator(): void {
    this.dataSoure.load({
      page: this.paginator.pageIndex,
      size: this.paginator.pageSize,
      sortBy: 'id',
      sortType: 'desc',
    });
  }

  trackById(index: number, item: CategoryDataView) {
    return item.id;
  }

  delete(id: number) {
    this.categoryService
      .delete(id)
      .pipe(takeUntil(this.unscription$))
      .subscribe(
        () => this.onSuccess(),
        err => this.onError(err)
      );
  }
  onError(err: any): void {
    this.snackbar.open('Xóa không thành công', 'Đóng', { duration: 2000 });
  }
  onSuccess(): void {
    this.snackbar.open('Xóa thành công', 'Đóng', { duration: 2000 });
    this.loadCategoriesByPaginator();
    this.maxPageSize--;
  }

  edit(category: CategoryDataView) {
    this.editClicked.emit(category);
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
  }
}

export class CategoryDataSource extends DataSource<CategoryDataView> {
  private categories = new BehaviorSubject<CategoryDataView[]>([]);

  constructor(private categoryService: CategoryService) {
    super();
  }

  connect(
    collectionViewer: CollectionViewer
  ): Observable<CategoryDataView[] | readonly CategoryDataView[]> {
    return this.categories.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.categories.complete();
  }

  load(args: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortType: string;
  }) {
    const { page, size, sortBy, sortType } = args;
    this.categoryService
      .fetchCategoriesBy(page, size, sortBy, sortType)
      .pipe(
        map(categories =>
          categories.map(category =>
            CategoryDataView.of(category, this.categoryService)
          )
        )
      )
      .subscribe(categoriesData => this.categories.next(categoriesData));
  }
}

export class CategoryDataView {
  parmaterTypes: ParameterTypeDto[];

  manufacturers: ManufacturerView[];

  parentName: string;

  constructor(
    public id: number,
    public categoryName: string,
    private categoryService: CategoryService,
    public productCount: number = 0,
    public parentId?: number
  ) {
    this.categoryService.getParameterTypesBy(this.id)
    .subscribe(v => this.parmaterTypes = v);
    this.categoryService.getManufacturersBy(this.id)
    .subscribe(v => this.manufacturers = v);
    if (parentId) {
      this.categoryService.findById(parentId)
      .pipe(map(v => v.categoryName))
      .subscribe(v => this.parentName = v);
    }
  }

  static of(
    categoryView: CategoryView,
    categoryService: CategoryService
  ): CategoryDataView {
    return new CategoryDataView(
      categoryView.id,
      categoryView.categoryName,
      categoryService,
      categoryView.productCount,
      categoryView.parentId
    );
  }
}
