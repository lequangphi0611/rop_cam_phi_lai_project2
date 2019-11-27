export interface TransactionDataView {

  id: number;

  createdTime: Date;

  fullname: string;

  email: string;

  phoneNumber: string;

  address: string;

  subTotal: number;

  discountTotal: number;

  sumTotal: number;

}

export enum TransactionDataViewColumn {

  ID = 'id',

  CREATEDTIME = 'createdTime',

  FULLNAME = 'fullname',

  EMAIL = 'email',

  PHONENUMBER = 'phoneNumber',

  ADDRESS = 'address',

  SUBTOTAL = 'subTotal',

  DISCOUNTTOTAL = 'discountTotal',

  SUMTOTAL = 'sumToTal'

}
