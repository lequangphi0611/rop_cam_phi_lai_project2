import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'paramterTypeTransform'
})
export class ParamterTypeTransformPipe implements PipeTransform {

  transform(value: ParameterTypeDto[]): any {
    if (!value) {
      return null;
    }
    return value.map(parameterType => parameterType.parameterTypeName);
  }

}
