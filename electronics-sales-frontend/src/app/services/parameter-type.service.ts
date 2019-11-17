import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ParameterTypeDto } from './../models/dtos/paramter-type.dto';

@Injectable({
  providedIn: 'root',
})
export class ParameterTypeService {
  static readonly BASE_REQUEST = '/api/parameter-types';

  constructor(private http: HttpClient) {}

  create(parameterType: ParameterTypeDto): Observable<ParameterTypeDto> {
    return this.http.post<ParameterTypeDto>(
      `${ParameterTypeService.BASE_REQUEST}`,
      parameterType
    );
  }

  fetchAll(): Observable<ParameterTypeDto[]> {
    return this.http.get<ParameterTypeDto[]>(
      `${ParameterTypeService.BASE_REQUEST}`
    );
  }
}
