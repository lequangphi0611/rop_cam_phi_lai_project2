import { ParagraphDto } from './../models/dtos/paragraph.dto';
import { DiscountView } from './../models/view-model/discount.view';
import { ImageView } from './../models/view-model/image-data.view';
import { CategoryView } from './../models/view-model/category.view.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ProductView } from '../models/view-model/product.view.model';
import { ProductDto } from './../models/dtos/product.dto';
import { FetchProductOption } from './../models/fetch-product-option.model';
import { Page } from './../models/page.model';
import { ProductParameterView } from './../models/view-model/product-parameter.view';

const AMPERSAND = '&';

const QUESTION_MARK = '?';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private static readonly BASE_REQUEST = `/api/products`;

  constructor(private http: HttpClient) {}

  fetchProduct(option: FetchProductOption): Observable<ProductView[]> {
    return this.http
      .get<ProductView[]>(this.buildRequestUrlFrom(option))
      .pipe(
        map((products: ProductView[]) =>
          products.map(product => ProductView.of(product, this))
        ),
        catchError(() => of([]))
      );
  }

  parseFormDataFrom(productDto: ProductDto): FormData {
    const formData = new FormData();
    if (productDto.images) {
      productDto.images.forEach(image => formData.append('images', image));
    }
    productDto.images = null;
    formData.append('product', JSON.stringify(productDto));
    return formData;
  }

  createProduct(productDto: ProductDto): Observable<ProductView> {
    const body = this.parseFormDataFrom(productDto);
    return this.http.post<ProductView>(ProductService.BASE_REQUEST, body);
  }

  updateProduct(productDto: ProductDto): Observable<ProductView> {
    if (!productDto || !productDto.id) {
      return throwError('product and id must be not null !');
    }

    const body = this.parseFormDataFrom(productDto);
    return this.http.put<ProductView>(`${ProductService.BASE_REQUEST}/${productDto.id}`, body);
  }

  deleteProduct(productId: number): Observable<any> {
    return this.http.delete(`${ProductService.BASE_REQUEST}/${productId}`);
  }

  getDiscount(productId: number): Observable<DiscountView> {
    return null;
  }

  existsByName(name: string): Observable<boolean> {
    return this.http
      .head<any>(`${ProductService.BASE_REQUEST}/product-name/${name}`, {
        observe: 'response',
      })
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
      .pipe(map(product => ProductView.of(product, this)));
  }

  getImages(id: number): Observable<ImageView[]> {
    return this.http.get<ImageView[]>(`/api/products/${id}/images`);
  }

  getCategories(productId: number): Observable<CategoryView[]> {
    return this.http.get<CategoryView[]>(`${ProductService.BASE_REQUEST}/${productId}/categories`);
  }

  getParameters(productId: number): Observable<ProductParameterView[]> {
    return this.http.get<ProductParameterView[]>(
      `${ProductService.BASE_REQUEST}/${productId}/parameters`
    );
  }

  getDescriptions(productId: number): Observable<ParagraphDto[]> {
    return this.http.get<ParagraphDto[]>(`${ProductService.BASE_REQUEST}/${productId}/descriptions`);
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
