import { TransactionDetailedDataModule } from './../../transaction-detailed-data/transaction-detailed-data.module';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionDetailedDialogComponent } from './transaction-detailed-dialog.component';



@NgModule({
  declarations: [TransactionDetailedDialogComponent],
  imports: [
    CommonModule,
    MaterialModule,
    PipesModule,
    TransactionDetailedDataModule
  ],
  entryComponents: [TransactionDetailedDialogComponent]
})
export class TransactionDetailedDialogModule { }
