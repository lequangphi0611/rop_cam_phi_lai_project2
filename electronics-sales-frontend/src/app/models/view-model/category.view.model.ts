import { CategoryView } from 'src/app/models/view-model/category.view.model';

export interface CategoryView {

  id: number;

  categoryName: string;

  parentId: number;

  productCount: number;

  childrens: CategoryView[];

}
