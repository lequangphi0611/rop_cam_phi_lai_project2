import { MaterialModule } from './../../material/material.module';
import { PipesModule } from './../../pipes/pipes.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CartRoutingModule } from './cart-routing.module';
import { CartComponent } from './cart.component';
import {LazyLoadImageModule} from 'ng-lazyload-image';


@NgModule({
  declarations: [CartComponent],
  imports: [
    CommonModule,
    CartRoutingModule,
    LazyLoadImageModule,
    PipesModule,
    MaterialModule
  ]
})
export class CartModule { }
