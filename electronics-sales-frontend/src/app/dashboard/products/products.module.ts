import { LazyLoadImageModule } from 'ng-lazyload-image';
import { MyLazyLoadImageModule } from './../../my-lazy-load-image/my-lazy-load-image.module';
import { PipesModule } from './../../pipes/pipes.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './../../material/material.module';
import { ChooseImagesModule } from './../../choose-images/choose-images.module';
import { MultiChooseImagesModule } from './../../multi-choose-images/multi-choose-images.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductsComponent } from './products.component';
import { ProductsFormComponent } from './products-form/products-form.component';
import { BaseProductFormComponent } from './products-form/base-product-form/base-product-form.component';
import { ParametersProductFormComponent } from './products-form/parameters-product-form/parameters-product-form.component';
import { ProductDescriptionFormComponent } from './products-form/product-description-form/product-description-form.component';
import { ProductsDataComponent } from './products-data/products-data.component';


@NgModule({
  declarations: [ProductsComponent, ProductsFormComponent, BaseProductFormComponent, ParametersProductFormComponent, ProductDescriptionFormComponent, ProductsDataComponent],
  imports: [
    CommonModule,
    ProductsRoutingModule,
    MultiChooseImagesModule,
    ChooseImagesModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    PipesModule,
    MyLazyLoadImageModule,
    LazyLoadImageModule
  ]
})
export class ProductsModule { }
