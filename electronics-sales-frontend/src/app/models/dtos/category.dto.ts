import { ParameterTypeDto } from './paramter-type.dto';
export interface CategoryDto {

  id?: number;

  categoryName: string;

  parameterTypes: ParameterTypeDto[];

  manufacturerIds: number[];

  parentId?: number;

}
