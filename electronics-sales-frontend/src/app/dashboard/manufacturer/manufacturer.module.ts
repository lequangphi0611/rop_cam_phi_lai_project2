import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChooseImagesModule } from './../../choose-images/choose-images.module';
import { MaterialModule } from './../../material/material.module';
import { LazyLoadImageModule } from 'ng-lazyload-image';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ManufacturerRoutingModule } from './manufacturer-routing.module';
import { ManufacturerComponent } from './manufacturer.component';
import { ManufacturerFormDialogComponent } from './manufacturer-form-dialog/manufacturer-form-dialog.component';


@NgModule({
  declarations: [ManufacturerComponent, ManufacturerFormDialogComponent],
  imports: [
    CommonModule,
    ManufacturerRoutingModule,
    LazyLoadImageModule,
    MaterialModule,
    ChooseImagesModule,
    FormsModule,
    ReactiveFormsModule
  ],
  entryComponents: [ManufacturerFormDialogComponent]
})
export class ManufacturerModule { }
