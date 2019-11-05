import { map, catchError } from 'rxjs/operators';
import { ProductParameterView } from './../models/view-model/product-parameter.view';
import { Page } from './../models/page.model';
import { FetchProductOption } from './../models/fetch-product-option.model';
import { Observable, from, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProductView } from '../models/view-model/product.view.model';

const AMPERSAND = '&';

const QUESTION_MARK = '?';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private static readonly BASE_REQUEST = `/api/products`;

  constructor(private http: HttpClient) {}

  fetchProduct(option: FetchProductOption): Observable<Page<ProductView>> {
    return this.http
      .get<Page<ProductView>>(this.buildRequestUrlFrom(option))
      .pipe(
        map((productPage: Page<ProductView>) => {
          productPage.content = productPage.content.map(product =>
            ProductView.of(product)
          );
          return productPage;
        })
      );
  }

  existsByName(name: string): Observable<boolean> {
    return this.http
      .head<any>(`/api/products/product-name/${name}`, { observe: 'response' })
      .pipe(
        map(() => true),
        catchError(() => {
          return of(false);
        })
      );
  }

  getProduct(id: number): Observable<ProductView> {
    return this.http
      .get<ProductView>(`${ProductService.BASE_REQUEST}/${id}`)
      .pipe(map(product => ProductView.of(product)));
  }

  getImages(id: number): Observable<string[]> {
    return this.http.get<{ data: string }[]>(`/api/products/${id}/images`).pipe(
      map(datas => {
        const dataStrs = [];
        datas.map(data => data.data).forEach(data => dataStrs.push(data));
        return dataStrs;
      })
    );
  }

  getParameters(productId: number): Observable<ProductParameterView[]> {
    return this.http.get<ProductParameterView[]>(
      `${ProductService.BASE_REQUEST}/${productId}/parameters`
    );
  }

  private buildRequestUrlFrom(option: FetchProductOption): string {
    let builder = `${ProductService.BASE_REQUEST}`;
    const parameters = this.createParametersFrom(option);
    if (parameters.length === 0) {
      return builder;
    }
    builder += `${QUESTION_MARK}${this.parseParameters(parameters)}`;
    return builder;
  }

  private createParametersFrom(option: FetchProductOption): string[] {
    const parameters = [];

    if (option.categoriesId) {
      parameters.push(this.createCategoriesIdParameter(option.categoriesId));
    }

    if (option.manufacturersId) {
      parameters.push(
        this.createManufacturersIdParameter(option.manufacturersId)
      );
    }

    if (option.page >= 0) {
      parameters.push(`p=${option.page}`);
    }

    if (option.size >= 0) {
      parameters.push(`s=${option.size}`);
    }

    if (option.fromPrice >= 0) {
      parameters.push(`fromPrice=${option.fromPrice}`);
    }

    if (option.toPrice >= 0) {
      parameters.push(`toPrice=${option.toPrice}`);
    }

    if (option.productSortType) {
      parameters.push(`productSortType=${option.productSortType}`);
    }

    if (option.sortType) {
      parameters.push(`sortType=${option.sortType}`);
    }

    if (option.search) {
      parameters.push(`search=${option.search}`);
    }

    if (option.fetchType) {
      parameters.push(`fetchType=${option.fetchType}`);
    }

    if (option.fetchDiscount != null) {
      parameters.push(`fetchDiscount=${option.fetchDiscount}`);
    }
    return parameters;
  }

  private createCategoriesIdParameter(categoriesId: number[]): string {
    const categoriesIdParameterArray = categoriesId.map(
      categoryId => `categoriesId=${categoryId}`
    );
    return this.parseParameters(categoriesIdParameterArray);
  }

  private createManufacturersIdParameter(manufacturersId: number[]): string {
    const manufacturersIdParameterArray = manufacturersId.map(
      manufacturerId => `manufacturersId=${manufacturerId}`
    );
    return this.parseParameters(manufacturersIdParameterArray);
  }

  private parseParameters(parameters: any[]): string {
    let builder = '';
    parameters.forEach((parameter, index) => {
      if (index > 0) {
        builder += AMPERSAND;
      }
      builder += `${parameter}`;
    });
    return builder;
  }
}
