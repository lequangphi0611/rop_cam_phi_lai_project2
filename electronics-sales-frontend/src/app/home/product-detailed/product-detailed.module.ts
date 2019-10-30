import { LazyLoadImageModule } from 'ng-lazyload-image';
import { CurrencyVNPipe } from './../../pipes/currency-vn.pipe';
import { MyLazyLoadImageModule } from './../../my-lazy-load-image/my-lazy-load-image.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductDetailedRoutingModule } from './product-detailed-routing.module';
import { ProductDetailedComponent } from './product-detailed.component';
import { ProductImagePreviewComponent } from './product-image-preview/product-image-preview.component';
import { PipesModule } from 'src/app/pipes/pipes.module';


@NgModule({
  declarations: [ProductDetailedComponent, ProductImagePreviewComponent],
  imports: [
    CommonModule,
    ProductDetailedRoutingModule,
    MyLazyLoadImageModule,
    PipesModule,
    LazyLoadImageModule
  ]
})
export class ProductDetailedModule { }
