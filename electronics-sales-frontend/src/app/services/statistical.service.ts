import { RevenueMonthView } from './../models/view-model/revenue-month.view.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CategoryStatistical } from '../models/view-model/category-statistical.model';
import { ProductStatisticalView } from '../models/view-model/product-statistical';

const STATISTICAL_URL = '/api/statisticals';

@Injectable({
  providedIn: 'root'
})
export class StatisticalService {

  constructor(private http: HttpClient) { }

  getCategoryStatistical(): Observable<CategoryStatistical> {
    return this.http.get<CategoryStatistical>(`${STATISTICAL_URL}/category`);
  }

  getRevenueProductStatistical(top = 5): Observable<ProductStatisticalView[]> {
    return this.http.get<ProductStatisticalView[]>(`${STATISTICAL_URL}/revenue-product`, {
      params: new HttpParams()
        .set('top', top.toString()),
    });
  }

  getRevenueOverMonthStatistical(): Observable<RevenueMonthView[]> {
    return this.http.get<RevenueMonthView[]>(`${STATISTICAL_URL}/revenue-month`);
  }
}
