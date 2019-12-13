import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { QuestionAndAnswersRoutingModule } from './question-and-answers-routing.module';
import { QuestionAndAnswersComponent } from './question-and-answers.component';


@NgModule({
  declarations: [QuestionAndAnswersComponent],
  imports: [
    CommonModule,
    QuestionAndAnswersRoutingModule
  ]
})
export class QuestionAndAnswersModule { }
