import { ConfirmModule } from './../confirm/confirm.module';
import { MaterialModule } from './../material/material.module';
import { OverviewsModule } from './overviews/overviews.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { SideBarItemComponent } from './side-bar-item/side-bar-item.component';
import { ChangePasswordModule } from '../change-password/change-password.module';


@NgModule({
  declarations: [DashboardComponent, SideBarItemComponent],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    MaterialModule,
    ConfirmModule,
    ChangePasswordModule
  ]
})
export class DashboardModule { }
