import { PipesModule } from 'src/app/pipes/pipes.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StatisticalRoutingModule } from './statistical-routing.module';
import { StatisticalComponent } from './statistical.component';
import { RevenueStatisticalComponent } from './revenue-statistical/revenue-statistical.component';
import { ImportInvoiceReportComponent } from './import-invoice-report/import-invoice-report.component';


@NgModule({
  declarations: [StatisticalComponent, RevenueStatisticalComponent, ImportInvoiceReportComponent],
  imports: [
    CommonModule,
    StatisticalRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    PipesModule
  ]
})
export class StatisticalModule { }
