import { OverviewsModule } from './overviews/overviews.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { SideBarItemComponent } from './side-bar-item/side-bar-item.component';


@NgModule({
  declarations: [DashboardComponent, SideBarItemComponent],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    OverviewsModule
  ]
})
export class DashboardModule { }
