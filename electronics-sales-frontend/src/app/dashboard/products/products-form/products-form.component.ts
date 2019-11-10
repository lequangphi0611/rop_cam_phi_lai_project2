import { ProductParameterDto } from './../../../models/dtos/product-parameter.dto';
import { ProductDto } from './../../../models/dtos/product.dto';
import { ProductDescriptionFormComponent } from './product-description-form/product-description-form.component';
import { ParametersProductFormComponent } from './parameters-product-form/parameters-product-form.component';
import { ParagraphDto } from './../../../models/dtos/paragraph.dto';
import { MultiChooseImagesComponent } from './../../../multi-choose-images/multi-choose-images.component';
import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
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

  images: File[];

  constructor(
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private productService: ProductService
  ) {}

  ngOnInit() {
    this.subcription.add(
      this.multiChooseImages.imagesChoosed$.subscribe(images => {
        this.images = images;
      })
    );
  }

  onSelectedCategory(categoryId: number) {
    this.parameterTypes$ = this.categoryService.getParameterTypesBy(categoryId);
  }

  ngOnDestroy() {
    this.subcription.unsubscribe();
  }

  onBasicFormChange(basicForm: FormGroup) {
    this.basicForm = basicForm;
  }

  onDecriptionFormChange(descriptionForm: FormGroup) {
    this.descriptionsForm = descriptionForm;
    console.log('description init ', this.descriptionsForm.value);
  }

  onParametersFormChange(parameterForm: FormGroup) {
    this.parametersForm = parameterForm;
  }

  getProduct(): ProductDto {
    const baseFormValue = this.basicForm.value;
    const parametersFormValue = this.parametersForm
      ? this.parametersForm.value
      : null;
    const descriptionFormValue = this.descriptionsForm.value;
    console.log({ descriptionFormValue });
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
    const descriptionsProduct = (descriptionFormValue.descriptions as Array<{title: string, text: string}>)
      .map(e => {
        return {...e};
      });
    return {
      productName: baseFormValue.productName,
      categoryIds: baseFormValue.categoryIds,
      manufacturerId: baseFormValue.manufacturerId,
      images: this.images,
      price: baseFormValue.price as number,
      productParameters,
      paragraphs: descriptionsProduct
    };
  }

  onSubmit() {
    const product = this.getProduct();

    this.productService.createProduct(product)
      .subscribe(console.log);
  }
}
