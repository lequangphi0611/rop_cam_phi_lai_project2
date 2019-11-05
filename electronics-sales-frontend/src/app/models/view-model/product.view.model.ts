import { DiscountView } from './discount.view';
import { DiscountType } from '../types/discount.type';
export class ProductView {
  constructor(
    public id: number,
    public productName: string,
    public price: number,
    public quantity: number,
    public manufacturerId?: number,
    public discount?: DiscountView
  ) {}

  static of(iProduct: {
    id: number;
    productName: string;
    price: number;
    quantity: number;
    manufacturerId?: number;
    discount?: DiscountView;
  }): ProductView {
    return new ProductView(
      iProduct.id,
      iProduct.productName,
      iProduct.price,
      iProduct.quantity,
      iProduct.manufacturerId,
      iProduct.discount
    );
  }

  getCurrentPrice(): number {
    if (!this.discount) {
      return this.price;
    }

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
