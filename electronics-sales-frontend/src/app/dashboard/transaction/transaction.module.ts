import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PipesModule } from './../../pipes/pipes.module';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionRoutingModule } from './transaction-routing.module';
import { TransactionComponent } from './transaction.component';
import { TransactionDetailedDialogModule } from '../transaction-detailed-dialog/transaction-detailed-dialog.module';


@NgModule({
  declarations: [TransactionComponent],
  imports: [
    CommonModule,
    TransactionRoutingModule,
    MaterialModule,
    PipesModule,
    FormsModule,
    ReactiveFormsModule,
    TransactionDetailedDialogModule
  ]
})
export class TransactionModule { }
