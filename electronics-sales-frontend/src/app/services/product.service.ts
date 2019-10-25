import { Page } from './../models/page.model';
import { FetchProductOption } from './../models/fetch-product-option.model';
import { Observable, from } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProductView } from '../models/view-model/product.view.model';

const AMPERSAND = '&';

const QUESTION_MARK = '?';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  fetchProduct(option: FetchProductOption): Observable<Page<ProductView>> {
    return this.http
      .get<Page<ProductView>>(this.buildRequestUrlFrom(option));
  }

  private buildRequestUrlFrom(option: FetchProductOption): string {
    let builder = `/api/products`;
    const parameters = this.createParametersFrom(option);
    if (parameters.length === 0) {
      return builder;
    }
    builder += `${QUESTION_MARK}${this.parseParameters(parameters)}`;
    console.log({builder});
    return builder;
  }

  private createParametersFrom(option: FetchProductOption): string[] {
    const parameters = [];

    if (option.categoriesId) {
      parameters.push(this.createCategoriesIdParameter(option.categoriesId));
    }

    if (option.manufacturersId) {
      parameters.push(this.createManufacturersIdParameter(option.manufacturersId));
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
    const categoriesIdParameterArray = categoriesId
          .map(categoryId => `categoriesId=${categoryId}`);
    return this.parseParameters(categoriesIdParameterArray);
  }

  private createManufacturersIdParameter(manufacturersId: number[]): string {
    const manufacturersIdParameterArray = manufacturersId
      .map(manufacturerId => `manufacturersId=${manufacturerId}`);
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
