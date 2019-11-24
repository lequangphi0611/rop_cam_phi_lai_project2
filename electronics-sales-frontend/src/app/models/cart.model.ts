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

  set(product: ProductView, quantity: number): boolean {
    if (product.quantity <= 0) {
      return false;
    }
    if (product.quantity < quantity) {
      quantity = product.quantity;
    }
    const cartItemIndex = this.indexOf({productId: product.id, quantity});
    const cartItem = {productId: product.id, quantity};
    if (cartItemIndex < 0 && quantity > 0) {
      this.cartItems.push(cartItem);
      return true;
    }

    if (cartItem.quantity <= 0) {
      this.cartItems.splice(cartItemIndex, 1);
      return true;
    }

    this.cartItems[cartItemIndex].quantity = quantity;
    return true;
  }

  push (product: ProductView, quantity: number): boolean {
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
    if (+oldCartItem.quantity + quantity > product.quantity) {
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

  public remove(cartItem: CartItem) {
    const index = this.indexOf(cartItem);
    this.cartItems.splice(index, 1);
  }

  private updateCartItemIn(index: number, quantity: number) {
    this.cartItems[index].quantity += quantity;
    return this.cartItems[index];
  }

}
