import { tap, map } from 'rxjs/operators';
import { StatistycalType } from './../../../models/types/statistical.type';
import {
  StatisticalService,
  StatisticalOption
} from './../../../services/statistical.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { RevenueStatisticalView } from './../../../models/view-model/revenue-statistical.model';
import { DataSource } from '@angular/cdk/table';

export class RevenueStatisticalDataSource extends DataSource<
  RevenueStatisticalView
> {
  private revenueStatisticals = new BehaviorSubject<RevenueStatisticalView[]>(
    []
  );

  totalElements = 0;

  constructor(private statisticalService: StatisticalService) {
    super();
  }

  connect(): Observable<
    RevenueStatisticalView[] | readonly RevenueStatisticalView[]
  > {
    return this.revenueStatisticals.asObservable();
  }

  disconnect(): void {
    this.revenueStatisticals.complete();
  }

  load(
    option: {
      statisticalType?: StatistycalType;
      pageable?: StatisticalOption;
    } = {}
  ) {
    this.statisticalService
      .getRevenueStatisticalBy(option.pageable, option.statisticalType)
      .pipe(
        tap(response => this.revenueStatisticals.next(response.content)),
        map(response => response.totalElements)
      )
      .subscribe(totalElements => (this.totalElements = totalElements));
  }
}
