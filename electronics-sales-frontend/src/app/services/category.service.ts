import { CategoryView } from './../models/view-model/category.view.model';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) { }

  fetchCategories(): Observable<CategoryView[]> {
    return this.http.get<CategoryView[]>('/api/categories');
  }
}
