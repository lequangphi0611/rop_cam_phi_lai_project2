import { ManagerGuard } from './../manager.guard';
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
          import('./overviews/overviews.module').then(m => m.OverviewsModule)
      },
      {
        path: 'product',
        loadChildren: () =>
          import('./products/products.module').then(m => m.ProductsModule)
      },
      {
        path: 'manufacturer',
        loadChildren: () =>
          import('./manufacturer/manufacturer.module').then(
            m => m.ManufacturerModule
          )
      },
      {
        path: 'category',
        loadChildren: () =>
          import('./d-category/d-category.module').then(m => m.DCategoryModule)
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'overview'
      },
      {
        path: 'transactions',
        canActivate: [ManagerGuard],
        loadChildren: () =>
          import('./transaction/transaction.module').then(
            m => m.TransactionModule
          )
      },
      {
        path: 'employees',
        canActivate: [ManagerGuard],
        loadChildren: () =>
          import('./employees/employees.module').then(m => m.EmployeesModule)
      },
      {
        path: 'discounts',
        loadChildren: () =>
          import('./discount/discount.module').then(m => m.DiscountModule)
      },
      {
        path: 'my-account',
        loadChildren: () =>
          import(
            '../home/my-account/update-my-account/update-my-account.module'
          ).then(m => m.UpdateMyAccountModule)
      },
      {
        path: 'statistical',
        canActivate: [ManagerGuard],
        loadChildren: () =>
          import('./statistical/statistical.module').then(
            m => m.StatisticalModule
          )
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {}
