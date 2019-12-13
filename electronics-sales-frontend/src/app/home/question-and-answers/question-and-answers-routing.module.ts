import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { QuestionAndAnswersComponent } from './question-and-answers.component';

const routes: Routes = [{ path: '', component: QuestionAndAnswersComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QuestionAndAnswersRoutingModule { }
