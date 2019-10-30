import { CurrencyVNPipe } from './currency-vn.pipe';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [CurrencyVNPipe],
  imports: [
    CommonModule
  ],
  exports: [CurrencyVNPipe]
})
export class PipesModule { }
