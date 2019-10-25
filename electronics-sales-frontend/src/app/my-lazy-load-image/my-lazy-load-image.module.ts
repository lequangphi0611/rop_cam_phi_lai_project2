import { LazyLoadImageModule, scrollPreset } from 'ng-lazyload-image';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyLazyLoadImageComponent } from './my-lazy-load-image.component';



@NgModule({
  declarations: [MyLazyLoadImageComponent],
  imports: [
    CommonModule,
    LazyLoadImageModule.forRoot({
      preset: scrollPreset // <-- tell LazyLoadImage that you want to use scrollPreset
    })
  ],
  exports: [MyLazyLoadImageComponent]
})
export class MyLazyLoadImageModule { }
