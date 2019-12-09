import { Pipe, PipeTransform } from '@angular/core';

const DATA_BASE64_PREFIX = 'data:image/png;base64,';

@Pipe({
  name: 'imageBase64'
})
export class ImageBase64Pipe implements PipeTransform {

  transform(value: string, ...args: string[]): any {
    if (!value) {
      return args[0];
    }

    return `${DATA_BASE64_PREFIX}${value}`;
  }

}
