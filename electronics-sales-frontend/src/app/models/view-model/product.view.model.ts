import { DiscountView } from './discount.view';
import { DiscountType } from '../types/discount.type';

export interface IProduct {

    id: number;

    productName: string;

    price: number;

    quantity: number;

    createdTime?: Date;

    updatedTime?: Date;

    manufacturerId?: number;

    discount?: DiscountView;

}
export class ProductView {
  constructor(
    public id: number,
    public productName: string,
    public price: number,
    public quantity: number,
    public createdTime?: Date,
    public updatedTime?: Date,
    public manufacturerId?: number,
    public discount?: DiscountView
  ) {}

  static of(iProduct: IProduct): ProductView {
    return new ProductView(
      iProduct.id,
      iProduct.productName,
      iProduct.price,
      iProduct.quantity,
      iProduct.createdTime,
      iProduct.updatedTime,
      iProduct.manufacturerId,
      iProduct.discount
    );
  }

  getCurrentPrice(): number {
    return this.price - this.getDiscountPrice();
  }

  getDiscountPrice(): number {
    if (!this.discount) {
      return 0;
    }

    return this.discount.discountType === DiscountType.AMOUNT
      ? this.discount.discountValue
      : (this.price * this.discount.discountValue) / 100;
  }
}
