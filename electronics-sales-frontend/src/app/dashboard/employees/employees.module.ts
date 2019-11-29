import { ConfirmModule } from './../../confirm/confirm.module';
import { ChooseImagesModule } from './../../choose-images/choose-images.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EmployeesRoutingModule } from './employees-routing.module';
import { EmployeesComponent } from './employees.component';
import { EmployeeFormDialogComponent } from './employee-form-dialog/employee-form-dialog.component';
import { EmployeesDataComponent } from './employees-data/employees-data.component';


@NgModule({
  declarations: [EmployeesComponent, EmployeeFormDialogComponent, EmployeesDataComponent],
  imports: [
    CommonModule,
    EmployeesRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    ChooseImagesModule,
    ConfirmModule
  ],
  entryComponents: [EmployeeFormDialogComponent]
})
export class EmployeesModule { }
