import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit, Inject, OnDestroy, Output, EventEmitter } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ImportInvoiceService } from 'src/app/services/import-invoice.service';
import { ImportInvoiceDto } from 'src/app/models/dtos/import-invoice.dto';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-import-products',
  templateUrl: './import-products.component.html',
  styleUrls: ['./import-products.component.css']
})
export class ImportProductsComponent implements OnInit, OnDestroy {

  importForm: FormGroup;

  private unscription$ = new Subject();

  constructor(
    private formBuilder: FormBuilder,
    private importInvoiceService: ImportInvoiceService,
    private snackbar: MatSnackBar,
    private dialogRef: MatDialogRef<ImportProductsComponent>,
    @Inject(MAT_DIALOG_DATA) private data,
  ) {
   }

   get product() {
     return this.data.product;
   }

   get quantityControl() {
     return this.importForm.get('quantity');
   }

  ngOnInit() {
    this.importForm = this.formBuilder.group({
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
  }

  close() {
    this.dialogRef.close(false);
  }

  getImportInvoice(): ImportInvoiceDto {
    return {
      productId: this.product.id,
      quantity: this.quantityControl.value as number
    };
  }

  save() {
    const importInvoice = this.getImportInvoice();
    this.importInvoiceService.create(importInvoice)
      .pipe(takeUntil(this.unscription$))
      .subscribe(() => {
        this.onSuccess();
      });
  }
  onSuccess() {
    this.snackbar.open('Nhập số lượng thành công !', 'Đóng', {
      duration: 2000
    });
    this.dialogRef.close(true);
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
