import { map, takeUntil } from 'rxjs/operators';
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

import { hasUpperCase } from '../../validators/password.validator';

const PASSWORD_MIN_LENGTH = 6;

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

const passwordValidator = {
  hasUpperCaseValidator: (control: AbstractControl) => {
    const value = control.value;

    if (!value || hasUpperCase(value)) {
      return null;
    }
    return { nonStartWithUpperCase: true };
  },

  passwordLengthValidator: (control: AbstractControl) => {
    const value = control.value;

    if (!value || value.length > PASSWORD_MIN_LENGTH) {
      return null;
    }
    return { notEnoughPasswordLength: true };
  },
};

const passwordValidators = [
  passwordValidator.passwordLengthValidator,
  passwordValidator.hasUpperCaseValidator,
];

const registerLogin = {
  firstname: [null, [Validators.required]],
  lastname: [null, [Validators.required]],
  gender: 'true',
  username: [null, [Validators.required]],
  password: [null, [Validators.required, ...passwordValidators]],
  confirmPassword: [null, [Validators.required]],
  birthday: '1990-01-01',
  email: [null, [Validators.required]],
  phoneNumber: [null, [Validators.required]],
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
    checked: true
  },
  {
    name: 'Nữ',
    value: false
  }
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

  gender$: Observable<{name: string, value: boolean, checked?: boolean}[]>;

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
    this.usernameElement = this.element.nativeElement.querySelectorAll('#username')[0];
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

  async onSubmit() {
    const existUsername = await this.validateUsername();
    if (existUsername) {
      this.onExistsUsernameError();
      return;
    }

    const user = this.getUserFromRegisterForm();
    this.userService
      .register(user)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.userAuthenticatedService.load();
        this.route.navigate(['/index']);
      });
  }

  validateUsername(): Promise<boolean> {
    return this.userService
      .existsByUsername(this.registerForm.value.username)
      .toPromise();
  }

  onExistsUsernameError(): void {
    this.registerForm.controls.username.setErrors({
      existUsername: true,
    });
    this.usernameElement.focus();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }
}
