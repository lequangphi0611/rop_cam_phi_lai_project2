import { CategoryService } from './../../services/category.service';
import { FetchProductOption } from './../../models/fetch-product-option.model';
import { map, filter, tap, finalize } from 'rxjs/operators';
import { Subscription, Subject } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FetchProductType } from 'src/app/models/types/fetch-product-type.type';
import { ProductSortType } from 'src/app/models/types/product-sort-type.type';

export interface ProductBanner {
  id: number;

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

  loading = false;

  readonly basicOption: FetchProductOption = {
    page: 0,
    size: 4,
    fetchType: FetchProductType.SELLING,
    productSortType: ProductSortType.TIME
  };

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.subCriptionProductBanners();
  }

  subCriptionProductBanners(): void {
    this.loading = true;
    this.subcription = this.categoryService.fetchCategoriesHasProductSellable()
      .pipe(
        map(categories => {
          const productBanners: ProductBanner[] = [];
          categories
            .sort((a1, b1) => b1.productCount - a1.productCount)
            .forEach(category =>
            productBanners.push({
              id: category.id,
              title: category.categoryName,
              option: { ...this.basicOption, categoriesId: [category.id] },
            })
          );
          return productBanners;
        }),
        finalize(() => this.loading = false)
      )
      .subscribe(productBanners => (this.productBanners = [...productBanners]));
  }

  ngOnDestroy() {
    this.subcription.unsubscribe();
  }
}
