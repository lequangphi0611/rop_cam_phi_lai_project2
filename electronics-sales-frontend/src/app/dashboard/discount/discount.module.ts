import { ConfirmModule } from './../../confirm/confirm.module';
import { PipesModule } from './../../pipes/pipes.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DiscountRoutingModule } from './discount-routing.module';
import { DiscountComponent } from './discount.component';
import { DiscountFormDialogComponent } from './discount-form-dialog/discount-form-dialog.component';
import { DiscountDataComponent } from './discount-data/discount-data.component';


@NgModule({
  declarations: [DiscountComponent, DiscountFormDialogComponent, DiscountDataComponent],
  imports: [
    CommonModule,
    DiscountRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    PipesModule,
    ConfirmModule
  ],
  entryComponents: [DiscountFormDialogComponent]
})
export class DiscountModule { }
