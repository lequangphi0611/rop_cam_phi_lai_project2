import { ProductView } from 'src/app/models/view-model/product.view.model';
import { DiscountType } from './../../../models/types/discount.type';
import { DiscountDto } from './../../../models/dtos/discount.dto';
import { CutStringPipe } from './../../../pipes/cut-string.pipe';
import {
  DiscountFormData,
  DiscountFormDialogComponent
} from './../discount-form-dialog/discount-form-dialog.component';
import { DiscountDataView } from './../discount.component';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DiscountService } from './../../../services/discount.service';
import { DiscountView } from './../../../models/view-model/discount.view';
import { Subject } from 'rxjs';
import {
  Component,
  OnInit,
  OnDestroy,
  Input,
  Output,
  EventEmitter,
  ViewChild,
  AfterViewInit
} from '@angular/core';
import { DiscountDataSource } from '../discount.component';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/confirm/confirm-dialog.component';
import { takeUntil } from 'rxjs/operators';

const DISCOUNT_COLUMN = [
  'startedTime',
  'discountValue',
  'productCount',
  'editAction',
  'deleteAction'
];

const DATE_TIME_FORMAT_PATTERN = 'hh:mm dd-MM-yyyy';

@Component({
  selector: 'app-discount-data',
  templateUrl: './discount-data.component.html',
  styleUrls: ['./discount-data.component.css'],
  providers: [CutStringPipe]
})
export class DiscountDataComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() datasource: DiscountDataSource;

  @Output() deleteElement = new EventEmitter(true);

  @Output() pageChange = new EventEmitter<PageEvent>(true);

  @Output() editSuccess = new EventEmitter(true);

  @Output() removeProductSuccess = new EventEmitter(true);

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  private unscription$ = new Subject();

  displayedColumns = DISCOUNT_COLUMN;

  dateFormatPattern = DATE_TIME_FORMAT_PATTERN;

  constructor(
    private discountService: DiscountService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar,
    private cutString: CutStringPipe
  ) {}

  ngOnInit() {}

  ngAfterViewInit(): void {
    this.paginator.page.subscribe(page => this.pageChange.emit(page));
  }

  trackById(index: number, item: DiscountView) {
    return item.id;
  }

  getDiscountFormData(discount: DiscountDataView): DiscountFormData {
    const products = discount.products.map(product => {
      const { id, productName } = product;
      return {
        id,
        name: productName
      };
    });
    return {
      ...discount,
      products
    };
  }

  onEditActionClick(discount: DiscountDataView) {
    const data: DiscountFormData = this.getDiscountFormData(discount);

    const discountDialogRef = DiscountFormDialogComponent.open(this.dialog, {
      data,
      autoFocus: false
    });
    discountDialogRef.componentInstance.saveSuccess
      .pipe(takeUntil(this.unscription$))
      .subscribe(() => this.editSuccess.emit());
  }

  onDeleteActionClick(discount: DiscountView) {
    const { id } = discount;
    ConfirmDialogComponent.open(this.dialog, {
      message: 'Bạn có chắc chắc muốn xóa ? ',
      actionName: 'Xóa',
      action: () =>
        this.discountService.delete(id).subscribe(
          () => this.onDeleteSuccess(),
          err => this.onDeleteError(err)
        )
    });
  }

  onDeleteSuccess() {
    this.deleteElement.next();
    this.snackbar.open('Xóa thành công !', 'Đóng', { duration: 2000 });
  }

  onDeleteError(err) {
    this.snackbar.open('Xóa không thành công !', 'Đóng', { duration: 2000 });
  }

  removeProduct(discount: DiscountDataView, indexProduct: number) {
    const { productName } = discount.products[indexProduct];
    ConfirmDialogComponent.open(this.dialog, {
      message: `Bạn không muốn giảm giá '${this.cutString.transform(
        productName,
        20
      )}' nữa ?`,
      actionName: 'OK',
      action: () => this.onRemoveProduct(discount, indexProduct)
    });
  }

  onRemoveProduct(discount: DiscountDataView, indexProduct: number) {
    const productsCopied = [...discount.products];
    productsCopied.splice(indexProduct, 1);
    const discountCopied = { ...discount, products: productsCopied };
    const discountDto = this.getDiscountDto(discountCopied);
    console.log(discountDto);
    this.discountService
      .update(discountDto)
      .subscribe(
        (result) => {
          this.onRemoveProductSuccess(result);
          discount.products.splice(indexProduct, 1);
        },
        (error) => this.onRemoveProductError(error)
      );
  }

  onRemoveProductError(error: any): void {
    console.log(error);
    this.snackbar.open('Có lỗi xảy ra', 'Đóng', {duration: 2000});
  }

  onRemoveProductSuccess(result: DiscountView): void {
    this.snackbar.open('Thành Công', 'Đóng', {duration: 2000});
    this.removeProductSuccess.emit();
    console.log(result);
  }

  getDiscountDto(discount: {
    discountType: DiscountType;
    discountValue: number;
    id: number;
    products: ProductView[];
  }): DiscountDto {
    const {id, discountValue, discountType} = discount;
    return {
      id,
      discountType,
      discountValue,
      productIds: discount.products.map(p => p.id)
    };
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
