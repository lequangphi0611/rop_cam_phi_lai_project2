import { RatingComponent } from './../rating/rating.component';
import { HomeBannerComponent } from './../home-banner/home-banner.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeContentDefaultRoutingModule } from './home-content-default-routing.module';
import { HomeContentDefaultComponent } from './home-content-default.component';
import {LazyLoadImageModule} from 'ng-lazyload-image';
import { ShowProductContentComponent } from './show-product-content/show-product-content.component';


@NgModule({
  declarations: [
    HomeContentDefaultComponent,
    HomeBannerComponent,
    RatingComponent,
    ShowProductContentComponent,
  ],
  imports: [
    CommonModule,
    HomeContentDefaultRoutingModule,
    LazyLoadImageModule
  ]
})
export class HomeContentDefaultModule { }
