import { MatSnackBar } from '@angular/material/snack-bar';
import {
  AfterViewInit,
  Component,
  Input,
  OnDestroy,
  OnInit,
  Output,
  EventEmitter,
  ViewChild,
  ElementRef,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subject, of, fromEvent } from 'rxjs';
import { map, takeUntil, switchMap, tap, debounceTime, filter } from 'rxjs/operators';
import { CategoryDto } from 'src/app/models/dtos/category.dto';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { CategoryService } from 'src/app/services/category.service';
import { ParameterTypeService } from 'src/app/services/parameter-type.service';
import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { ManufacturerService } from './../../../services/manufacturer.service';
import { CategoryDataView } from './../category-data-table/category-data-table.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ManufacturerFormDialogComponent } from '../../manufacturer/manufacturer-form-dialog/manufacturer-form-dialog.component';
import { ParameterFormDialogComponent } from '../parameter-form-dialog/parameter-form-dialog.component';

@Component({
  selector: 'app-d-category-form',
  templateUrl: './d-category-form.component.html',
  styleUrls: ['./d-category-form.component.css'],
})
export class DCategoryFormComponent
  implements OnInit, AfterViewInit, OnDestroy {

  static readonly DEFAULT_DIALOG_CONFIG: MatDialogConfig = {
    autoFocus: true,
    disableClose: false
  };

  categoryForm: FormGroup;

  manufacturers$: Observable<ManufacturerView[]>;

  parameterTypes$: Observable<ParameterTypeDto[]>;

  parent$: Observable<CategoryView[]>;

  @Input() currenCategory: CategoryDataView;

  @Output() cancled: EventEmitter<void>;

  unscriptions$ = new Subject();

  editMode = false;

  @ViewChild('categoryNameInput', {static: true})
  categoryNameInput: ElementRef;

  categoryNameInputEvent$ = of(null);

  constructor(
    private formBuilder: FormBuilder,
    private manufacturerService: ManufacturerService,
    private parameterTypeService: ParameterTypeService,
    private categoryService: CategoryService,
    private snackbar: MatSnackBar,
    private dialog: MatDialog
  ) {
    this.cancled = new EventEmitter(true);
  }

  ngOnInit() {
    this.categoryForm = this.formBuilder.group({
      categoryName: ['', [Validators.required]],
      parentId: [0, []],
      manufacturerIds: [[], []],
      parameterTypes: [[], []],
    });

    if (this.currenCategory) {
      this.setFormValueBy(this.currenCategory);
      this.editMode = true;
    }
  }

  ngAfterViewInit() {
    this.fetchManufacturers();
    this.fetchParameterTypes();
    this.parent$ = this.categoryService.fetchCategories();

    this.categoryNameInputEvent$ = fromEvent<any>(this.categoryNameInput.nativeElement, 'input')
    .pipe(map(event => event.target.value));
    this.subcriptionCheckExistsCategoryName();

  }

  checkExistsCategoryName(categoryName: string): Observable<boolean> {
    if (this.editMode && this.currenCategory.categoryName.toLowerCase() === categoryName.toLowerCase()) {
      return of(false);
    }

    return this.categoryService.existsByName(categoryName);
  }

  subcriptionCheckExistsCategoryName() {
    this.categoryNameInputEvent$
      .pipe(
        debounceTime(500),
        filter(value => value && value.trim().length > 0),
        switchMap(value => this.checkExistsCategoryName(value)),
        filter(exists => exists)
      ).subscribe(() => this.categoryNameControl.setErrors({
        existsName: true
      }));
  }

  fetchManufacturers() {
    this.manufacturers$ = this.manufacturerService.fetchAll();
  }

  fetchParameterTypes() {
    this.parameterTypes$ = this.parameterTypeService.fetchAll();
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
    return parentId === 0 ? null : parentId;
  }

  pushManufacturerId(manufacturerId: number) {
    const manufacturerValue = this.manufacturerIdsControl.value as number[];
    manufacturerValue.push(manufacturerId);
    this.manufacturerIdsControl.setValue(manufacturerValue);
  }

  pushParameterType(parameterType: ParameterTypeDto) {
    const parameterTypesValue = this.parameterTypes.value as ParameterTypeDto[];
    parameterTypesValue.push(parameterType);
    this.parameterTypes.setValue(parameterTypesValue);
  }

  setFormValueBy(category: CategoryDataView) {
    const { categoryName, parentId } = category;
    this.categoryNameControl.setValue(categoryName);
    this.parentIdControl.setValue(parentId ? parentId : 0);
    const manufacturerIds = category.manufacturers.map(v => v.id);
    this.manufacturerIdsControl.setValue(manufacturerIds);

    this.parameterTypes.setValue(category.parmaterTypes);
  }

  resetForm() {
    this.categoryNameControl.setValue(null);
    this.parentIdControl.setValue(null);
    this.manufacturerIdsControl.setValue([]);
    this.parameterTypes.setValue([]);
  }

  getCategory(): CategoryDto {
    return {
      categoryName: this.categoryNameControl.value.trim(),
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
    of(this.editMode)
      .pipe(
        takeUntil(this.unscriptions$),
        switchMap(edit => {
          if (!edit) {
            return this.categoryService.create(category);
          }
          category.id = this.currenCategory.id;
          return this.categoryService.update(category);
        })
      )
      .subscribe(
        () => this.onSuccess(),
        err => this.onError(err)
      );
  }
  onError(err: any): void {
    this.snackbar.open(`Lưu không thành công !`, 'Đóng', { duration: 2000 });
  }
  onSuccess(): void {
    this.snackbar.open(`Lưu thành công !`, 'Đóng', { duration: 2000 });
    this.cancle();
  }

  cancle() {
    this.resetForm();
    this.currenCategory = null;
    this.cancled.emit();
  }

  openFormManufacturer() {
    const dialogConfig = {...DCategoryFormComponent.DEFAULT_DIALOG_CONFIG};

    dialogConfig.data = {edit: false};

    const manufacturerDialog = this.dialog.open(ManufacturerFormDialogComponent, dialogConfig);

    manufacturerDialog.componentInstance.saveSuccess.pipe(
      takeUntil(this.unscriptions$),
      map((v: ManufacturerView) => v.id),
      tap(id => {
        this.pushManufacturerId(id);
        this.fetchManufacturers();
      })
    ).subscribe();
  }

  openFormParameter() {
    const dialogConfig = {...DCategoryFormComponent.DEFAULT_DIALOG_CONFIG};

    dialogConfig.minWidth = '400px';
    const parameterDialog = this.dialog.open(ParameterFormDialogComponent, dialogConfig);

    parameterDialog.componentInstance.saveSuccess
      .pipe(takeUntil(this.unscriptions$))
      .subscribe(parameterType => {
        this.pushParameterType(parameterType);
        this.fetchParameterTypes();
      });
  }

  ngOnDestroy() {
    this.unscriptions$.next();
    this.unscriptions$.complete();
  }
}
