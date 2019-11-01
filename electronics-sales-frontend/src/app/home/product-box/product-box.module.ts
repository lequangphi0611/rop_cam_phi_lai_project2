import { MaterialModule } from './../../material/material.module';
import { RatingModule } from './../rating/rating.module';
import { MyLazyLoadImageModule } from './../../my-lazy-load-image/my-lazy-load-image.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductBoxComponent } from './product-box.component';
import { PipesModule } from 'src/app/pipes/pipes.module';



@NgModule({
  declarations: [ProductBoxComponent],
  imports: [
    CommonModule,
    MyLazyLoadImageModule,
    RatingModule,
    PipesModule,
    MaterialModule
  ],
  exports: [ProductBoxComponent]
})
export class ProductBoxModule { }
