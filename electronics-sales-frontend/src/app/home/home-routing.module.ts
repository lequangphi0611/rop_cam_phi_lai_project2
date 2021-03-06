import { AuthenticatedGuard } from './../authenticated.guard';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
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
          )
      },
      {
        path: 'login',
        loadChildren: () =>
          import('./login/login.module').then(m => m.LoginModule)
      },
      {
        path: 'register',
        loadChildren: () =>
          import('./register/register.module').then(m => m.RegisterModule)
      },
      {
        path: 'about',
        loadChildren: () =>
          import('./about/about.module').then(m => m.AboutModule)
      },
      {
        path: 'cart',
        loadChildren: () => import('./cart/cart.module').then(m => m.CartModule)
      },
      {
        path: 'product/:id',
        loadChildren: () =>
          import('./product-detailed/product-detailed.module').then(
            m => m.ProductDetailedModule
          )
      },
      {
        path: 'products',
        loadChildren: () =>
          import('./h-products/h-products.module').then(m => m.HProductsModule)
      },
      {
        path: 'checkout',
        loadChildren: () =>
          import('./checkout/checkout.module').then(m => m.CheckoutModule)
      },
      {
        path: 'order-success/:transactionId',
        loadChildren: () =>
          import('./order-success/order-success.module').then(
            m => m.OrderSuccessModule
          )
      },
      {
        path: 'my-account',
        canActivate: [AuthenticatedGuard],
        loadChildren: () =>
          import('./my-account/my-account.module').then(m => m.MyAccountModule)
      },
      {
        path: 'question-and-answers',
        loadChildren: () =>
          import('./question-and-answers/question-and-answers.module').then(
            m => m.QuestionAndAnswersModule
          )
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {}
