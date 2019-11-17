import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DCategoryComponent } from './d-category.component';

const routes: Routes = [{ path: '', component: DCategoryComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DCategoryRoutingModule { }
