import { CategoryService } from './../../services/category.service';
import { FetchProductOption } from './../../models/fetch-product-option.model';
import { map } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FetchProductType } from 'src/app/models/types/fetch-product-type.type';
import { ProductSortType } from 'src/app/models/types/product-sort-type.type';

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

  readonly basicOption: FetchProductOption = {
    page: 0,
    size: 4,
    fetchType: FetchProductType.ALL,
    productSortType: ProductSortType.TIME
  };

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.subCriptionProductBanners();
  }

  subCriptionProductBanners(): void {
    this.subcription = this.categoryService.fetchCategories()
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
