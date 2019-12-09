import { map } from 'rxjs/operators';
import { MatPaginator } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { FormGroup, FormControl } from '@angular/forms';
import {
  StatisticalService,
  StatisticalOption
} from './../../../services/statistical.service';
import { RevenueStatisticalDataSource } from './revenue-statistical-datasource';
import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import {StatisticalType} from 'src/app/models/types/statistical.type';
import * as Statistical from 'src/app/models/types/statistical.type';
import { BehaviorSubject, combineLatest } from 'rxjs';

const DISPLAYED_COLUMN = [
  'createdTime',
  'minRevenue',
  'maxRevenue',
  'totalRevenue'
];

const DEFAULT_STATISTICAL_OPTION: StatisticalOption = {
  page: 0,
  size: 5
};

const DEFAULT_STATISTICAL_TYPE = StatisticalType.DAY;

@Component({
  selector: 'app-revenue-statistical',
  templateUrl: './revenue-statistical.component.html',
  styleUrls: ['./revenue-statistical.component.css']
})
export class RevenueStatisticalComponent implements OnInit, AfterViewInit {
  dataSource: RevenueStatisticalDataSource;

  displayedColumns = DISPLAYED_COLUMN;

  statisticalTypes = Statistical.values;

  private statisticalOption = new BehaviorSubject<StatisticalOption>({
    ...DEFAULT_STATISTICAL_OPTION
  });

  private statisticaltype = new BehaviorSubject<StatisticalType>(
    DEFAULT_STATISTICAL_TYPE
  );

  statisticalTypeControl = new FormControl(StatisticalType.DAY);

  @ViewChild(MatPaginator, { static : true}) paginator: MatPaginator;

  constructor(private statisticalService: StatisticalService) {}

  ngOnInit() {
    this.dataSource = new RevenueStatisticalDataSource(this.statisticalService);

    combineLatest(this.statisticalOption, this.statisticaltype).subscribe(
      option =>
        this.dataSource.load({
          pageable: option[0],
          statisticalType: option[1]
        })
    );

    this.statisticalTypeControl.valueChanges
        .subscribe(statisticalType => this.statisticaltype.next(statisticalType));
  }

  ngAfterViewInit(): void {
    this.paginator.page
      .pipe(
        map(page => {
          return {
            page: page.pageIndex,
            size: page.pageSize
          };
        }),
        map(option => this.mergeOption(option))
      )
      .subscribe(option => this.statisticalOption.next(option));
  }

  format(year: number, month?: number, day?: number): string {
    return `${day ? `${this.formatNumber(day)}-` : ''}${
      month ? `${this.formatNumber(month)}-` : ''
    }${year}`;
  }

  formatNumber(num: number): string {
    return num < 10 ? `0${num}` : `${num}`;
  }

  get timeHeaderName() {
    return this.getStatisticalTypeName(this.statisticaltype.value);
  }

  getStatisticalTypeName(statisticalType: StatisticalType) {
    switch (statisticalType) {
      case StatisticalType.DAY:
        return `Ngày`;

      case StatisticalType.MONTH:
        return `Tháng`;

      default:
        return 'Năm';
    }
  }

  mergeOption(option: StatisticalOption) {
    const oldOption = this.statisticalOption.value;
    return {...oldOption, ...option};
  }

  onSortChange(event) {
    const option: StatisticalOption = {
      sort: event.active,
      direction: event.direction
    };
    if (!option.direction || option.sort === 'createdTime') {
      option.sort = '';
      option.direction = '';
    }
    this.statisticalOption.next(this.mergeOption(option));
  }
}
