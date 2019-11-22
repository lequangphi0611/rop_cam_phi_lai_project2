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

  push(cartItem: CartItem) {
    const {quantity} = cartItem;
    const cartItemIndex = this.indexOf(cartItem);
    console.log({cartItemIndex});
    if (cartItemIndex < 0) {
      this.cartItems.push(cartItem);
      return;
    }

    if (cartItem.quantity < 0) {
      this.reduceOf(cartItemIndex, quantity);
      return;
    }

    this.increaseOf(cartItemIndex, quantity);
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
