import { tap } from "rxjs/operators";
import { RevenueStatisticalView } from "./../models/view-model/revenue-statistical.model";
import { RevenueMonthView } from "./../models/view-model/revenue-month.view.model";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CategoryStatistical } from "../models/view-model/category-statistical.model";
import { ProductStatisticalView } from "../models/view-model/product-statistical";
import { Page } from "../models/page.model";
import { StatistycalType } from "../models/types/statistical.type";

const STATISTICAL_URL = "/api/statisticals";

export interface StatisticalOption {
  page?: number;

  size?: number;

  sort?: string;

  direction?: string;
}

const DEFAULT_STATISTICAL_OPTION: StatisticalOption = {
  page: 0,
  size: 10,
  direction: "DESC"
};

@Injectable({
  providedIn: "root"
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
        params: new HttpParams().set("top", top.toString())
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
    statisticalType: StatistycalType = StatistycalType.DAY
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
}
