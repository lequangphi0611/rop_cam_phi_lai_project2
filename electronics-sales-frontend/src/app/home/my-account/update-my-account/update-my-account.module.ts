import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './../../../material/material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UpdateMyAccountRoutingModule } from './update-my-account-routing.module';
import { UpdateMyAccountComponent } from './update-my-account.component';
import { ChooseAvartarComponent } from './choose-avartar/choose-avartar.component';


@NgModule({
  declarations: [UpdateMyAccountComponent, ChooseAvartarComponent],
  imports: [
    CommonModule,
    UpdateMyAccountRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class UpdateMyAccountModule { }
