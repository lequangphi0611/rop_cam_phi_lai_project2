import { PipesModule } from 'src/app/pipes/pipes.module';
import { MaterialModule } from './../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionDetailedDataComponent } from './transaction-detailed-data.component';



@NgModule({
  declarations: [TransactionDetailedDataComponent],
  imports: [
    CommonModule,
    MaterialModule,
    PipesModule
  ],
  exports: [TransactionDetailedDataComponent]
})
export class TransactionDetailedDataModule { }
