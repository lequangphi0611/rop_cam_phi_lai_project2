import { OverviewsComponent } from './overviews/overviews.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './dashboard.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children: [
      {
        path: 'overview',
        loadChildren: () =>
          import('./overviews/overviews.module').then(m => m.OverviewsModule),
      },
      {
        path: 'product',
        loadChildren: () =>
          import('./products/products.module').then(m => m.ProductsModule),
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'overview'
      }
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
