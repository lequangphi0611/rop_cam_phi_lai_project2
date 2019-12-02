import { EmployeeReceiver } from './../../../models/employee.receiver';
import { UserDto } from './../../../models/dtos/user.dto';
import {
  takeUntil,
  debounceTime,
  distinct,
  filter,
  map,
  switchMap
} from 'rxjs/operators';
import { ChooseImagesComponent } from './../../../choose-images/choose-images.component';
import {
  genders,
  toBoolean,
  parseGenderFromBoolean,
  Gender
} from './../../../models/types/gender.type';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogConfig,
  MatDialog
} from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/user.service';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import {
  Component,
  OnInit,
  Inject,
  ViewChild,
  AfterViewInit,
  OnDestroy,
  Output,
  EventEmitter,
  ElementRef
} from '@angular/core';

import { pattern } from '../../../models/RegexPattern';
import { Subject, Observable, of } from 'rxjs';

const EMPLOYEE_FORM_BUILDER = {
  lastname: ['', [Validators.required]],
  firstname: ['', [Validators.required]],
  gender: [Gender.MALE],
  username: ['', [Validators.required]],
  password: ['', [Validators.required]],
  birthday: [null, [Validators.required]],
  email: ['', [Validators.required, Validators.pattern(pattern.email)]],
  phoneNumber: ['', [Validators.required, Validators.pattern(pattern.phone)]],
  address: ['', [Validators.required]]
};

const DEBOUND_TIME = 1000;

export interface EmployeeFormDialogData {
  employees?: EmployeeReceiver;
}

const DEFAULT_DIALOG_CONFIG = {
  autoFocus: true,
  disableClose: true
};

const BASE64_IMAGE = 'data:image/png;base64,';

@Component({
  selector: 'app-employee-form-dialog',
  templateUrl: './employee-form-dialog.component.html',
  styleUrls: ['./employee-form-dialog.component.css']
})
export class EmployeeFormDialogComponent
  implements OnInit, AfterViewInit, OnDestroy {
  @Output() saveSusccess = new EventEmitter<any>(true);

  @ViewChild(ChooseImagesComponent, { static: true })
  chooseImagesComponent: ChooseImagesComponent;

  @ViewChild('passwordInput', { static: true })
  passwordInput: ElementRef;

  genders = [...genders];

  employeeForm: FormGroup;

  unscription$ = new Subject();

  avartar: File;

  editMode = false;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<EmployeeFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data
  ) {
  }

  static open(
    dialog: MatDialog,
    config: MatDialogConfig = {}
  ): MatDialogRef<EmployeeFormDialogComponent> {
    return dialog.open(EmployeeFormDialogComponent, {
      ...DEFAULT_DIALOG_CONFIG,
      ...config
    });
  }

  ngOnInit() {
    this.employeeForm = this.formBuilder.group(EMPLOYEE_FORM_BUILDER);
    if (this.employee) {
      this.editMode = true;
      this.setFormValue(this.employee);
    }
  }

  ngAfterViewInit() {
    this.chooseImagesComponent.filesSelected$
      .pipe(takeUntil(this.unscription$))
      .subscribe(file => (this.avartar = file));

    this.usernameControl.valueChanges
      .pipe(
        takeUntil(this.unscription$),
        debounceTime(DEBOUND_TIME),
        filter(value => value != null && (value as string).trim().length > 0)
      )
      .subscribe(value => this.onUsernameControlChange(value));
  }

  isMale(gender: Gender) {
    return toBoolean(gender);
  }

  get lasnameControl() {
    return this.employeeForm.get('lastname');
  }

  get firstnameControl() {
    return this.employeeForm.get('firstname');
  }

  get genderControl() {
    return this.employeeForm.get('gender');
  }

  get usernameControl() {
    return this.employeeForm.get('username');
  }

  get passwordControl() {
    return this.employeeForm.get('password');
  }

  get birthdayControl() {
    return this.employeeForm.get('birthday');
  }

  get emailControl() {
    return this.employeeForm.get('email');
  }

  get phoneNumberControl() {
    return this.employeeForm.get('phoneNumber');
  }

  get addressControl() {
    return this.employeeForm.get('address');
  }

  get employee(): EmployeeReceiver {
    return !this.data ? null : (this.data.employee as EmployeeReceiver);
  }

  checkUsernameExists(username: string): Observable<boolean> {
    return of(this.editMode ? this.employee.username !== username : true)
      .pipe(
        filter(exists => exists),
        switchMap(exists => this.userService.existsByUsername(username))
      );
  }

  onUsernameControlChange(username: string) {
    this.checkUsernameExists(username)
      .pipe(filter(exists => exists))
      .subscribe(exists => {
        this.usernameControl.setErrors({
          existsUsername: exists
        });
      });
  }

  cancle() {
    this.dialogRef.close();
  }

  get user(): UserDto {
    const { gender } = this.employeeForm.value;
    return {
      ...this.employeeForm.value,
      gender: gender === 1,
      id: !this.editMode ? null : this.employee.id
    };
  }

  resetForm() {
    this.employeeForm.reset({
      gender: Gender.MALE
    });
    this.chooseImagesComponent.onRemoveFile();
  }

  setFormValue(employee: EmployeeReceiver) {
    const gender = employee.gender ? Gender.MALE : Gender.FEMALE;
    const {
      firstname,
      lastname,
      birthday,
      phoneNumber,
      email,
      address,
      username,
      avartar
    } = employee;
    this.employeeForm.setValue({
      firstname,
      lastname,
      birthday,
      phoneNumber,
      address,
      username,
      email,
      gender,
      password: ''
    });
    this.chooseImagesComponent.fileUrl = avartar
      ? `${BASE64_IMAGE}${avartar}`
      : null;
    this.passwordInput.nativeElement.focus();
  }

  onSubmit() {
    of(this.editMode)
      .pipe(
        switchMap(edit =>
          edit
            ? this.userService.update(this.user, this.avartar)
            : this.userService.create(this.user, this.avartar)
        )
      )
      .subscribe(
        result => this.onSuccess(result),
        err => this.onError(err)
      );
  }

  onSuccess(result?: any): void {
    this.resetForm();
    this.snackBar.open('Lưu thành công', 'Đóng', { duration: 2000 });
    this.cancle();
    this.saveSusccess.emit(true);
  }

  onError(err: any): void {
    this.snackBar.open(`Có lỗi xảy ra`, 'Đóng', { duration: 2000 });
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
