import { TransactionService } from './../../services/transaction.service';
import { map, switchMap, takeUntil } from 'rxjs/operators';
import { Observable, Subject, of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { TransactionDetailedView } from 'src/app/models/view-model/transaction-detailed.view';

@Component({
  selector: 'app-order-success',
  templateUrl: './order-success.component.html',
  styleUrls: ['./order-success.component.css']
})
export class OrderSuccessComponent implements OnInit, OnDestroy {

  transactionId$: Observable<string> = of();

  transactionDetailed: TransactionDetailedView[] = [];

  unscriptions$ = new Subject();

  constructor(
    private route: ActivatedRoute,
    private transactionService: TransactionService
  ) { }

  ngOnInit() {
    this.transactionIdSubcription();
    this.transactionDetailedScriptions();
  }

  transactionIdSubcription() {
    this.transactionId$ = this.route.params
      .pipe(map(params => params.transactionId));
  }

  transactionDetailedScriptions() {
    this.transactionId$.pipe(
      takeUntil(this.unscriptions$),
      switchMap(id => this.transactionService.fetchTransactionDetailed(+id))
    ).subscribe(result => this.transactionDetailed = result);
  }

  get sumTotal() {
    return this.transactionDetailed
      .reduce((total, current) => total + current.total , 0);
  }

  ngOnDestroy(): void {
    this.unscriptions$.next();
    this.unscriptions$.complete();
  }

}
