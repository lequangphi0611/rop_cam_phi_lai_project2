import { CurrencyVNPipe } from './../../pipes/currency-vn.pipe';
import { RatingModule } from './../rating/rating.module';
import { MyLazyLoadImageModule } from './../../my-lazy-load-image/my-lazy-load-image.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductBoxComponent } from './product-box.component';



@NgModule({
  declarations: [ProductBoxComponent, CurrencyVNPipe],
  imports: [
    CommonModule,
    MyLazyLoadImageModule,
    RatingModule
  ],
  exports: [ProductBoxComponent]
})
export class ProductBoxModule { }
