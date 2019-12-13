import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { SortType } from './../../../models/types/sort-type.type';
import { Pageable } from 'src/app/models/pageable';
import { BehaviorSubject, Subject, combineLatest } from 'rxjs';
import { TransactionDataView, TransactionDataViewColumn } from 'src/app/models/view-model/transaction-data.view.model';
import { FormControl } from '@angular/forms';
import { UserAuthenticatedService } from './../../../services/user-authenticated.service';
import { TransactionService, TransactionFetchOption } from './../../../services/transaction.service';
import { TransactionHistoryDataSource } from './transaction-history-datasource';
import { Component, OnInit, OnDestroy, ViewChild, AfterViewInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { TransactionDetailedDialogComponent } from 'src/app/dashboard/transaction-detailed-dialog/transaction-detailed-dialog.component';

const DISPLAY_COLUMN = [
  'createdTime',
  'fullname',
  // 'email',
  'phoneNumber',
  'address',
  'subTotal',
  'discountTotal',
  'sumTotal',
  'viewDetail'
];

const FIVE_DAYS_MILISECOND = 1000 * 60 * 60 * 24 * 30;

const DATE_TIME_FORMAT_PATTERN = 'HH:mm dd-MM-yyyy';

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css']
})
export class TransactionHistoryComponent implements OnInit, OnDestroy, AfterViewInit {
  dataSource = new TransactionHistoryDataSource(
    this.transactionService,
    this.userAuthenticatedService
  );

  pageable$ = new BehaviorSubject<Pageable>({
    page: 0,
    size: 5,
    sort: 'createdTime',
    direction: 'DESC'
  });

  dateFormat = DATE_TIME_FORMAT_PATTERN ;

  displayedColumns = DISPLAY_COLUMN;

  @ViewChild(MatPaginator, {static : true}) paginator: MatPaginator;

  constructor(
    private transactionService: TransactionService,
    private userAuthenticatedService: UserAuthenticatedService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.subcritionOption();
  }

  trackById(index: number, item: TransactionDataView) {
    return item.id;
  }

  subcritionOption() {
    this.pageable$
      .subscribe((pageable) => {
        this.dataSource.load({
          pageable: {
            page: pageable.page,
            size: pageable.size,
            sort: TransactionDataViewColumn[pageable.sort.toUpperCase()],
            sortDirection: SortType[pageable.direction.toUpperCase()]
          }
        });
      });
  }

  ngAfterViewInit(): void {
  }

  onSortChange({active: sort, direction}) {
    if (!direction) {
      return;
    }
    this.pageable$.next({
      ...this.pageable$.value,
      sort,
      direction
    });
  }

  onPageChange({pageIndex: page, pageSize: size}) {
    this.pageable$.next({
      ...this.pageable$.value,
      page,
      size
    });
  }

  openTransactionDetailed(transactionId) {
    TransactionDetailedDialogComponent.open(this.dialog, {
      transactionId
    });
  }

  get now() {
    return new Date();
  }

  ngOnDestroy(): void {
    this.pageable$.complete();
  }
}
