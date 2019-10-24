import { AccountDto } from './../../models/dtos/account.dto';
import { Router } from '@angular/router';
import { Role } from './../../models/types/role.type';
import { AuthenticatedResponse } from './../../models/dtos/authenticated.response';
import { UserAuthenticatedService } from './../../services/user-authenticated.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormBuilder, FormGroup, NgForm } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

const INDEX_PATH = '/index';

const DASHBOARD_PATH = '/dashboard';

const PASSWORD_INPUT_TYPE = 'password';

const TEXT_INPUT_TYPE = 'text';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  passwordInputType = PASSWORD_INPUT_TYPE;

  loginFailed: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private userAuthenticatedService: UserAuthenticatedService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: '',
      password: ''
    });
  }

  onSubmit() {
    this.loginFailed = false;
    const loginRequest = this.loginForm.value as AccountDto;
    this.userService
      .login(loginRequest)
      .subscribe(
        authResponse => this.onSuccess(authResponse),
        err => this.onError(err)
      );
  }

  onSuccess(authResponse: AuthenticatedResponse): void {
    this.userAuthenticatedService.load();
    const role = Role[authResponse.role.toUpperCase()];
    if (role === Role.CUSTOMER) {
      this.router.navigate([INDEX_PATH]);
      return;
    }
    this.router.navigate([DASHBOARD_PATH]);
  }

  onError(err: HttpErrorResponse): void {
    this.loginFailed = true;
    this.setPasswordInputEmpty();
  }

  setPasswordInputEmpty(): void {
    this.loginForm.controls.password.setValue('');
  }

  onChangeShowPasswordCheckBox(showPassword: boolean): void {
   this.passwordInputType = showPassword ? TEXT_INPUT_TYPE : PASSWORD_INPUT_TYPE;
  }
}
