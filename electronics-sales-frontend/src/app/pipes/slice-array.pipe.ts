import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sliceArray'
})
export class SliceArrayPipe implements PipeTransform {

  transform(value: Array<any>, start = 0, end = value.length): any {
    if (!value) {
      return null;
    }

    return value.filter((e, i) => {
      return i >= start && i <= end;
    });
  }

}
