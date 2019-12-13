import { map } from 'rxjs/operators';
import { Page } from './../models/page.model';
import { TransactionDataView } from './../models/view-model/transaction-data.view.model';
import { SortType } from './../models/types/sort-type.type';
import { TransactionDetailedView } from './../models/view-model/transaction-detailed.view';
import { TransactionDto } from './../models/dtos/transaction.dto';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TransactionDataViewColumn } from '../models/view-model/transaction-data.view.model';
import { formatDate } from '@angular/common';

const BASE_REQUEST = '/api/transactions';

const DEFAULT_PAGE = 0;

const DEFAULT_SIZE = 10;

const DEFAULT_COLUMN_SORT = TransactionDataViewColumn.CREATEDTIME;

const DEFAULT_SORT_DIRECTION = SortType.DESC;

const DEFAUT_FETCH_OPTION: TransactionFetchOption = {
  pageable: {
    page: DEFAULT_PAGE,
    size: DEFAULT_SIZE,
    sort: DEFAULT_COLUMN_SORT,
    sortDirection: DEFAULT_SORT_DIRECTION
  }
};

const FORMAT_DATE_PATTERN = 'yyyy-MM-dd';

const LOCALE = 'en-US';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  constructor(private http: HttpClient) {}

  create(transactionDto: TransactionDto): Observable<TransactionDto> {
    return this.http.post<TransactionDto>(BASE_REQUEST, transactionDto);
  }

  fetchTransactionDetailed(
    transactionId: number
  ): Observable<TransactionDetailedView[]> {
    return this.http.get<TransactionDetailedView[]>(
      `${BASE_REQUEST}/${transactionId}/transaction-detaileds`
    );
  }

  fetchAll(
    option: TransactionFetchOption = {}
  ): Observable<Page<TransactionDataView>> {
    const defaultPageable = { ...DEFAUT_FETCH_OPTION.pageable };
    const pageable = option.pageable
      ? { ...defaultPageable, ...option.pageable }
      : defaultPageable;
    const { conditions } = option;
    const { page, size, sort, sortDirection } = pageable;

    const pageableParams = {
      page: page.toString(),
      size: size.toString(),
      sort: `${sort},${sortDirection}`
    };

    const conditionParams: any = {};
    if (conditions) {
      const { fromDate, toDate } = conditions;
      if (fromDate) {
        conditionParams.fromDate = formatDate(
          fromDate,
          FORMAT_DATE_PATTERN,
          LOCALE
        );
      }

      if (toDate) {
        conditionParams.toDate = formatDate(
          toDate,
          FORMAT_DATE_PATTERN,
          LOCALE
        );
      }
    }

    return this.http
      .get<Page<TransactionDataView>>(BASE_REQUEST, {
        params: {
          ...pageableParams,
          ...conditionParams
        }
      })
      .pipe(
        map(p => {
          p.content.forEach(v => (v.createdTime = new Date(v.createdTime)));
          return p;
        })
      );
  }

  fetchBy(
    customerId: number,
    option: TransactionFetchOption = {}
  ): Observable<Page<TransactionDataView>> {
    const defaultPageable = { ...DEFAUT_FETCH_OPTION.pageable };
    const pageable = option.pageable
      ? { ...defaultPageable, ...option.pageable }
      : defaultPageable;
    const { conditions } = option;
    const { page, size, sort, sortDirection } = pageable;

    const pageableParams = {
      page: page.toString(),
      size: size.toString(),
      sort: `${sort},${sortDirection}`
    };

    const conditionParams: any = {};
    if (conditions) {
      const { fromDate, toDate } = conditions;
      if (fromDate) {
        conditionParams.fromDate = formatDate(
          fromDate,
          FORMAT_DATE_PATTERN,
          LOCALE
        );
      }

      if (toDate) {
        conditionParams.toDate = formatDate(
          toDate,
          FORMAT_DATE_PATTERN,
          LOCALE
        );
      }
    }

    return this.http
      .get<Page<TransactionDataView>>(BASE_REQUEST, {
        params: {
          customerId,
          ...pageableParams,
          ...conditionParams
        }
      })
      .pipe(
        map(p => {
          p.content.forEach(v => (v.createdTime = new Date(v.createdTime)));
          return p;
        })
      );
  }
}

export interface TransactionFetchOption {
  conditions?: {
    fromDate?: Date;

    toDate?: Date;
  };

  pageable?: {
    page?: number;

    size?: number;

    sort?: TransactionDataViewColumn;

    sortDirection?: SortType;
  };
}
