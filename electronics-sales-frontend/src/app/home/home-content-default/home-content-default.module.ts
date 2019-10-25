import { MyLazyLoadImageModule } from './../../my-lazy-load-image/my-lazy-load-image.module';
import { HomeBannerComponent } from './../home-banner/home-banner.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeContentDefaultRoutingModule } from './home-content-default-routing.module';
import { HomeContentDefaultComponent } from './home-content-default.component';
import { ShowProductContentComponent } from './show-product-content/show-product-content.component';
import { ProductBoxModule } from '../product-box/product-box.module';


@NgModule({
  declarations: [
    HomeContentDefaultComponent,
    HomeBannerComponent,
    ShowProductContentComponent,
  ],
  imports: [
    CommonModule,
    HomeContentDefaultRoutingModule,
    MyLazyLoadImageModule,
    ProductBoxModule
  ]
})
export class HomeContentDefaultModule { }
