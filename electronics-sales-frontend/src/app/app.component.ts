import { FetchProductOption } from './models/fetch-product-option.model';
import { UserAuthenticatedService } from './services/user-authenticated.service';
import { map } from 'rxjs/operators';
import { UserService } from './services/user.service';
import { Component, OnInit } from '@angular/core';
import { pipe } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { Router, NavigationEnd } from '@angular/router';
import { ProductService } from './services/product.service';
import { ProductSortType } from './models/types/product-sort-type.type';
import { SortType } from './models/types/sort-type.type';
import { FetchProductType } from './models/types/fetch-product-type.type';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  option: FetchProductOption;

  constructor(
    private userAuthService: UserAuthenticatedService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userAuthService.load();
    this.onSubcriptionRouteEvent();
  }

  onSubcriptionRouteEvent() {
    this.router.events.subscribe(event => {
      if (!(event instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });
  }
}
