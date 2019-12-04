import { SortType } from 'src/app/models/types/sort-type.type';
import { Page } from './../models/page.model';
import { EmployeeReceiver } from './../models/employee.receiver';
import { Role } from './../models/types/role.type';
import { UserDto } from './../models/dtos/user.dto';
import { User } from './../models/view-model/user.view.model';
import { AccountDto } from './../models/dtos/account.dto';
import { AuthenticatedResponse } from './../models/dtos/authenticated.response';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { map, catchError, tap } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';

const ACCOUNT_URL = 'api/accounts';

export const DEFAULT_EMPLOYEE_FETCH_OPTION: EmployeeFetchOption = {
  page: 0,

  size: 5,

  direction: SortType.DESC.toString(),

  search: null,

  sort: ['id']
};

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private static readonly LOGIN_REQUEST: string = '/api/login';

  private static readonly REGISTER_REQUEST: string = 'api/register';

  private static readonly EXPRIED_TIME = 604800000;

  public static readonly USER_COOKIE_KEY = 'user';

  constructor(private http: HttpClient, private cookieService: CookieService) {}

  mapUser(user: User): User {
    user.birthday = new Date(user.birthday);
    return user;
  }

  login(loginRequest: AccountDto): Observable<AuthenticatedResponse> {
    return this.http
      .post<AuthenticatedResponse>(UserService.LOGIN_REQUEST, loginRequest)
      .pipe(
        map(authentiatedResponse => {
          this.onLogin(authentiatedResponse);
          return authentiatedResponse;
        })
      );
  }

  onLogin(auth: AuthenticatedResponse) {
    this.cookieService.delete(UserService.USER_COOKIE_KEY);
    this.cookieService.set(
      UserService.USER_COOKIE_KEY,
      JSON.stringify(auth),
      UserService.EXPRIED_TIME,
      '/'
    );
  }

  register(user: UserDto): Observable<AuthenticatedResponse> {
    return this.http
      .post<AuthenticatedResponse>(UserService.REGISTER_REQUEST, user)
      .pipe(
        map(auth => {
          this.onLogin(auth);
          return auth;
        })
      );
  }

  getCurrentRole(): Observable<Role> {
    return this.http.get<{ role: string }>(`${ACCOUNT_URL}/roles`).pipe(
      map(result => result.role.toUpperCase()),
      map(roleStr => Role[roleStr])
    );
  }

  getCurrentUser(): Observable<User> {
    return this.http
      .get<User>(`${ACCOUNT_URL}/current`)
      .pipe(map(this.mapUser));
  }

  existsByUsername(username: string): Observable<boolean> {
    return this.http.head<any>(`${ACCOUNT_URL}/username/${username}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  getFormDataFrom(user: UserDto, avartar?: File): FormData {
    const formData = new FormData();
    formData.append('user', JSON.stringify(user));
    if (avartar) {
      formData.append('avartar', avartar);
    }
    return formData;
  }

  create(user: UserDto, avartar?: File) {
    const formData = this.getFormDataFrom(user, avartar);
    return this.http.post(ACCOUNT_URL, formData);
  }

  update(user: UserDto, avartar?: File) {
    const body = this.getFormDataFrom(user, avartar);
    const { id } = user;
    return this.http.put<User>(`${ACCOUNT_URL}/${id}`, body);
  }

  remove(userId: number) {
    return this.http.delete(`${ACCOUNT_URL}/${userId}`);
  }

  fetchEmployees(
    option: EmployeeFetchOption = {}
  ): Observable<Page<EmployeeReceiver>> {
    const { page, size, sort, direction, search } = {
      ...DEFAULT_EMPLOYEE_FETCH_OPTION,
      ...option
    };
    const params: any = {
      page,
      size,
      search: search ? search : '',
      sort: sort.map(s => `${s},${direction}`)
    };
    return this.http
      .get<Page<EmployeeReceiver>>(`${ACCOUNT_URL}/employees`, {
        params
      })
      .pipe(
        tap(employees => {
          employees.content.forEach(v => (v.birthday = new Date(v.birthday)));
        })
      );
  }
}

export interface EmployeeFetchOption {
  search?: string;

  page?: number;

  size?: number;

  sort?: string[];

  direction?: string;
}
