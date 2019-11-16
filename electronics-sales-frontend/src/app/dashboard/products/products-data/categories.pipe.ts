import { CategoryView } from './../../../models/view-model/category.view.model';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'categoriesData'
})
export class CategoriesPipe implements PipeTransform {

  transform(value: CategoryView[]): any {
    if (!value || value.length === 0) {
      return '';
    }

    return value
      .map((v) => v.categoryName)
      .join(' - ');
  }

}
