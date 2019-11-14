import { ImageView } from './image-data.view';
import { Observable } from 'rxjs';
import { ProductService } from './../../services/product.service';
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

  images$: Observable<ImageView[]>;

  discount$: Observable<DiscountView>;

  constructor(
    public id: number,
    public productName: string,
    public price: number,
    public quantity: number,
    public productService: ProductService,
    public createdTime?: Date,
    public updatedTime?: Date,
    public manufacturerId?: number,
    public discount?: DiscountView,
  ) {
    this.images$ = this.productService.getImages(this.id);
    this.discount$ = this.productService.getDiscount(this.id);
  }

  static of(iProduct: IProduct, productService: ProductService): ProductView {
    return new ProductView(
      iProduct.id,
      iProduct.productName,
      iProduct.price,
      iProduct.quantity,
      productService,
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
