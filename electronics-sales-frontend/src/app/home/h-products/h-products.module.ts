import { MaterialModule } from './../../material/material.module';
import { ProductBoxModule } from './../product-box/product-box.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HProductsRoutingModule } from './h-products-routing.module';
import { HProductsComponent } from './h-products.component';
import { DisplayProductsComponent } from './display-products/display-products.component';


@NgModule({
  declarations: [HProductsComponent, DisplayProductsComponent],
  imports: [
    CommonModule,
    HProductsRoutingModule,
    ProductBoxModule,
    MaterialModule
  ]
})
export class HProductsModule { }
