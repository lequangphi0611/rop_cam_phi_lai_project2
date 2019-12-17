import { tap, map, filter } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from './../../../services/user.service';
import { UserDto } from './../../../models/dtos/user.dto';
import { Subject } from 'rxjs';
import { pattern } from './../../../models/RegexPattern';
import { UserAuthenticatedService } from './../../../services/user-authenticated.service';
import { ChooseAvartarComponent } from './choose-avartar/choose-avartar.component';
import { Component, OnInit, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { User } from 'src/app/models/view-model/user.view.model';

const MY_ACCOUNT_FORM_BUILD_PROPERTY = {
  lastname: [null, [Validators.required]],
  firstname: [null, [Validators.required]],
  gender: [true],
  username: [{value : null, disabled: true}, [Validators.required]],
  birthday: [null, [Validators.required]],
  email: [null, [Validators.required, Validators.pattern(pattern.email)]],
  phoneNumber: [null, [Validators.required, Validators.pattern(pattern.phone)]],
  address: [null, [Validators.required]]
};

const IMG_URL = '/api/images';

@Component({
  selector: 'app-update-my-account',
  templateUrl: './update-my-account.component.html',
  styleUrls: ['./update-my-account.component.css']
})
export class UpdateMyAccountComponent implements OnInit, OnDestroy  {

  @ViewChild(ChooseAvartarComponent, { static: true })
  chooseAvartarComponent: ChooseAvartarComponent;

  myAccountForm = this.formBuilder.group(MY_ACCOUNT_FORM_BUILD_PROPERTY);

  avartar: File;

  currentUser: User;

  constructor(
    private userData: UserAuthenticatedService,
    private formBuilder: FormBuilder,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.currentUserSubcriptions();
  }

  get firstnameControl() {
    return this.myAccountForm.get('firstname');
  }

  get lastnameControl() {
    return this.myAccountForm.get('lastname');
  }

  get usernameControl() {
    return this.myAccountForm.get('username');
  }

  get birthdayControl() {
    return this.myAccountForm.get('birthday');
  }

  get emailControl() {
    return this.myAccountForm.get('email');
  }

  get phoneNumberControl() {
    return this.myAccountForm.get('phoneNumber');
  }

  get addressControl() {
    return this.myAccountForm.get('address');
  }

  currentUserSubcriptions(): void {
    this.userData.user$
      .pipe(
        tap(user => {
          this.currentUser = user;
          this.patchFormValue(user);
        }),
        map(user => user.avartarId),
        filter(id => id != null),
        map(avartarId => `${IMG_URL}/${avartarId}`)
      )
      .subscribe(url => this.chooseAvartarComponent.fileUrl = url);
  }

  patchFormValue(user: User) {
    const {
      lastname,
      firstname,
      gender,
      username,
      birthday,
      email,
      phoneNumber,
      address
    } = user;
    this.myAccountForm.patchValue({
      lastname,
      firstname,
      gender,
      username,
      birthday,
      email,
      phoneNumber,
      address
    });
  }

  onAvartarChange(file: File) {
    this.avartar = file;
  }

  get user(): UserDto {
    return {
      ...this.myAccountForm.value,
      id: this.userData.value.id
    };
  }

  onSubmit() {
    this.userService.update(this.user, this.avartar)
      .subscribe(
        (result) => this.onSuccess(result),
        (err) => this.onError(err)
      );


  }

  onSuccess(result: User): void {
    this.snackBar.open('Cập nhật tài khoản thành công !', 'Đóng', { duration: 2000});
    this.userData.load();
  }

  reset() {
    this.patchFormValue(this.currentUser);
    this.chooseAvartarComponent.fileUrl = `${IMG_URL}/${this.currentUser.avartarId}`;
  }

  onError(err: any): void {
    this.snackBar.open('Có lỗi xảy ra !', 'Đóng', { duration: 2000});
  }

  ngOnDestroy(): void {
  }
}
