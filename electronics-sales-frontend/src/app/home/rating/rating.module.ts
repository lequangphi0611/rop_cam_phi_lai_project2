import { RatingComponent } from './rating.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [RatingComponent],
  imports: [
    CommonModule
  ],
  exports: [RatingComponent]
})
export class RatingModule { }
