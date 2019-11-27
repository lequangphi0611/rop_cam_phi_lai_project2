import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserInfo } from './../models/user-info.model';
import { User } from './../models/view-model/user.view.model';

const BASE_REQUEST = '/api/user-infos';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  constructor(private http: HttpClient) { }

  create(user: UserInfo): Observable<User> {
    return this.http.post<User>(BASE_REQUEST, user);
  }

  update(user: UserInfo): Observable<User> {
    const {userInfoId} = user;
    return this.http.put<User>(`${BASE_REQUEST}/${userInfoId}`, user);
  }
}
