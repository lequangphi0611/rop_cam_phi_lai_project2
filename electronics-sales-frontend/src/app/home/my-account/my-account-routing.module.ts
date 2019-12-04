import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MyAccountComponent } from './my-account.component';

const routes: Routes = [
  {
    path: '',
    component: MyAccountComponent,
    children: [
      {
        path: 'update',
        loadChildren: () =>
          import('./update-my-account/update-my-account.module').then(
            m => m.UpdateMyAccountModule
          )
      },
      {
        path: 'transaction-history',
        loadChildren: () =>
          import('./transaction-history/transaction-history.module').then(
            m => m.TransactionHistoryModule
          )
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MyAccountRoutingModule {}
