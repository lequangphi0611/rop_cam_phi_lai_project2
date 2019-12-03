import { tap } from 'rxjs/operators';
import { TransactionService } from './../../services/transaction.service';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogConfig
} from '@angular/material/dialog';
import { Component, OnInit, Inject, Input } from '@angular/core';
import { TransactionDetailedView } from 'src/app/models/view-model/transaction-detailed.view';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-transaction-detailed-dialog',
  templateUrl: './transaction-detailed-dialog.component.html',
  styleUrls: ['./transaction-detailed-dialog.component.css']
})
export class TransactionDetailedDialogComponent implements OnInit {

  @Input() title = 'Thông tin đơn hàng';

  datasource = new MatTableDataSource<TransactionDetailedView>();

  constructor(
    private dialogRef: MatDialogRef<TransactionDetailedDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data,
    private transactionService: TransactionService
  ) {}

  get transactionId() {
    return this.data ? this.data.transactionId : null;
  }

  static open(
    dialog: MatDialog,
    data: { transactionId: number },
    config: MatDialogConfig = {}
  ): MatDialogRef<TransactionDetailedDialogComponent> {
    return dialog.open(TransactionDetailedDialogComponent, {
      ...config,
      data
    });
  }



  ngOnInit() {
    this.transactionService.fetchTransactionDetailed(this.transactionId)
      .subscribe(transactionDetaileds => this.datasource.data = transactionDetaileds);
  }
}
