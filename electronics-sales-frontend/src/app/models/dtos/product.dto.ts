import { ProductParameterDto } from './product-parameter.dto';
import { ParagraphDto } from './paragraph.dto';
export interface ProductDto {

  id?: number;

  productName: string;

  price: number;

  categoryIds: number[];

  manufacturerId: number;

  paragraphs: ParagraphDto[];

  productParameters: ProductParameterDto[];

  images: File[];

}
