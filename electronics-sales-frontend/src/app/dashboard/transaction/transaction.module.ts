import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PipesModule } from './../../pipes/pipes.module';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionRoutingModule } from './transaction-routing.module';
import { TransactionComponent } from './transaction.component';


@NgModule({
  declarations: [TransactionComponent],
  imports: [
    CommonModule,
    TransactionRoutingModule,
    MaterialModule,
    PipesModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class TransactionModule { }
