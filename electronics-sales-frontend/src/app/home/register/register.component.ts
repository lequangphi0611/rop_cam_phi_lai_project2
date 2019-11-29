import { map, takeUntil, filter } from 'rxjs/operators';
import { Component, OnInit, ElementRef, OnDestroy } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormGroupDirective,
  NgForm,
  ValidationErrors,
  ValidatorFn,
  Validators,
  AbstractControl,
} from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { UserDto } from './../../models/dtos/user.dto';
import { UserAuthenticatedService } from './../../services/user-authenticated.service';
import { Subject, Observable, of } from 'rxjs';

import * as Regex from './../../models/RegexPattern';

const PASSWORD_MIN_LENGTH = 6;

const PHONE_NUMBER_PATTERN = Regex.pattern.phone;

const EMAIL_PATTERN = Regex.pattern.email;

const confirmPaswordValidator: ValidatorFn = (
  control: FormGroup
): ValidationErrors | null => {
  const password = control.get('password').value;
  const confirmPassword = control.get('confirmPassword').value;
  return password === confirmPassword
    ? null
    : {
        passwordNotMatch: true,
      };
};


const registerLogin = {
  firstname: [null, [Validators.required]],
  lastname: [null, [Validators.required]],
  gender: 'true',
  username: [null, [Validators.required]],
  password: [null, [Validators.required, Validators.minLength(6)]],
  confirmPassword: [null, [Validators.required]],
  birthday: '1990-01-01',
  email: [null, [Validators.required, Validators.pattern(EMAIL_PATTERN)]],
  phoneNumber: [
    null,
    [
      Validators.required,
      Validators.pattern(PHONE_NUMBER_PATTERN),
      Validators.maxLength(11),
    ],
  ],
  address: [null, [Validators.required]],
};

class CrossFieldErrorMatcher implements ErrorStateMatcher {
  isErrorState(
    control: FormControl | null,
    form: FormGroupDirective | NgForm | null
  ): boolean {
    return (
      ((control.dirty || control.touched) && control.hasError('required')) ||
      form.hasError('passwordNotMatch')
    );
  }
}

const GENDER = [
  {
    name: 'Nam',
    value: true,
    checked: true,
  },
  {
    name: 'Nữ',
    value: false,
  },
];

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  errorMatcher = new CrossFieldErrorMatcher();

  registerForm: FormGroup;

  destroy$ = new Subject<void>();

  gender$: Observable<{ name: string; value: boolean; checked?: boolean }[]>;

  usernameElement;

  constructor(
    private formBuilder: FormBuilder,
    private route: Router,
    private userService: UserService,
    private userAuthenticatedService: UserAuthenticatedService,
    private element: ElementRef
  ) {}

  ngOnInit() {
    this.registerForm = this.formBuilder.group(registerLogin, {
      validators: [confirmPaswordValidator],
    });
    this.gender$ = of(GENDER);
    this.usernameElement = this.element.nativeElement.querySelectorAll(
      '#username'
    )[0];
  }

  get usernameControl() {
    return this.registerForm.get('username');
  }

  onUsernameInputChange() {
    const username = this.usernameControl.value;
    if (username) {
      this.userService.existsByUsername(username)
        .pipe(takeUntil(this.destroy$)
        , filter(v => v))
        .subscribe(() => this.usernameControl.setErrors({
          existUsername: true,
        }));
    }
  }

  getUserFromRegisterForm(): UserDto {
    const formValue = this.registerForm.value;
    return {
      firstname: formValue.firstname as string,
      lastname: formValue.lastname as string,
      birthday: formValue.birthday as Date,
      email: formValue.email as string,
      phoneNumber: formValue.phoneNumber as string,
      address: formValue.address as string,
      gender: formValue.gender as boolean,
      username: formValue.username as string,
      password: formValue.password as string,
    };
  }

  onSubmit() {
    const user = this.getUserFromRegisterForm();
    this.userService
      .register(user)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.userAuthenticatedService.load();
        this.route.navigate(['/index']);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }
}
