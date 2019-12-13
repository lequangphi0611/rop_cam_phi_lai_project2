import { TransactionDetailedDialogModule } from './../../../dashboard/transaction-detailed-dialog/transaction-detailed-dialog.module';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { MaterialModule } from './../../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionHistoryRoutingModule } from './transaction-history-routing.module';
import { TransactionHistoryComponent } from './transaction-history.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [TransactionHistoryComponent],
  imports: [
    CommonModule,
    TransactionHistoryRoutingModule,
    MaterialModule,
    PipesModule,
    FormsModule,
    ReactiveFormsModule,
    TransactionDetailedDialogModule
  ]
})
export class TransactionHistoryModule { }
