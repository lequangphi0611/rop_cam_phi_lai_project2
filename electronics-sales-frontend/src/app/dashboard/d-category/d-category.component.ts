import { Component, OnInit } from '@angular/core';
import { CategoryDataView } from './category-data-table/category-data-table.component';

@Component({
  selector: 'app-d-category',
  templateUrl: './d-category.component.html',
  styleUrls: ['./d-category.component.css']
})
export class DCategoryComponent implements OnInit {
  static readonly CATEGORY_FORM_INDEX = 1;

  selectedIndex = 0;

  currentCategory: CategoryDataView;

  constructor() { }

  ngOnInit() {
  }

  changeSelectedIndex(index: number) {
    this.selectedIndex = index;
  }

  openEditFor(category: CategoryDataView) {
    this.changeSelectedIndex(DCategoryComponent.CATEGORY_FORM_INDEX);
    console.log(category);
    this.currentCategory = category;
  }

  onCategoryFormCancled() {
    this.currentCategory = null;
  }
}
