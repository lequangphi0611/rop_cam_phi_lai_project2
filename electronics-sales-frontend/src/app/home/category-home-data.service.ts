import { CategoryView } from './../models/view-model/category.view.model';
import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CategoryHomeDataService {

  private categories = new BehaviorSubject([]);

  categories$ = this.categories.asObservable();

  constructor() { }

  setValue(categories: CategoryView[]): void {
    this.categories.next(categories);
  }
}
