import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MaterialModule } from './../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StatisticalRoutingModule } from './statistical-routing.module';
import { StatisticalComponent } from './statistical.component';
import { RevenueStatisticalComponent } from './revenue-statistical/revenue-statistical.component';


@NgModule({
  declarations: [StatisticalComponent, RevenueStatisticalComponent],
  imports: [
    CommonModule,
    StatisticalRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class StatisticalModule { }
