import { filter, switchMap, takeUntil, map } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { CategoryService } from 'src/app/services/category.service';
import { ManufacturerView } from './../../../../models/view-model/manufacturer.view.model';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { Observable, BehaviorSubject } from 'rxjs';
import { Component, OnInit, OnDestroy, Output, EventEmitter, Input } from '@angular/core';
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

  basicProductForm: FormGroup;

  constructor(
    private categoryService: CategoryService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.categorySubcription();
    this.basicProductForm = this.buildBaseProduct();
    this.onInit.emit(this.basicProductForm);
    this.onChange.emit(this.basicProductForm);
  }

  buildBaseProduct() {
    return this.formBuilder.group({
      productName: [null, [Validators.required]],
      categoryIds: this.formBuilder.array([]),
      categoryIdsForm: this.formBuilder.group({
        parentId: [null],
        childrenId: [null]
      }),
      manufacturerId: [null, [Validators.required]],
      price: [null, [Validators.required]],
      quantity: [null, [Validators.required]]
    });
  }

  get categoryIdsFormArray() {
    return this.basicProductForm.get('categoryIds') as FormArray;
  }

  pushCategoryIdArray(item: number, index: number) {
    const categoryFormCtrl = this.categoryIdsFormArray.at(index);
    if (categoryFormCtrl) {
      categoryFormCtrl.setValue(item);
      return;
    }

    this.categoryIdsFormArray.push(this.formBuilder.control(item));
  }

  categorySubcription() {
    this.allCategories$ = this.categoryService.fetchCategories();
  }

  async onSelectCategory(
    event: MatSelectChange,
    categoriesParam?: CategoryView[]
  ) {
    const categoryId = event.value;
    this.onSelectedCategory.emit(categoryId);
    this.onChange.emit(this.basicProductForm);
    const categories =
      categoriesParam || (await this.allCategories$.toPromise());
    const category = categories.filter(e => e.id === categoryId)[0];
    if (!categoriesParam) {
      if (!category.childrens || category.childrens.length === 0) {
        this.categoryChildrens = null;
      } else {
        this.categoryChildrens = category.childrens;
      }
    }
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
    this.onChange.unsubscribe();
  }

}
