import { ManufacturerView } from './../models/view-model/manufacturer.view.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ManufacturerDto } from '../models/dtos/manufacturer.dto';

@Injectable({
  providedIn: 'root'
})
export class ManufacturerService {

  static readonly BASE_REQUEST = '/api/manufacturers';

  constructor(private http: HttpClient) { }

  private buildFormData(manufacturer: ManufacturerDto, file?: File): FormData {
    const formData = new FormData();
    formData.append('manufacturer', JSON.stringify(manufacturer));
    if (file) {
      formData.append('logo', file);
    }
    return formData;
  }

  create(manufacturer: ManufacturerDto, logo?: File): Observable<ManufacturerView> {
    const body = this.buildFormData(manufacturer, logo);
    return this.http.post<ManufacturerView>(`${ManufacturerService.BASE_REQUEST}`, body);
  }

  update(manufacturer: ManufacturerDto, logo?: File): Observable<ManufacturerView> {
    const body = this.buildFormData(manufacturer, logo);
    return this.http.put<ManufacturerView>(`${ManufacturerService.BASE_REQUEST}/${manufacturer.id}`, body);
  }

  delete(manufacturerId: number): Observable<any> {
    return this.http.delete(`${ManufacturerService.BASE_REQUEST}/${manufacturerId}`);
  }

  getManufacturerBy(manufacturerId: number): Observable<ManufacturerView> {
    if (!manufacturerId) {
      return null;
    }
    return this.http.get<ManufacturerView>(`${ManufacturerService.BASE_REQUEST}/${manufacturerId}`);
  }

  fetchAll(page = 0, size?: number): Observable<ManufacturerView[]> {
    return this.http.get<ManufacturerView[]>(`${ManufacturerService.BASE_REQUEST}`, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size ? size.toString() : '')
    });
  }

}
