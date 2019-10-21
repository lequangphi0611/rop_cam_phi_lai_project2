import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./home-content-default/home-content-default.module').then(
            m => m.HomeContentDefaultModule
          ),
      },
      {
        path: 'login',
        loadChildren: () =>
          import('./login/login.module').then(m => m.LoginModule),
      },
      {
        path: 'register',
        loadChildren: () =>
          import('./register/register.module').then(m => m.RegisterModule),
      },
      {
        path: 'about',
        loadChildren: () =>
          import('./about/about.module').then(m => m.AboutModule),
      },
      {
        path: 'cart',
        loadChildren: () =>
          import('./cart/cart.module').then(m => m.CartModule),
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {}
