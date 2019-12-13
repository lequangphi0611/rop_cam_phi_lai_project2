import { tap, map } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';
import { UserAuthenticatedService } from './../../../services/user-authenticated.service';
import { TransactionService, TransactionFetchOption } from './../../../services/transaction.service';
import { DataSource } from '@angular/cdk/table';
import { TransactionDataView } from 'src/app/models/view-model/transaction-data.view.model';


export class TransactionHistoryDataSource extends DataSource<TransactionDataView> {

  private data = new BehaviorSubject<TransactionDataView[]>([]);

  private _totalElements = 0;

  constructor(private transactionService: TransactionService, private userData: UserAuthenticatedService) {
    super();
  }

  get userInfoId() {
    return this.userData.value.userInfoId;
  }

  connect() {
    return this.data.asObservable();
  }

  disconnect() {
    this.data.complete();
  }

  get totalElements() {
    return this._totalElements;
  }

  load(option?: TransactionFetchOption) {
    this.transactionService.fetchBy(this.userInfoId, option)
      .pipe(
        tap(page => this._totalElements = page.totalElements),
        map(page => page.content),
        tap(result => this.data.next(result))
      ).subscribe();
  }

}
