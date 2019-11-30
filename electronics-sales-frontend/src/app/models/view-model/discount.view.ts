import { DiscountType } from './../types/discount.type';


export interface DiscountView {

  id: number;

  discountValue: number;

  discountType: DiscountType;

  startedTime: Date;

  productCount?: number;

}
