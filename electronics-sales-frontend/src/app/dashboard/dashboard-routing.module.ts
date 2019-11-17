import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
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
        path: 'manufacturer',
        loadChildren: () =>
          import('./manufacturer/manufacturer.module').then(
            m => m.ManufacturerModule
          ),
      },
      {
        path: 'category',
        loadChildren: () =>
          import('./d-category/d-category.module').then(m => m.DCategoryModule),
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'overview',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
