import { DiscountDataView } from './../discount.component';
import { DiscountView } from './../../../models/view-model/discount.view';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DiscountService } from './../../../services/discount.service';
import { DiscountDto } from './../../../models/dtos/discount.dto';
import { takeUntil, switchMap } from 'rxjs/operators';
import { CategoryProductReceiver } from './../../../models/category-and-product-receive.model';
import { Observable, Subject, of } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  DISCOUNT_TYPES,
  DiscountType
} from './../../../models/types/discount.type';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogConfig
} from '@angular/material/dialog';
import {
  Component,
  OnInit,
  Inject,
  ViewChild,
  ElementRef,
  AfterViewInit,
  OnDestroy,
  Output,
  EventEmitter
} from '@angular/core';
import { CategoryService } from 'src/app/services/category.service';

const MIN_PERCENT_VALUE = 0;

const MIN_AMOUNT_VALUE = 1000;

const MAX_PERCENT_VALUE = 100;

const DEFAULT_DISCOUNT_VALUE_VALIDATORS = [Validators.required];

const DISCOUNT_PERCENT_VALIDATORS = [
  Validators.min(MIN_PERCENT_VALUE),
  Validators.max(MAX_PERCENT_VALUE)
];

const DISCOUNT_AMOUNT_VALIDATORS = [Validators.min(MIN_AMOUNT_VALUE)];

const DISCOUNT_FORM = {
  discountType: [DiscountType.PERCENT, []],
  discountValue: [
    '',
    [...DEFAULT_DISCOUNT_VALUE_VALIDATORS, ...DISCOUNT_PERCENT_VALIDATORS]
  ],
  products: [[], [Validators.required]]
};

export interface DiscountFormData {
  id: number;

  discountType: DiscountType;

  discountValue: number;

  products: {
    id: number;
    name: string;
  }[];
}

@Component({
  selector: 'app-discount-form-dialog',
  templateUrl: './discount-form-dialog.component.html',
  styleUrls: ['./discount-form-dialog.component.css']
})
export class DiscountFormDialogComponent implements OnInit, OnDestroy {
  @Output() saveSuccess = new EventEmitter(true);

  discountForm: FormGroup;

  readonly discountTypes = DISCOUNT_TYPES;

  categoriesProducts$: Observable<CategoryProductReceiver[]>;

  unscription$ = new Subject();

  minDiscountValue = MIN_PERCENT_VALUE;

  maxDiscountValue: number | string = MAX_PERCENT_VALUE;

  editMode = false;

  constructor(
    private formBuider: FormBuilder,
    private categoryService: CategoryService,
    private discountService: DiscountService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<DiscountFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data
  ) {}

  static open(dialog: MatDialog, config?: MatDialogConfig) {
    return dialog.open(DiscountFormDialogComponent, config);
  }

  ngOnInit() {
    this.initForm();

    this.discountTypeControl.valueChanges
      .pipe(takeUntil(this.unscription$))
      .subscribe(discountType =>
        this.onDiscountTypeControlChange(discountType)
      );
  }

  initForm() {
    this.discountForm = this.formBuider.group(DISCOUNT_FORM);
    let discountId = null;
    if (this.discountInput) {
      discountId = this.discountInput.id;
      this.editMode = true;
      this.setFormValue(this.discountInput);
    }
    this.categoriesProducts$ = this.categoryService.fetchCategoriesProducts(
      discountId
    );
  }

  get discountInput(): DiscountFormData {
    return this.data ? (this.data as DiscountFormData) : null;
  }

  get discountTypeControl() {
    return this.discountForm.get('discountType');
  }

  get discountValueControl() {
    return this.discountForm.get('discountValue');
  }

  get productsControl() {
    return this.discountForm.get('products');
  }

  get productControlValue() {
    return this.productsControl.value as { id: number; name: string }[];
  }

  get productIds(): number[] {
    if (this.productControlValue.length === 0) {
      return [];
    }

    return this.productControlValue.map(p => p.id);
  }

  setFormValue(discount: DiscountFormData) {
    const { discountType, discountValue, products } = discount;
    this.discountForm.setValue({
      discountType,
      discountValue,
      products
    });
    this.onDiscountTypeControlChange(discountType);
  }

  get discount(): DiscountDto {
    const { discountType, discountValue } = this.discountForm.value;
    return {
      discountType,
      discountValue,
      productIds: this.productIds,
      id: this.editMode ? this.discountInput.id : null
    };
  }

  get suffix() {
    const { value } = this.discountTypeControl;
    return value === DiscountType.PERCENT ? '%' : 'VNĐ';
  }

  discountTypeToString(discountType: DiscountType) {
    if (discountType === DiscountType.PERCENT) {
      return 'Phần trăm';
    }

    return 'Tiền';
  }

  onDiscountTypeControlChange(discountType: DiscountType): void {
    let validators = [...DEFAULT_DISCOUNT_VALUE_VALIDATORS];
    if (discountType === DiscountType.PERCENT) {
      validators = [...validators, ...DISCOUNT_PERCENT_VALIDATORS];
      this.minDiscountValue = MIN_PERCENT_VALUE;
      this.maxDiscountValue = MAX_PERCENT_VALUE;
    } else {
      validators = [...validators, ...DISCOUNT_AMOUNT_VALIDATORS];
      this.minDiscountValue = MIN_AMOUNT_VALUE;
      this.maxDiscountValue = '';
    }
    this.discountValueControl.markAsTouched();
    this.discountValueControl.setValidators(validators);
    this.discountValueControl.updateValueAndValidity();
  }

  compartProduct(ob1, ob2) {
    return ob1 && ob2 && ob1.id === ob2.id;
  }

  removedProduct(product: { id: number; name: string }) {
    const { id } = product;
    const index = this.productControlValue.findIndex(v => v.id === id);
    this.productControlValue.splice(index, 1);
    this.productsControl.setValue(this.productControlValue);
  }

  onSubmit() {
    const discount = this.discount;
    of(this.editMode)
      .pipe(
        switchMap(edit => {
          if (!edit) {
            return this.discountService.create(discount);
          }
          return this.discountService.update(discount);
        })
      )
      .subscribe(
        result => this.onSuccess(result),
        err => this.onError(err)
      );
  }

  onError(err: any): void {
    this.snackBar.open('Có lỗi xảy ra', 'Đóng', { duration: 2000 });
  }

  onSuccess(result: DiscountView): void {
    this.saveSuccess.emit(result);
    this.dialogRef.close();
    this.snackBar.open(`Lưu thành công !`, `Đóng`, { duration: 2000 });
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
