import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HProductsComponent } from './h-products.component';

const routes: Routes = [{ path: '', component: HProductsComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HProductsRoutingModule { }
