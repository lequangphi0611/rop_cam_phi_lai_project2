import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { UpdateMyAccountComponent } from './update-my-account.component';

const routes: Routes = [{ path: '', component: UpdateMyAccountComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UpdateMyAccountRoutingModule { }
