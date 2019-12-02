import { ProductView } from 'src/app/models/view-model/product.view.model';
import { tap } from 'rxjs/operators';
import { Page } from './../models/page.model';
import { SortType } from 'src/app/models/types/sort-type.type';
import { DiscountView } from './../models/view-model/discount.view';
import { Observable } from 'rxjs';
import { DiscountDto } from './../models/dtos/discount.dto';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { formatDate } from '@angular/common';


const DISCOUNT_URL = '/api/discounts';

export const DEFAULT_DISCOUNT_FETCH_OPTION: DiscountFetchOption = {

  page : 0,

  size: 5,

  sort: 'started_time',

  sortDirection: SortType.DESC,

};

const DATE_FORMAT_PATTERN = 'yyyy-MM-dd';

const LOCALE = 'en-US';

@Injectable({
  providedIn: 'root'
})
export class DiscountService {

  constructor(private http: HttpClient) { }

  create(discount: DiscountDto): Observable<DiscountView> {
    return this.http.post<DiscountView>(DISCOUNT_URL, discount);
  }

  update(discount: DiscountDto): Observable<DiscountView> {
    const {id} = discount;
    return this.http.put<DiscountView>(`${DISCOUNT_URL}/${id}`, discount);
  }

  getById(id: number): Observable<DiscountView> {
    return this.http.get<DiscountView>(`${DISCOUNT_URL}/${id}`);
  }

  fetchAll(option: DiscountFetchOption = {}): Observable<Page<DiscountView>> {
    option = {...DEFAULT_DISCOUNT_FETCH_OPTION, ...option};
    const {page, size, sort, sortDirection, fromDate, toDate} = option;
    const params: any = {
      page: page.toString(),
      size: size.toString(),
      sort: `${sort},${sortDirection}`
    };

    if (fromDate) {
      params.fromDate = formatDate(fromDate, DATE_FORMAT_PATTERN, LOCALE);
    }

    if (toDate) {
      params.toDate = formatDate(toDate, DATE_FORMAT_PATTERN, LOCALE);
    }

    return this.http.get<Page<DiscountView>>(DISCOUNT_URL, {
      params
    }).pipe(tap(v => v.content.map(e => e.startedTime = new Date(e.startedTime))));
  }

  getProductsByDiscountId(id: number): Observable<ProductView[]> {
    return this.http.get<ProductView[]>(`${DISCOUNT_URL}/${id}/products`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${DISCOUNT_URL}/${id}`);
  }

}

export interface DiscountFetchOption {

  page?: number;

  size?: number;

  sort?: string;

  sortDirection?: SortType;

  fromDate?: Date;

  toDate?: Date;

}
