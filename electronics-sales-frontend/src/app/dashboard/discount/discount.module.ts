import { PipesModule } from './../../pipes/pipes.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DiscountRoutingModule } from './discount-routing.module';
import { DiscountComponent } from './discount.component';
import { DiscountFormDialogComponent } from './discount-form-dialog/discount-form-dialog.component';


@NgModule({
  declarations: [DiscountComponent, DiscountFormDialogComponent],
  imports: [
    CommonModule,
    DiscountRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    PipesModule
  ],
  entryComponents: [DiscountFormDialogComponent]
})
export class DiscountModule { }
