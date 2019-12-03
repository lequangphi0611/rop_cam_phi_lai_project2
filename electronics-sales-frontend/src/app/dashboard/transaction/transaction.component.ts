import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { filter, map, takeUntil } from 'rxjs/operators';
import { SortType } from 'src/app/models/types/sort-type.type';
import { Page } from './../../models/page.model';
import { TransactionDataView, TransactionDataViewColumn } from './../../models/view-model/transaction-data.view.model';
import { TransactionFetchOption, TransactionService } from './../../services/transaction.service';
import { TransactionDetailedDialogComponent } from '../transaction-detailed-dialog/transaction-detailed-dialog.component';
import { MatDialog } from '@angular/material/dialog';

const DEFAULT_PAGE_INDEX = 0;

const DEFAULT_PAGE_SIZE = 5;

const DEFAULT_SORT_COLUMN = TransactionDataViewColumn.CREATEDTIME;

const DEFAUT_SORT_TYPE = SortType.DESC;

const DEFAULT_PAGEABLE = {
  page: DEFAULT_PAGE_INDEX,
  size: DEFAULT_PAGE_SIZE,
  sort: DEFAULT_SORT_COLUMN,
  sortDirection: DEFAUT_SORT_TYPE
};

const DEFAULT_OPTION = { pageable: { ...DEFAULT_PAGEABLE }, conditions: {} };

const DISPLAY_COLUMN = [
  'createdTime',
  'fullname',
  'email',
  'phoneNumber',
  'address',
  'subTotal',
  'discountTotal',
  'sumTotal',
  'viewDetail'
];

const FIVE_DAYS_MILISECOND = 1000 * 60 * 60 * 24 * 5;

const DATE_TIME_FORMAT_PATTERN = 'HH:mm dd-MM-yyyy';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  datasource: TransactionDataSource;

  readonly displayedColumns = DISPLAY_COLUMN;

  transactionFetchOption$ = new BehaviorSubject<TransactionFetchOption>(null);

  currentOption: TransactionFetchOption = {};

  unscriptions$ = new Subject();

  fromDateControl = new FormControl();

  toDateControl = new FormControl();

  dateFormat = DATE_TIME_FORMAT_PATTERN;

  constructor(
    private transactionService: TransactionService,
    private dialog: MatDialog
  ) {}

  maxDate: Date;

  ngOnInit() {
    this.datasource = new TransactionDataSource(this.transactionService);
    this.maxDate = this.now;
    this.initOption();
    this.transactionFetchOption$
      .pipe(
        takeUntil(this.unscriptions$),
        filter(option => option != null)
      )
      .subscribe(option => {
        this.currentOption = option;
        this.datasource.load(option);
      });

    this.fromDateControl.valueChanges
      .pipe(takeUntil(this.unscriptions$))
      .subscribe(v => this.proccessOnFromDateControlChange(v));

    this.toDateControl.valueChanges
      .pipe(takeUntil(this.unscriptions$))
      .subscribe(v => this.proccessOnToDateControlChange(v));
  }

  merge(
    ob1: TransactionFetchOption,
    ob2: TransactionFetchOption
  ): TransactionFetchOption {
    return {
      pageable: { ...ob1.pageable, ...ob2.pageable },
      conditions: {
        ...ob1.conditions,
        ...ob2.conditions
      }
    };
  }

  initOption() {
    const now = this.now;
    this.toDateControl.setValue(now);

    const fiveDaysAgo = new Date(now.getTime() - FIVE_DAYS_MILISECOND);
    this.fromDateControl.setValue(fiveDaysAgo);

    const conditions = {
      fromDate: fiveDaysAgo,
      toDate: now
    };
    const option = this.merge(
      { ...DEFAULT_OPTION },
      { pageable: {}, conditions }
    );
    this.transactionFetchOption$.next(option);
  }

  get now() {
    return new Date();
  }

  ngAfterViewInit() {
    this.pageLoadSubcriptions();
  }

  pageLoadSubcriptions() {
    this.paginator.page
      .pipe(
        takeUntil(this.unscriptions$),
        map(p => {
          return {
            pageable: {
              page: p.pageIndex,
              size: p.pageSize
            },
            conditions: {}
          };
        }),
        map(fetchOption => this.merge(this.currentOption, fetchOption))
      )
      .subscribe(option => this.transactionFetchOption$.next(option));
  }

  trackById(index: number, item: TransactionDataView) {
    return item.id;
  }

  onSortChange(event: { active: string; direction: string }) {
    const { active, direction } = event;

    const pageable: any = {};

    if (active && direction) {
      pageable.sort = TransactionDataViewColumn[active.toUpperCase()];
      pageable.sortDirection = SortType[direction.toUpperCase()];
    }

    const optionBySort: TransactionFetchOption = {
      pageable,
      conditions: {}
    };
    const optionMerged = this.merge(this.currentOption, optionBySort);
    this.transactionFetchOption$.next(optionMerged);
  }

  proccessOnToDateControlChange(value: Date) {
    const option = {
      pageable: {},
      conditions: {
        toDate: value
      }
    };
    this.transactionFetchOption$.next(this.merge(this.currentOption, option));
  }

  proccessOnFromDateControlChange(value: Date) {
    const option = {
      pageable: {},
      conditions: {
        fromDate: value
      }
    };
    this.transactionFetchOption$.next(this.merge(this.currentOption, option));
  }

  openDetailTransaction(id: number) {
    TransactionDetailedDialogComponent.open(this.dialog, {transactionId: id});
  }

  ngOnDestroy(): void {
    this.transactionFetchOption$.complete();
    this.unscriptions$.next();
    this.unscriptions$.complete();
  }
}

export class TransactionDataSource extends DataSource<TransactionDataView> {
  private transactionDatas = new BehaviorSubject<TransactionDataView[]>([]);

  private page: Page<TransactionDataView>;

  constructor(private transactionService: TransactionService) {
    super();
  }

  connect(
    collectionViewer: CollectionViewer
  ): Observable<TransactionDataView[] | readonly TransactionDataView[]> {
    return this.transactionDatas.asObservable();
  }

  load(option?: TransactionFetchOption) {
    this.transactionService.fetchAll(option).subscribe(page => {
      this.page = page;
      this.transactionDatas.next(page.content);
    });
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.transactionDatas.complete();
  }

  get totalElement() {
    return this.empty ? 0 : this.page.totalElements;
  }

  get empty() {
    return !this.page || this.page.empty;
  }

  get next() {
    return !this.empty && !this.page.last;
  }

  get last() {
    return !this.empty && !this.page.first;
  }
}
