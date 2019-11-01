import { PipeTransform, Pipe } from '@angular/core';
import { CurrencyPipe } from '@angular/common';

const COMMA = ',';

const PERIOD = '.';

const CURRENCY_CODE = 'VND';

@Pipe({ name: 'currencyvn' })
export class CurrencyVNPipe extends CurrencyPipe implements PipeTransform {
  transform(value: string, ...args: any[]) {
    const currencyValue = super.transform(value, CURRENCY_CODE, ...args);
    let currencyVn = this.replaceAll(currencyValue, COMMA, PERIOD);
    const priceFirstIndex = 1;
    const priceEndIndex = currencyVn.length - 1;
    const priceOnly = currencyVn.substr(priceFirstIndex, priceEndIndex);
    const currencyPrefix = currencyVn.substr(0, priceFirstIndex);
    currencyVn = `${priceOnly} ${currencyPrefix}`;
    return currencyVn;
  }

  replaceAll(value: string, searchValue: string, replaceValue: string): string {
    let newValue = `${value}`;
    while (newValue.search(searchValue) >= 0) {
      newValue = newValue.replace(searchValue, replaceValue);
    }
    return newValue;
  }
}
