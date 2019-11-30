import { DiscountView } from './../models/view-model/discount.view';
import { Observable } from 'rxjs';
import { DiscountDto } from './../models/dtos/discount.dto';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


const DISCOUNT_URL = '/api/discounts';

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

  fetchAll() {

  }

}
