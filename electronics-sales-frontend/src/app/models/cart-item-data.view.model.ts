import { map, switchMap } from 'rxjs/operators';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { ProductService } from './../services/product.service';
import { CartItem } from './cart-item.model';
import { Observable } from 'rxjs';
export class CartDataView {
  product: ProductView;

  image: string;

  constructor(
    public productId: number,
    public quantity: number,
    private productService: ProductService
  ) {
    this.productService.getProduct(productId)
      .subscribe(v => this.product = v);
    this.productService.getImages(this.productId).pipe(
      map(images => images[0]),
      map(image => image.data)
    ).subscribe(v => this.image = v);
  }

  static of(cartItem: CartItem, productService: ProductService): CartDataView {
    const { productId, quantity } = cartItem;
    return new CartDataView(productId, quantity, productService);
  }

  get total(): number {
    if (!this.product) {
      return 0;
    }
    return this.product.price * this.quantity;
  }
}
