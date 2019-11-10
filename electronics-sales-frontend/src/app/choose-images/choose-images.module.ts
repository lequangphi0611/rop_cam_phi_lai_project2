import { MatTooltipModule } from '@angular/material/tooltip';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChooseImagesComponent } from './choose-images.component';



@NgModule({
  declarations: [ChooseImagesComponent],
  imports: [
    CommonModule,
    MatTooltipModule
  ],
  exports: [ChooseImagesComponent],
  entryComponents: [ChooseImagesComponent]
})
export class ChooseImagesModule { }
