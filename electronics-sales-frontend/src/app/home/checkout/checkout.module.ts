import { PipesModule } from './../../pipes/pipes.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CheckoutRoutingModule } from './checkout-routing.module';
import { CheckoutComponent } from './checkout.component';
import { CheckoutInfomationComponent } from './checkout-infomation/checkout-infomation.component';
import { CheckoutItemsComponent } from './checkout-items/checkout-items.component';


@NgModule({
  declarations: [CheckoutComponent, CheckoutInfomationComponent, CheckoutItemsComponent],
  imports: [
    CommonModule,
    CheckoutRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    PipesModule
  ]
})
export class CheckoutModule { }
