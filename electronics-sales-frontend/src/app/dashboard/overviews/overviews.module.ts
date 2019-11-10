import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OverviewsRoutingModule } from './overviews-routing.module';
import { OverviewsComponent } from './overviews.component';


@NgModule({
  declarations: [OverviewsComponent],
  imports: [
    CommonModule,
    OverviewsRoutingModule
  ]
})
export class OverviewsModule { }
