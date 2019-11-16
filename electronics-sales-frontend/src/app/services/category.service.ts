import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ParameterTypeDto } from './../models/dtos/paramter-type.dto';
import { CategoryView } from './../models/view-model/category.view.model';
import { ManufacturerView } from './../models/view-model/manufacturer.view.model';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  static readonly BASE_REQUEST = '/api/categories';

  constructor(private http: HttpClient) {}

  fetchCategories(): Observable<CategoryView[]> {
    return this.http.get<CategoryView[]>('/api/categories');
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
    return this.http.get<CategoryView[]>(`${CategoryService.BASE_REQUEST}/${categoryId}/childrens`);
  }
}
