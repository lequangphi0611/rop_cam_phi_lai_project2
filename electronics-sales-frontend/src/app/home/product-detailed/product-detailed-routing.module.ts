import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProductDetailedComponent } from './product-detailed.component';

const routes: Routes = [{ path: '', component: ProductDetailedComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductDetailedRoutingModule { }
