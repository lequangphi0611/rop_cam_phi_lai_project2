import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { Pageable } from './../../../models/pageable';
import { BehaviorSubject } from 'rxjs';
import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { StatisticalService } from 'src/app/services/statistical.service';
import { ProductStatisticalDataSource } from './product-statistical-datasource';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-product-statistical',
  templateUrl: './product-statistical.component.html',
  styleUrls: ['./product-statistical.component.css']
})
export class ProductStatisticalComponent implements OnInit, AfterViewInit {

  private pageable$ = new BehaviorSubject<Pageable>({
    page: 0,
    size: 5,
    sort: 'productName',
    direction: 'ASC'
  });

  dataSource = new ProductStatisticalDataSource(this.statisticalService);

  displayedColumns = [
    'productName',
    'quantityImport',
    'quantitySold',
    'quantityRemaining',
    'totalDiscount',
    'totalRevenue'
  ];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private statisticalService: StatisticalService) { }

  ngOnInit() {
    this.pageable$.subscribe(pageable => this.dataSource.load(pageable));
  }

  ngAfterViewInit(): void {
    this.paginator
      .page
      .pipe(map(page => {
        return {
          ...this.pageable$.value,
          page: page.pageIndex,
          size: page.pageSize
        };
      }))
      .subscribe(pageable => this.pageable$.next(pageable));

    this.sort
      .sortChange
      .pipe(
        map(sort => {
          return {
            ...this.pageable$.value,
            sort: sort.active,
            direction: sort.direction
          };
        })
      ).subscribe(pageable => this.pageable$.next(pageable));
  }

}
