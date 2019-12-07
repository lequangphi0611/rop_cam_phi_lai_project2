import { MaterialModule } from './../../material/material.module';
import { PipesModule } from './../../pipes/pipes.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OverviewsRoutingModule } from './overviews-routing.module';
import { OverviewsComponent } from './overviews.component';
import { CategoryStatisticalComponent } from './category-statistical/category-statistical.component';
import { TopProductStatisticalComponent } from './top-product-statistical/top-product-statistical.component';
import { OverviewsChartComponent } from '../../dashbboard/overviews/overviews-chart/overviews-chart.component';


@NgModule({
  declarations: [OverviewsComponent, CategoryStatisticalComponent, TopProductStatisticalComponent, OverviewsChartComponent],
  imports: [
    CommonModule,
    OverviewsRoutingModule,
    PipesModule,
    MaterialModule
  ]
})
export class OverviewsModule { }
