import { DataSource } from '@angular/cdk/table';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { ImportInvoiceReportView } from './../../../models/view-model/import-invoice-report.view.model';
import { ImportInvoiceReportOption, StatisticalService } from './../../../services/statistical.service';

export class ImportInvoiceReportDataSource extends DataSource<ImportInvoiceReportView> {
  private importInvoiceReports = new BehaviorSubject<ImportInvoiceReportView[]>(
    []
  );

  // tslint:disable-next-line: variable-name
  private _totalElements = 0;

  constructor(private statisticalService: StatisticalService) {
    super();
  }

  connect(): Observable<
    ImportInvoiceReportView[] | readonly ImportInvoiceReportView[]
  > {
    return this.importInvoiceReports.asObservable();
  }

  disconnect(): void {
    this.importInvoiceReports.complete();
  }

  load(option?: ImportInvoiceReportOption) {
    this.statisticalService
      .getImportInvoceReports(option)
      .pipe(
        tap(result => this.importInvoiceReports.next(result.content)),
        map(result => result.totalElements),
        tap(totalElements => (this._totalElements = totalElements))
      )
      .subscribe();
  }

  get totalElements() {
    return this._totalElements;
  }
}
