import { AppComponent } from './app.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboarGuardGuard } from './dashboar-guard.guard';

const routes: Routes = [
  {
    path: 'index',
    loadChildren: () => import('./home/home.module').then(m => m.HomeModule),
  },
  {
    path: 'dashboard',
    canActivate: [DashboarGuardGuard],
    loadChildren: () =>
      import('./dashboard/dashboard.module').then(m => m.DashboardModule),
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'index'
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
