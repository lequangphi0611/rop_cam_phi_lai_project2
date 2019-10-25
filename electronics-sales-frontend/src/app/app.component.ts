import { FetchProductOption } from './models/fetch-product-option.model';
import { UserAuthenticatedService } from './services/user-authenticated.service';
import { map } from 'rxjs/operators';
import { UserService } from './services/user.service';
import { Component } from '@angular/core';
import { pipe } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { ProductService } from './services/product.service';
import { ProductSortType } from './models/types/product-sort-type.type';
import { SortType } from './models/types/sort-type.type';
import { FetchProductType } from './models/types/fetch-product-type.type';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  {

  option: FetchProductOption;

  constructor(private userAuthService: UserAuthenticatedService) {
    this.userAuthService.load();
  }

}
