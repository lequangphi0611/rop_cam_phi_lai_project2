import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'join',
})
export class JoinPipe implements PipeTransform {
  transform(value: Array<any>, separator = ', '): any {
    if (!value) {
      return '';
    }
    return value.join(separator);
  }
}
