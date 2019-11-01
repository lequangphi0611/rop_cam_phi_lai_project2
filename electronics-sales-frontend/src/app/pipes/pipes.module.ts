import { CurrencyVNPipe } from './currency-vn.pipe';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiscountPipe } from './discount.pipe';
import { CutStringPipe } from './cut-string.pipe';

@NgModule({
  declarations: [CurrencyVNPipe, DiscountPipe, CutStringPipe],
  imports: [CommonModule],
  exports: [CurrencyVNPipe, DiscountPipe, CutStringPipe],
})
export class PipesModule {}
