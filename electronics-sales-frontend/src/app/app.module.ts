import { StatisticalService } from './services/statistical.service';
import { TransactionService } from './services/transaction.service';
import { UserInfoService } from './services/user-info.service';
import { CartDataService } from './home/cart-data.service';
import { ManufacturerService } from './services/manufacturer.service';
import { CurrencyVNPipe } from './pipes/currency-vn.pipe';
import { CategoryService } from './services/category.service';
import { UserAuthenticatedService } from './services/user-authenticated.service';
import { AuthInterceptorService } from './auth/auth-interceptor.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { CookieService } from 'ngx-cookie-service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProductService } from './services/product.service';
import { MAT_DATE_LOCALE } from '@angular/material/core';

import { StorageServiceModule } from 'ngx-webstorage-service';
import { DiscountService } from './services/discount.service';

@NgModule({
  declarations: [AppComponent],
  entryComponents: [],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true,
    },
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'vi-VN'
    },
    CategoryService,
    AuthInterceptorService,
    UserAuthenticatedService,
    DiscountService,
    ProductService,
    ManufacturerService,
    StorageServiceModule,
    CartDataService,
    UserInfoService,
    TransactionService,
    StatisticalService
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
