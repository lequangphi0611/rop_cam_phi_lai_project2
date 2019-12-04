import { ConfirmModule } from './../confirm/confirm.module';
import { CartDataService } from './cart-data.service';
import { StorageServiceModule } from 'ngx-webstorage-service';
import { MaterialModule } from './../material/material.module';
import { HomeHeaderComponent } from './home-header/home-header.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { HomeFooterComponent } from './home-footer/home-footer.component';
import { HomeContentDefaultModule } from './home-content-default/home-content-default.module';
import { LazyLoadImageModule } from 'ng-lazyload-image';
import { SubNavigationComponent } from './home-header/sub-navigation/sub-navigation.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    HomeComponent,
    HomeHeaderComponent,
    HomeFooterComponent,
    SubNavigationComponent,
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    HomeContentDefaultModule,
    LazyLoadImageModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    ConfirmModule
  ],
  providers: [

  ],
})
export class HomeModule {}
