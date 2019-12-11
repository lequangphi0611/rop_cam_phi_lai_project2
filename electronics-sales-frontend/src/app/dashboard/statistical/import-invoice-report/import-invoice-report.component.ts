import { FormControl } from '@angular/forms';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { ImportInvoiceReportOption } from './../../../services/statistical.service';
import { BehaviorSubject, Subject, combineLatest, pipe } from 'rxjs';
import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  AfterViewInit
} from '@angular/core';
import { StatisticalService } from 'src/app/services/statistical.service';
import { ImportInvoiceReportDataSource } from './import-invoice-report-datasource';
import { Pageable } from 'src/app/models/pageable';
import { tap, finalize, map } from 'rxjs/operators';

const DEFAULT_PAGEABLE = {
  page: 0,
  size: 5,
  sort: 'importTime',
  direction: 'DESC'
};

const THIRTY_DAYS_MILISECOND = 1000 * 60 * 60 * 24 * 30;

@Component({
  selector: 'app-import-invoice-report',
  templateUrl: './import-invoice-report.component.html',
  styleUrls: ['./import-invoice-report.component.css']
})
export class ImportInvoiceReportComponent
  implements OnInit, OnDestroy, AfterViewInit {
  dataSource: ImportInvoiceReportDataSource;

  displayedColumns = [
    'importTime',
    'creatorUsername',
    'productName',
    'quantity'
  ];

  private fromDate$ = new Subject<Date>();

  private toDate$ = new Subject<Date>();

  private pageable$ = new Subject<Pageable>();

  private pageable: Pageable;

  private importInvoiceOption$ = new Subject<ImportInvoiceReportOption>();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  fromDateControl = new FormControl();

  toDateControl = new FormControl();

  maxDate: Date;

  constructor(private statisticalService: StatisticalService) {}

  ngOnInit() {
    this.dataSource = new ImportInvoiceReportDataSource(
      this.statisticalService
    );
    this.combineImportInvoiceReportOption();
    this.importInvoiceOption$.subscribe(option => this.dataSource.load(option));
    this.pageable$.subscribe(pageable => (this.pageable = pageable));
    this.combineImportInvoiceReportOption();

    this.initImportInvoiceOptionValue();
  }

  ngAfterViewInit() {
    this.paginator.page
      .pipe(
        map(page => {
          return {
            ...this.pageable,
            page: page.pageIndex,
            size: page.pageSize
          };
        })
      )
      .subscribe(pageable => this.pageable$.next(pageable));

    this.sort.sortChange
      .pipe(
        map(sort => {
          return {
            ...this.pageable,
            sort: sort.active,
            direction: sort.direction
          } as Pageable;
        }),
        tap(pageable => {
          const { sort, direction } = pageable;
          if (!sort || direction === '') {
            pageable.sort = DEFAULT_PAGEABLE.sort;
            pageable.direction = DEFAULT_PAGEABLE.direction;
          }
        })
      )
      .subscribe(pageable => this.pageable$.next(pageable));

    this.toDateControl.valueChanges.subscribe(value =>
      this.toDate$.next(value)
    );
    this.fromDateControl.valueChanges.subscribe(value =>
      this.fromDate$.next(value)
    );
  }

  combineImportInvoiceReportOption() {
    combineLatest(this.toDate$, this.fromDate$, this.pageable$).subscribe(
      ([toDate, fromDate, pageable]) => {
        this.importInvoiceOption$.next({
          ...pageable,
          fromDate,
          toDate
        });
      }
    );
  }

  initImportInvoiceOptionValue() {
    const toDate = this.now;
    this.maxDate = toDate;
    this.toDate$.next(toDate);
    this.toDateControl.setValue(toDate);
    const fromDate = new Date(toDate.getTime() - THIRTY_DAYS_MILISECOND);
    this.fromDate$.next(fromDate);
    this.fromDateControl.setValue(fromDate);
    this.pageable$.next({ ...DEFAULT_PAGEABLE });
  }

  get now() {
    return new Date();
  }

  ngOnDestroy(): void {
    this.fromDate$.complete();
    this.toDate$.complete();
    this.pageable$.complete();
  }
}
