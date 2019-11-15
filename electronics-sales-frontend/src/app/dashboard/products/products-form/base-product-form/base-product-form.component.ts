import { ProductDataView } from './../../products-data/products-data.component';
import { filter, switchMap, takeUntil, map } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { CategoryService } from 'src/app/services/category.service';
import { ManufacturerView } from './../../../../models/view-model/manufacturer.view.model';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { Observable, BehaviorSubject, of } from 'rxjs';
import {
  Component,
  OnInit,
  OnDestroy,
  Output,
  EventEmitter,
  Input,
} from '@angular/core';
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'app-base-product-form',
  templateUrl: './base-product-form.component.html',
  styleUrls: [
    '../../../dashboard.component.css',
    './base-product-form.component.css',
  ],
})
export class BaseProductFormComponent implements OnInit, OnDestroy {
  @Output() onChange = new EventEmitter(true);

  @Output() onInit = new EventEmitter(true);

  @Output() onSelectedCategory = new EventEmitter();

  allCategories$: Observable<CategoryView[]>;

  categoryChildrens: CategoryView[];

  manufacturers$: Observable<ManufacturerView[]>;

  @Input() basicProductForm: FormGroup;

  @Input() currentProduct$: Observable<ProductDataView>;

  constructor(
    private categoryService: CategoryService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.categorySubcription();
    this.onInit.emit(this.basicProductForm);
    this.onChange.emit(this.basicProductForm);

    this.currentProduct$
      .pipe(filter(product => product != null))
      .subscribe(product => this.setFormValues(product));
  }

  async setFormValues(product: ProductDataView) {
    this.productNameControl.setValue(product.productName);
    (await product.categories$.toPromise())
      .filter((value, i) => i <= 1)
      .map((category) => {
        this.categoryService.getChildrens(category.id)
          .pipe(filter(childrens => childrens && childrens.length > 0))
          .subscribe(childrens => this.categoryChildrens = childrens);
        this.fetchManufacturer(category.id);
        return category.id;
      })
      .forEach((categoryId, i) =>
        this.categoryIdsFormArray.at(i).setValue(categoryId)
      );
    this.priceControl.setValue(product.price);
    this.manufacturerIdControl.setValue(product.manufacturerId);
  }

  get manufacturerIdControl() {
    return this.basicProductForm.get('manufacturerId');
  }

  get productNameControl() {
    return this.basicProductForm.get('productName');
  }

  get categoryIdsFormArray() {
    return this.basicProductForm.get('categoryIds') as FormArray;
  }

  get priceControl() {
    return this.basicProductForm.get('price');
  }

  categorySubcription() {
    this.allCategories$ = this.categoryService.fetchCategories();
  }

  async onSelectCategory(categoryId: number, categoriesParam?: CategoryView[]) {
    const categories =
      categoriesParam || (await this.allCategories$.toPromise());
    const category = categories.filter(e => e.id === categoryId)[0];
    if (category && !categoriesParam) {
      if (!category.childrens || category.childrens.length === 0) {
        this.categoryChildrens = null;
      } else {
        this.categoryChildrens = category.childrens;
      }
    }
    this.onCategoryValueChange(categoryId);
  }

  onCategoryValueChange(categoryId: number) {
    this.onSelectedCategory.emit(categoryId);
    this.onChange.emit(this.basicProductForm);
    this.fetchManufacturer(categoryId);
  }

  fetchManufacturer(categoryId: number) {
    this.manufacturers$ = this.categoryService
      .getManufacturersBy(categoryId)
      .pipe(filter(manufacturers => manufacturers.length > 0));
  }

  trackByManufacturer(item: ManufacturerView, index: number) {
    return item.id;
  }

  onFormChange() {
    this.onChange.emit(this.basicProductForm);
  }

  ngOnDestroy() {
  }
}
