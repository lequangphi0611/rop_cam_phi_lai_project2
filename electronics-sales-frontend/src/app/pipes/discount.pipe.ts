import { DiscountView } from './../models/view-model/discount.view';
import { Pipe, PipeTransform } from '@angular/core';
import { CurrencyVNPipe } from './currency-vn.pipe';
import { DiscountType } from '../models/types/discount.type';

const PERCENT_PREFIX = '%';

@Pipe({
  name: 'discount',
})
export class DiscountPipe implements PipeTransform {
  private currencyVN: CurrencyVNPipe;

  constructor() {
    this.currencyVN = new CurrencyVNPipe('en-EN');
  }

  transform(value: DiscountView): any {
    if (!value) {
      return ``;
    }

    if (value.discountType === DiscountType.PERCENT) {
      return `${value.discountValue} ${PERCENT_PREFIX}`;
    }

    return `${this.currencyVN.transform(`${value.discountValue}`)}`;
  }
}
