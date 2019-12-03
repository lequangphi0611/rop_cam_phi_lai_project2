import { MatSort } from '@angular/material/sort';
import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
import { TransactionDetailedView } from '../models/view-model/transaction-detailed.view';
import { MatTableDataSource } from '@angular/material/table';

const DISPLAY_COLUMNS = [
  'productName',
  'productPrice',
  'quantity',
  'subTotal',
  'discount',
  'total'
];

@Component({
  selector: 'app-transaction-detailed-data',
  templateUrl: './transaction-detailed-data.component.html',
  styleUrls: ['./transaction-detailed-data.component.css']
})
export class TransactionDetailedDataComponent implements OnInit, AfterViewInit {

  @Input() datasource: MatTableDataSource<TransactionDetailedView>;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  displayColumns = DISPLAY_COLUMNS;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    this.datasource.sort = this.sort;
  }

  get data(): TransactionDetailedView[] {
    return !this.datasource ? null : this.datasource.data;
  }

  get total() {
    if (!this.data || this.data.length === 0) {
      return 0;
    }

    return this.data.reduce((total, current) => total + current.total, 0);
  }

}
