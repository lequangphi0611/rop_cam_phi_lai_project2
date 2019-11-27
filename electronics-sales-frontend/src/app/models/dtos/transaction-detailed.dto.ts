import { DiscountType } from './../types/discount.type';
export interface TransactionDetailedDto {

  productId: number;

  quantity: number;

  price: number;

  discountType: DiscountType;

  discountValue: number;

}
