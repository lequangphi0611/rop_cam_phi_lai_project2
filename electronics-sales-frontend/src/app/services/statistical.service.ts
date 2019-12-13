import { ProductsStatisticalView } from './../models/view-model/product-statistical.view';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { StatisticalType } from '../models/types/statistical.type';
import { CategoryStatistical } from '../models/view-model/category-statistical.model';
import { ProductStatisticalView } from '../models/view-model/product-statistical';
import { RevenueMonthView } from './../models/view-model/revenue-month.view.model';
import { RevenueStatisticalView } from './../models/view-model/revenue-statistical.model';
import { Pageable } from '../models/pageable';
import { ImportInvoiceReportView } from '../models/view-model/import-invoice-report.view.model';
import { tap } from 'rxjs/operators';
import { formatDate } from '@angular/common';

const STATISTICAL_URL = '/api/statisticals';

const FORMAT_DATE_PATTERN = 'yyyy-MM-dd';

const LOCALE = 'en-US';

export interface StatisticalOption  {
  page?: number;

  size?: number;

  sort?: string;

  direction?: string;
}

export interface ImportInvoiceReportOption extends Pageable {


  fromDate?: Date;

  toDate?: Date;

}

const DEFAULT_STATISTICAL_OPTION: StatisticalOption = {
  page: 0,
  size: 10,
  direction: 'DESC'
};

@Injectable({
  providedIn: 'root'
})
export class StatisticalService {
  constructor(private http: HttpClient) {}

  getCategoryStatistical(): Observable<CategoryStatistical> {
    return this.http.get<CategoryStatistical>(`${STATISTICAL_URL}/category`);
  }

  getRevenueProductStatistical(top = 5): Observable<ProductStatisticalView[]> {
    return this.http.get<ProductStatisticalView[]>(
      `${STATISTICAL_URL}/revenue-product`,
      {
        params: new HttpParams().set('top', top.toString())
      }
    );
  }

  getRevenueOverMonthStatistical(): Observable<RevenueMonthView[]> {
    return this.http.get<RevenueMonthView[]>(
      `${STATISTICAL_URL}/revenue-month`
    );
  }

  getRevenueStatisticalBy(
    option: StatisticalOption = {},
    statisticalType: StatisticalType = StatisticalType.DAY
  ): Observable<Page<RevenueStatisticalView>> {
    option = {...DEFAULT_STATISTICAL_OPTION, ...option};
    const {page, size, sort, direction} = option;
    const params: any = {page, size};
    if (sort) {
      params.sort = `${sort},${direction}`;
    }
    return this.http.get<Page<RevenueStatisticalView>>(
      `${STATISTICAL_URL}/revenue`,
      {
        params: {
          statisticalType: statisticalType.toString(),
          ...params
        }
      }
    );
  }

  getImportInvoceReports(option: ImportInvoiceReportOption = {}) {
    const { page, size, sort, direction, fromDate, toDate } = option;
    const params: any = { page, size };
    if (sort) {
      params.sort = `${sort},${direction || 'ASC'}`;
    }

    params.fromDate = formatDate(fromDate, FORMAT_DATE_PATTERN, LOCALE) || '';
    params.toDate = formatDate(toDate, FORMAT_DATE_PATTERN, LOCALE) || '';
    return this.http
      .get<Page<ImportInvoiceReportView>>(
        `${STATISTICAL_URL}/import-invoice-reports`,
        {
          params
        }
      )
      .pipe(
        tap(result =>
          result.content.forEach(
            value => (value.importTime = new Date(value.importTime))
          )
        )
      );
  }

  getProductStatistical(pageable: Pageable): Observable<Page<ProductsStatisticalView>> {
    const {page, size, sort, direction} = pageable;

    const params: any = {
      page: page ? page : '',
      size: size ? size : '',
      sort : sort && direction ? `${sort},${direction}` : ''
    };
    return this.http.get<Page<ProductsStatisticalView>>(`${STATISTICAL_URL}/product`,
      {params}
    );
  }
}
