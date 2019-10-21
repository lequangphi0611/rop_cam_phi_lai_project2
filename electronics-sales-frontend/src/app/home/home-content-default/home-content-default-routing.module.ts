import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeContentDefaultComponent } from './home-content-default.component';

const routes: Routes = [{ path: '', component: HomeContentDefaultComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeContentDefaultRoutingModule { }
