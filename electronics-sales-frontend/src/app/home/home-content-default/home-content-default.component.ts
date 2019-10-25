import { FetchProductOption } from './../../models/fetch-product-option.model';
import { map } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CategoryService } from 'src/app/services/category.service';

export interface ProductBanner {
  title: string;

  option: FetchProductOption;
}
@Component({
  selector: 'app-home-content-default',
  templateUrl: './home-content-default.component.html',
  styleUrls: ['../home.component.css'],
})
export class HomeContentDefaultComponent implements OnInit, OnDestroy {
  productBanners: ProductBanner[];

  subcription: Subscription;

  readonly basicOption: FetchProductOption = { page: 0, size: 4 };

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.subCriptionProductBanners();
  }

  subCriptionProductBanners(): void {
    this.subcription = this.categoryService
      .fetchCategories()
      .pipe(
        map(categories => {
          const productBanners: ProductBanner[] = [];
          categories.forEach(category =>
            productBanners.push({
              title: category.categoryName,
              option: { ...this.basicOption, categoriesId: [category.id] },
            })
          );
          return productBanners;
        })
      )
      .subscribe(productBanners => (this.productBanners = [...productBanners]));
  }

  ngOnDestroy() {
    this.subcription.unsubscribe();
  }
}
