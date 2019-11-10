import { ChooseImagesModule } from './../choose-images/choose-images.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MultiChooseImagesComponent } from './multi-choose-images.component';



@NgModule({
  declarations: [MultiChooseImagesComponent],
  imports: [
    CommonModule,
    ChooseImagesModule
  ],
  providers: [],
  exports: [MultiChooseImagesComponent]
})
export class MultiChooseImagesModule { }
