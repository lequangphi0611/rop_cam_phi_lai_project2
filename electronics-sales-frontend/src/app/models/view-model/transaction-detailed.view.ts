import { DiscountType } from './../types/discount.type';
export interface TransactionDetailedView {

  id: number;

  image: string;

  productName: string;

  price: number;

  quantity: number;

  discountType: DiscountType;

  discountValue: number;

  total: number;

}
