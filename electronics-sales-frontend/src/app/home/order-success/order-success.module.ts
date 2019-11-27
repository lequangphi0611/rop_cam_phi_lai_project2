import { PipesModule } from './../../pipes/pipes.module';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OrderSuccessRoutingModule } from './order-success-routing.module';
import { OrderSuccessComponent } from './order-success.component';


@NgModule({
  declarations: [OrderSuccessComponent],
  imports: [
    CommonModule,
    OrderSuccessRoutingModule,
    MaterialModule,
    PipesModule
  ]
})
export class OrderSuccessModule { }
