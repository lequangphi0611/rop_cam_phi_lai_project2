import { CurrencyVNPipe } from './currency-vn.pipe';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiscountPipe } from './discount.pipe';
import { CutStringPipe } from './cut-string.pipe';
import { SliceArrayPipe } from './slice-array.pipe';

@NgModule({
  declarations: [CurrencyVNPipe, DiscountPipe, CutStringPipe, SliceArrayPipe],
  imports: [CommonModule],
  exports: [CurrencyVNPipe, DiscountPipe, CutStringPipe, SliceArrayPipe],
})
export class PipesModule {}
