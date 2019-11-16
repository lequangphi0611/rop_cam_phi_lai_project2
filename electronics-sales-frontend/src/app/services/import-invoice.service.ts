import { ImportInvoiceDto } from './../models/dtos/import-invoice.dto';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ImportInvoiceService {

  static readonly BASE_REQUEST = '/api/import-invoices'

  constructor(private http: HttpClient) { }

  create(importInvoiceDto: ImportInvoiceDto) {
    return this.http.post<ImportInvoiceDto>(`${ImportInvoiceService.BASE_REQUEST}`, importInvoiceDto);
  }
}
