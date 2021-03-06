import { ManufacturerModule } from './../manufacturer/manufacturer.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DCategoryRoutingModule } from './d-category-routing.module';
import { DCategoryComponent } from './d-category.component';
import { CategoryDataTableComponent } from './category-data-table/category-data-table.component';
import { ParamterTypeTransformPipe } from './category-data-table/paramter-type-transform.pipe';
import { ManufacturersTransformPipe } from './category-data-table/manufacturers-transform.pipe';
import { DCategoryFormComponent } from './d-category-form/d-category-form.component';
import { ParameterFormDialogComponent } from './parameter-form-dialog/parameter-form-dialog.component';

@NgModule({
  declarations: [
    DCategoryComponent,
    CategoryDataTableComponent,
    ParamterTypeTransformPipe,
    ManufacturersTransformPipe,
    DCategoryFormComponent,
    ParameterFormDialogComponent,
  ],
  imports: [
    CommonModule,
    DCategoryRoutingModule,
    MaterialModule,
    PipesModule,
    FormsModule,
    ReactiveFormsModule,
    ManufacturerModule
  ],
  entryComponents: [ParameterFormDialogComponent]
})
export class DCategoryModule {}
