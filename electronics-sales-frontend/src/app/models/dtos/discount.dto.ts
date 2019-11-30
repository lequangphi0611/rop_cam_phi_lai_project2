import { DiscountType } from './../types/discount.type';
export interface DiscountDto {

  id?: number;

  discountType: DiscountType;

  discountValue: number;

  productIds: number[];

}
