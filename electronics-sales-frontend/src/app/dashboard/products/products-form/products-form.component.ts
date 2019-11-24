import { ImageView } from './../../../models/view-model/image-data.view';
import { ProductView } from './../../../models/view-model/product.view.model';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { filter, map, switchMap, takeUntil, finalize } from 'rxjs/operators';
import { CategoryService } from 'src/app/services/category.service';
import { ImportInvoiceService } from 'src/app/services/import-invoice.service';
import { ProductService } from 'src/app/services/product.service';
import { ParagraphDto } from './../../../models/dtos/paragraph.dto';
import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { ProductDto } from './../../../models/dtos/product.dto';
import { MultiChooseImagesComponent } from './../../../multi-choose-images/multi-choose-images.component';
import { ProductDataView } from './../products-data/products-data.component';

@Component({
  selector: 'app-products-form',
  templateUrl: './products-form.component.html',
  styleUrls: ['../../dashboard.component.css', './products-form.component.css'],
})
export class ProductsFormComponent implements OnInit, OnDestroy {
  parameterTypes$: Observable<ParameterTypeDto[]>;

  @ViewChild('multiChooseImages', { static: true })
  multiChooseImages: MultiChooseImagesComponent;

  basicForm: FormGroup;

  parametersForm: FormGroup;

  descriptionsForm: FormGroup;

  editMode = false;

  private unscription$ = new Subject<void>();

  imagesUrls = new BehaviorSubject<any>(null);

  proccessing = new BehaviorSubject(false);

  proccessing$ = this.proccessing.asObservable();

  imageUrls$ = this.imagesUrls.asObservable();

  descriptions$: Observable<ParagraphDto[]>;

  images: File[];

  productId: number;

  @Input() currentProduct$: Observable<ProductDataView>;

  @Output() onCancleCliked = new EventEmitter(true);

  constructor(
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private productService: ProductService,
    private _snackBar: MatSnackBar,
    private importInvoice: ImportInvoiceService
  ) {}

  ngOnInit() {
    this.initForm();
    this.currentProduct$
      .pipe(
        takeUntil(this.unscription$),
        filter(product => product != null),
        map(product => product.images$.value as ImageView[]),
        map(images => images.map(image => image.data)),
        map(datas => datas.map(data => `data:image/png;base64,${data}`))
      )
      .subscribe(datas => this.imagesUrls.next(datas));

    this.multiChooseImages.imagesChoosed$
      .pipe(takeUntil(this.unscription$))
      .subscribe(images => {
        this.images = images;
      });

    this.currentProduct$
      .pipe(takeUntil(this.unscription$))
      .subscribe(product => {
        this.editMode = product != null;
        this.productId = product ? product.id : null;
      });

    this.parameterTypes$ = this.currentProduct$.pipe(
      filter(value => value != null),
      switchMap(product => this.productService.getParameters(product.id)),
      map(productParameters =>
        productParameters.map(productParameter => {
          return {
            id: productParameter.id,
            parameterTypeName: productParameter.parameterType,
            parameterTypeValue: productParameter.parameterValue,
          };
        })
      )
    );

    this.descriptions$ = this.currentProduct$.pipe(
      filter(value => value != null),
      map(product => product.descriptions)
    );
  }

  initForm() {
    this.initBaseForm();
    this.initParametersForm();
    this.initDescriptionsForm();
  }

  initBaseForm() {
    this.basicForm = this.formBuilder.group({
      productName: [null, [Validators.required]],
      categoryIds: this.formBuilder.array([
        this.formBuilder.control('', Validators.required),
        this.formBuilder.control(''),
      ]),
      manufacturerId: [null],
      price: [0, [Validators.required]],
      quantity: [0],
    });
  }

  get quantityControl() {
    return this.basicForm.get('quantity');
  }

  initParametersForm() {
    this.parametersForm = this.formBuilder.group({
      parameters: this.formBuilder.array([]),
    });
  }

  initDescriptionsForm() {
    this.descriptionsForm = this.formBuilder.group({
      descriptions: this.formBuilder.array([]),
    });
  }

  onSelectedCategory(categoryId: number) {
    this.initParametersForm();
    this.parameterTypes$ = this.categoryService.getParameterTypesBy(categoryId);
  }

  getProduct(): ProductDto {
    const baseFormValue = this.basicForm.value;
    const parametersFormValue = this.parametersForm
      ? this.parametersForm.value
      : null;
    const descriptionFormValue = this.descriptionsForm.value;
    let productParameters = null;
    if (parametersFormValue) {
      productParameters = (parametersFormValue.parameters as Array<{
        id: number;
        parameterTypeName: string;
        parameterValue: string;
      }>).map(e => {
        return { parameterTypeId: e.id, parameterValue: e.parameterValue };
      });
    }
    const descriptionsProduct = (descriptionFormValue.descriptions as Array<{
      title: string;
      text: string;
    }>).map(e => {
      return { title: e.title, text: e.text };
    });
    const categoryIds = (baseFormValue.categoryIds as Array<number>).filter(
      v => v && v > 0
    );
    return {
      productName: baseFormValue.productName,
      categoryIds,
      manufacturerId: baseFormValue.manufacturerId,

      images: this.images,
      price: baseFormValue.price as number,
      productParameters,
      paragraphs: descriptionsProduct,
    };
  }

  isValid() {
    if (!this.basicForm) {
      return false;
    }

    if (this.basicForm.invalid) {
      return false;
    }

    if (this.parametersForm && this.parametersForm.invalid) {
      return false;
    }

    return true;
  }

  cancle() {
    this.onCancleCliked.emit();
    this.initForm();
    this.imagesUrls.next(null);
  }

  createProduct(productDto: ProductDto): Observable<ProductView | any> {
    return this.productService.createProduct(productDto)
      .pipe(switchMap(product => {
        const quantity = this.quantityControl.value || 0;
        if (quantity <= 0) {
          return of(product);
        }
        return this.importInvoice.create({
          productId: product.id,
          quantity
        });
      }));
  }

  updateProduct(productDto: ProductDto): Observable<ProductView> {
    return this.productService.updateProduct(productDto);
  }

  async onSubmit(): Promise<void> {
    this.proccessing.next(true);
    const product = this.getProduct();
    of(null)
      .pipe(
        takeUntil(this.unscription$),
        switchMap(() => {
          if (!this.editMode) {
            return this.createProduct(product);
          }
          product.id = this.productId;
          return this.updateProduct(product);
        }),
        finalize(() => this.proccessing.next(false))
      )
      .subscribe(
        result => this.onSuccess(result),
        err => this.onError(err)
      );
  }
  onError(err: any): void {
    console.log({err});
  }

  onSuccess(result?: any) {
    this.cancle();
    this._snackBar.open('Thành công !', 'Đóng', {
      duration: 2000,
    });
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
    this.imagesUrls.complete();
  }
}
