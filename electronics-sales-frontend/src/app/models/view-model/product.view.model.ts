import { DiscountView } from './discount.view';
export  interface ProductView {

  id: number;

  productName: string;

  imageIds?: number[];

  price?: number;

  quantity?: number;

  categoryIds?: number[];

  manufacturerId?: number;

  discount?: DiscountView;

}
