import { ProductDataView } from './../products-data/products-data.component';
import { ProductParameterDto } from './../../../models/dtos/product-parameter.dto';
import { ProductDto } from './../../../models/dtos/product.dto';
import { ProductDescriptionFormComponent } from './product-description-form/product-description-form.component';
import { ParametersProductFormComponent } from './parameters-product-form/parameters-product-form.component';
import { ParagraphDto } from './../../../models/dtos/paragraph.dto';
import { MultiChooseImagesComponent } from './../../../multi-choose-images/multi-choose-images.component';
import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import {
  Component,
  OnInit,
  ViewChild,
  OnDestroy,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { Observable, BehaviorSubject, of, Subscription } from 'rxjs';
import { filter, map, switchMap, takeUntil } from 'rxjs/operators';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { CategoryService } from 'src/app/services/category.service';
import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { BaseProductFormComponent } from './base-product-form/base-product-form.component';
import { ProductService } from 'src/app/services/product.service';

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

  subcription = new Subscription();

  imagesUrls = new BehaviorSubject<any>(null);

  imageUrls$ = this.imagesUrls.asObservable();

  descriptions$: Observable<ParagraphDto[]>;

  images: File[];

  @Input() currentProduct$: Observable<ProductDataView>;

  @Output() onCancleCliked = new EventEmitter(true);

  constructor(
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private productService: ProductService
  ) {
    this.initForm();
  }

  ngOnInit() {
    this.subcription.add(this.currentProduct$
      .pipe(
        filter(product => product != null),
        switchMap(product => product.images$),
        map(images => images.map(image => image.data)),
        map(datas => {
          return datas.map(data => `data:image/png;base64,${data}`);
        })
      )
      .subscribe(datas => this.imagesUrls.next(datas)));
    this.subcription.add(
      this.multiChooseImages.imagesChoosed$.subscribe(images => {
        this.images = images;
      })
    );

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
      switchMap(product => this.productService.getDescriptions(product.id))
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
        this.formBuilder.control(''),
        this.formBuilder.control(''),
      ]),
      manufacturerId: [null],
      price: [null, [Validators.required]],
      quantity: [null],
    });
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
    this.parameterTypes$ = this.categoryService.getParameterTypesBy(categoryId);
  }

  ngOnDestroy() {
    this.subcription.unsubscribe();
    this.imagesUrls.complete();
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

  cancle() {
    this.onCancleCliked.emit();
    this.initForm();
    this.imagesUrls.next(null);
  }

  onSubmit() {
    const product = this.getProduct();
    console.log(product);
    this.currentProduct$.subscribe(console.log);
    // this.productService
    //   .createProduct(product)
    //   .subscribe(() => console.log('ok'));
  }
}
