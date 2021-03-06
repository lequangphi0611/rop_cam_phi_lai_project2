import { CategoryProductReceiver } from './../models/category-and-product-receive.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { CategoryDto } from '../models/dtos/category.dto';
import { ParameterTypeDto } from './../models/dtos/paramter-type.dto';
import { ManufacturerView } from './../models/view-model/manufacturer.view.model';
import { hostViewClassName } from '@angular/compiler';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  static readonly BASE_REQUEST = '/api/categories';

  constructor(private http: HttpClient) {}

  fetchCategories(): Observable<CategoryView[]> {
    return this.http.get<CategoryView[]>(`${CategoryService.BASE_REQUEST}`);
  }

  fetchCategoriesProducts(discountId?: number): Observable<CategoryProductReceiver[]> {
    const params: any = {};
    if (discountId) {
      params.discountId = discountId;
    }
    return this.http.get<CategoryProductReceiver[]>(`${CategoryService.BASE_REQUEST}/group/products`, {
      params
    });
  }

  fetchCategoriesHasProductSellable(): Observable<CategoryView[]> {
    return this.http.get<CategoryView[]>(`${CategoryService.BASE_REQUEST}/products-sellable`);
  }

  create(category: CategoryDto): Observable<CategoryView> {
    return this.http.post<CategoryView>(
      `${CategoryService.BASE_REQUEST}`,
      category
    );
  }

  update(category: CategoryDto): Observable<CategoryView> {
    const { id } = category;
    return this.http.put<CategoryView>(
      `${CategoryService.BASE_REQUEST}/${id}`,
      category
    );
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${CategoryService.BASE_REQUEST}/${id}`);
  }

  findById(id: number): Observable<CategoryView> {
    return this.http.get<CategoryView>(`${CategoryService.BASE_REQUEST}/${id}`);
  }

  fetchCategoriesBy(
    page = 0,
    size?: number,
    sortBy?: string,
    sortType = 'asc'
  ) {
    return this.http.get<CategoryView[]>('/api/categories', {
      params: new HttpParams()
        .set('fetchType', 'full')
        .set('page', page.toString())
        .set('size', !size ? '' : size.toString())
        .set('sortBy', sortBy)
        .set('sortType', sortType),
    });
  }

  getManufacturersBy(categoryId: number): Observable<ManufacturerView[]> {
    return this.http.get<ManufacturerView[]>(
      `${CategoryService.BASE_REQUEST}/${categoryId}/manufacturers`
    );
  }

  getParameterTypesBy(categoryId: number): Observable<ParameterTypeDto[]> {
    return this.http.get<ParameterTypeDto[]>(
      `${CategoryService.BASE_REQUEST}/${categoryId}/parameter-types`
    );
  }

  getChildrens(categoryId: number): Observable<CategoryView[]> {
    return this.http.get<CategoryView[]>(
      `${CategoryService.BASE_REQUEST}/${categoryId}/childrens`
    );
  }


  existsByName(categoryName: string): Observable<boolean> {
    return this.http.head<any>(`${CategoryService.BASE_REQUEST}/categoryName/${categoryName}`)
      .pipe(
        map(() => true),
        catchError(() => of(false))
      );
  }
}
