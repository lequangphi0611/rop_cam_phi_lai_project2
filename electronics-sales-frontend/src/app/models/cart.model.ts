import { ProductView } from './view-model/product.view.model';
import { CartItem } from './cart-item.model';

export class Cart {

  constructor(public cartItems: CartItem[]) {}

  indexOf(cartItem: CartItem): number {
    if (this.cartItems.length === 0 ) {
      return -1;
    }
    return this.cartItems
      .map(c => c.productId)
      .findIndex(productId => productId === cartItem.productId);
  }

  push (product: ProductView, quantity: number): boolean {
    console.log('add cart', product, quantity);
    if (product.quantity <= 0) {
      return false;
    }

    const cartItemIndex = this.indexOf({productId: product.id, quantity});
    const cartItem = {productId: product.id, quantity};
    if (cartItemIndex < 0) {
      this.cartItems.push(cartItem);
      return true;
    }

    if (cartItem.quantity < 0) {
      this.reduceOf(cartItemIndex, quantity);
      return true;
    }

    const oldCartItem = this.cartItems[cartItemIndex];
    if (oldCartItem.quantity + quantity > product.quantity) {
      return false;
    }
    this.increaseOf(cartItemIndex, quantity);
    return true;
  }

  private reduceOf(index: number, quantityReduced: number) {
    const cartItem = this.updateCartItemIn(index, quantityReduced);
    if (cartItem.quantity <= 0) {
      this.cartItems.splice(index, 1);
    }
  }

  private increaseOf(index: number, quantityIncreased: number) {
    this.updateCartItemIn(index, quantityIncreased);
  }

  private updateCartItemIn(index: number, quantity: number) {
    this.cartItems[index].quantity += quantity;
    return this.cartItems[index];
  }

}
