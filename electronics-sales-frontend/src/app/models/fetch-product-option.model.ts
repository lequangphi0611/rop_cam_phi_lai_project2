import { ProductSortType } from './types/product-sort-type.type';
import { FetchProductType } from './types/fetch-product-type.type';
import { SortType } from './types/sort-type.type';

export interface FetchProductOption {

  categoriesId?: number[];

  manufacturersId?: number[];

  fromPrice?: number;

  toPrice?: number;

  search?: string;

  fetchDiscount?: boolean;

  sortType?: SortType;

  page?: number;

  size?: number;

  fetchType?: FetchProductType;

  productSortType?: ProductSortType;

}
