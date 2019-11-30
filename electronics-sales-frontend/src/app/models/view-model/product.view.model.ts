import { tap } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';
import { DiscountType } from '../types/discount.type';
import { ProductService } from './../../services/product.service';
import { ParagraphDto } from './../dtos/paragraph.dto';
import { DiscountView } from './discount.view';
import { ImageView } from './image-data.view';

export interface IProduct {
  id: number;

  productName: string;

  price: number;

  quantity: number;

  createdTime?: Date;

  updatedTime?: Date;

  manufacturerId?: number;

  discountId?: number;

  discount?: DiscountView;
}
export class ProductView {
  constructor(
    public id: number,
    public productName: string,
    public price: number,
    public quantity: number,
    public productService: ProductService,
    public createdTime: Date,
    public updatedTime: Date,
    public manufacturerId: number,
    public discountId: number
  ) {
    this.fetchImages();
    this.fetchProductDescriptions();
    this.fetchDiscount();
  }

  get discount() {
    return this.Discount;
  }
  images$ = new BehaviorSubject<ImageView[]>([]);

  private Descriptions: ParagraphDto[];

  Discount: DiscountView;

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
      iProduct.discountId
    );
  }

  fetchProductDescriptions() {
    this.productService.getDescriptions(this.id).subscribe(descriptions => {
      this.Descriptions = descriptions;
    });
  }

  fetchDiscount() {
    if (!this.discountId) {
      return;
    }
    this.productService.getDiscount(this.discountId)
      .subscribe(v => this.Discount = v);
  }

  get descriptions() {
    return this.Descriptions;
  }

  fetchImages() {
    this.productService.getImages(this.id).subscribe({
      next: v => this.images$.next(v),
      complete: () => this.images$.complete()
    });
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
