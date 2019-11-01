import { Pipe, PipeTransform } from '@angular/core';

const DEFAULT_LENGTH = 50;

@Pipe({
  name: 'cutString'
})
export class CutStringPipe implements PipeTransform {

  transform(value: string, length = DEFAULT_LENGTH): any {
    if (value.length <= length) {
      return value;
    }
    const start = 0;
    const valueCut = value.substr(start, length).trim();
    return `${valueCut}...`;
  }

}
