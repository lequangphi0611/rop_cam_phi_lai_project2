import { PipeTransform, Pipe } from '@angular/core';

const COMMA = ',';

const PERIOD = '.';

@Pipe({ name: 'currencyvn' })
export class CurrencyVNPipe implements PipeTransform {
  transform(value: string, ...args: any[]) {
    let newValue = this.replaceAll(value, COMMA, PERIOD);
    newValue = `${newValue.substr(1, newValue.length - 1)} ${newValue.substr(
      0,
      1
    )}`;
    return newValue;
  }

  replaceAll(value: string, searchValue: string, replaceValue: string): string {
    let newValue = `${value}`;
    while (newValue.search(searchValue) >= 0) {
      newValue = newValue.replace(searchValue, replaceValue);
    }
    return newValue;
  }
}
