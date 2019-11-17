import { map } from 'rxjs/operators';
import { CategoryDataView } from './../category-data-table/category-data-table.component';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { Observable } from 'rxjs';
import { ManufacturerService } from './../../../services/manufacturer.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Component, OnInit, AfterViewInit, Input } from '@angular/core';
import { ParameterTypeService } from 'src/app/services/parameter-type.service';
import { CategoryService } from 'src/app/services/category.service';
import { CategoryDto } from 'src/app/models/dtos/category.dto';

@Component({
  selector: 'app-d-category-form',
  templateUrl: './d-category-form.component.html',
  styleUrls: ['./d-category-form.component.css'],
})
export class DCategoryFormComponent implements OnInit, AfterViewInit {
  categoryForm: FormGroup;

  manufacturers$: Observable<ManufacturerView[]>;

  parameterTypes$: Observable<ParameterTypeDto[]>;

  parent$: Observable<CategoryView[]>;

  @Input() currenCategory: CategoryDataView;

  constructor(
    private formBuilder: FormBuilder,
    private manufacturerService: ManufacturerService,
    private parameterTypeService: ParameterTypeService,
    private categoryService: CategoryService,
  ) {}

  ngOnInit() {
    this.categoryForm = this.formBuilder.group({
      categoryName: ['', []],
      parentId: [0, []],
      manufacturerIds: [[], []],
      parameterTypes: [[], []]
    });

    if (this.currenCategory) {
      this.setFormValueBy(this.currenCategory);
    }
  }

  ngAfterViewInit() {
    this.manufacturers$ = this.manufacturerService.fetchAll();
    this.parameterTypes$ = this.parameterTypeService.fetchAll();
    this.parent$ = this.categoryService.fetchCategories();
  }

  get manufacturerIdsControl() {
    return this.categoryForm.get('manufacturerIds');
  }

  get categoryNameControl() {
    return this.categoryForm.get('categoryName');
  }

  get parameterTypes() {
    return this.categoryForm.get('parameterTypes');
  }

  get parentIdControl() {
    return this.categoryForm.get('parentId');
  }

  get parentId() {
    const parentId = this.parentIdControl.value as number;
    return  parentId === 0 ? null : parentId;
  }

  setFormValueBy(category: CategoryDataView) {
    const {categoryName, parentId} = category;
    this.categoryNameControl.setValue(categoryName);
    this.parentIdControl.setValue(parentId ? parentId : 0);
    category.manufacturers$
      .pipe(map(manufacturers => manufacturers.map(manufacturer => manufacturer.id)))
      .subscribe(manufacturerIds => {
        console.log({manufacturerIds});
        this.manufacturerIdsControl.setValue(manufacturerIds);
      });

    category.parmaterTypes$
    .subscribe(parameterTypes => {
      console.log({parameterTypes});
      this.parameterTypes.setValue(parameterTypes);
    });
  }

  getCategory(): CategoryDto {
    return {
      categoryName: this.categoryNameControl.value,
      manufacturerIds: this.manufacturerIdsControl.value as number[],
      parameterTypes: this.parameterTypes.value as ParameterTypeDto[],
      parentId: this.parentId,
    };
  }

  compartParameterType(para1, para2) {
    return para1 && para2 && para1.id === para2.id;
  }

  onSubmit() {
    const category = this.getCategory();
    this.categoryService.create(category)
      .subscribe(console.log);
  }
}
