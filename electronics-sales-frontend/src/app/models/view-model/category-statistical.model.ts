export interface CategoryStatistical {

  categoryStatisticals: {
    categoryName: string,
    productCount: number,
    totalProductSold: number
  }[];

  totalProductSold: number;
}
