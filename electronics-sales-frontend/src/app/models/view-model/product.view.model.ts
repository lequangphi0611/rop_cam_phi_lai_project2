import { DiscountView } from './discount.view';
import { DiscountType } from '../types/discount.type';
export class ProductView {

  id: number;

  productName: string;

  imageIds: number[];

  price: number;

  quantity: number;

  categoryIds: number[];

  manufacturerId: number;

  discount: DiscountView;

  constructor(
    id: number,
    productName: string,
    imageIds: number[],
    price: number,
    quantity: number,
    categoryIds?: number[],
    manufacturerId?: number,
    discount?: DiscountView
  ) {
    this.id = id;
    this.productName = productName;
    this.imageIds = imageIds;
    this.price = price;
    this.quantity = quantity;
    this.categoryIds = categoryIds;
    this.manufacturerId = manufacturerId;
    this.discount = discount;
  }

  static of(iProduct: {
    id: number;
    productName: string;
    imageIds: number[];
    price: number;
    quantity: number;
    categoryIds?: number[];
    manufacturerId?: number;
    discount?: DiscountView;
  }): ProductView {
    return new ProductView(
      iProduct.id,
      iProduct.productName,
      iProduct.imageIds,
      iProduct.price,
      iProduct.quantity,
      iProduct.categoryIds,
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
