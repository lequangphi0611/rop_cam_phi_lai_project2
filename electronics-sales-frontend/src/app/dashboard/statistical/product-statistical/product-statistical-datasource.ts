import { Pageable } from './../../../models/pageable';
import { BehaviorSubject } from 'rxjs';
import { ProductsStatisticalView } from './../../../models/view-model/product-statistical.view';
import { DataSource } from '@angular/cdk/table';
import { StatisticalService } from 'src/app/services/statistical.service';
import { tap, map } from 'rxjs/operators';


export class ProductStatisticalDataSource extends DataSource<ProductsStatisticalView> {

  private data = new BehaviorSubject<ProductsStatisticalView[]>([]);

  private _totalElements = 0;

  constructor(private statisticalService: StatisticalService) {
    super();
  }

  connect() {
    return this.data.asObservable();
  }

  disconnect() {
    this.data.complete();
  }

  load(pageable: Pageable) {
    this.statisticalService
      .getProductStatistical(pageable)
      .pipe(
        tap(page => this._totalElements = page.totalElements),
        map(page => page.content),
        tap(result => this.data.next(result))
      ).subscribe();
  }

  get totalElements() {
    return this._totalElements;
  }

}
