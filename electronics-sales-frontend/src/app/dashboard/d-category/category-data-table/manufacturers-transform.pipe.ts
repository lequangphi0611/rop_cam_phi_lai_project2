import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'manufacturersTransform'
})
export class ManufacturersTransformPipe implements PipeTransform {

  transform(value: ManufacturerView[]): any {
    if(!value) {
      return value;
    }

    return value.map(v => v.manufacturerName);
  }

}
