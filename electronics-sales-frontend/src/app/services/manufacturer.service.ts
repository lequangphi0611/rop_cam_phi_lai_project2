import { ManufacturerView } from './../models/view-model/manufacturer.view.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ManufacturerService {

  static readonly BASE_REQUEST = '/api/manufacturers';

  constructor(private http: HttpClient) { }

  getManufacturerBy(manufacturerId: number): Observable<ManufacturerView> {
    if (!manufacturerId) {
      return null;
    }
    return this.http.get<ManufacturerView>(`${ManufacturerService.BASE_REQUEST}/${manufacturerId}`);
  }

}
