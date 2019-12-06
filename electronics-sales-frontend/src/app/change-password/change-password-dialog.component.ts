import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/user.service';
import { map, filter } from 'rxjs/operators';
import { UserAuthenticatedService } from './../services/user-authenticated.service';
import { FormBuilder, Validators } from '@angular/forms';
import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit
} from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { zip, fromEvent, combineLatest } from 'rxjs';
import { Router } from '@angular/router';

const CHANGE_PASSWORD_FORM_BUILDER = {
  oldPassword: [null, [Validators.required]],
  newPassword: [null, [Validators.required, Validators.minLength(6)]],
  confirmNewPassword: [null, [Validators.required]]
};

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.css']
})
export class ChangePasswordDialogComponent implements OnInit, AfterViewInit {
  @ViewChild('newPasswordInput', { static: true }) newPasswordInput: ElementRef;

  @ViewChild('confirmNewPasswordInput', { static: true })
  confirmNewPasswordInput: ElementRef;

  changePasswordForm = this.formBuilder.group(CHANGE_PASSWORD_FORM_BUILDER);

  constructor(
    private dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
    private formBuilder: FormBuilder,
    private userService: UserService,
    private snackbar: MatSnackBar,
    private userAuthenticatedService: UserAuthenticatedService,
    private router: Router
  ) {}

  ngOnInit() {}

  get confirmNewPasswordControl() {
    return this.changePasswordForm.get('confirmNewPassword');
  }

  get oldPasswordControl() {
    return this.changePasswordForm.get('oldPassword');
  }

  get newPasswordControl() {
    return this.changePasswordForm.get('newPassword');
  }

  ngAfterViewInit() {
    const newPasswordInputEvent = fromEvent<any>(
      this.newPasswordInput.nativeElement,
      'input'
    ).pipe(map(event => event.target.value));
    const confirmNewPasswordInputEvent = fromEvent<any>(
      this.confirmNewPasswordInput.nativeElement,
      'input'
    ).pipe(map(event => event.target.value));

    combineLatest(newPasswordInputEvent, confirmNewPasswordInputEvent)
      .pipe(
        map(values => values[0] === values[1]),
        filter(match => !match)
      )
      .subscribe(() => {
        this.confirmNewPasswordControl.setErrors({
          invalidConfirm: true
        });
      });
  }

  changePass() {
    const { newPassword, oldPassword } = this.changePasswordForm.value;
    this.userService.updatePassword(oldPassword, newPassword).subscribe(
      () => {
        this.snackbar.open('Đổi mật khẩu thành công', 'Đóng', {
          duration: 2000
        });
        this.dialogRef.close();
        this.userAuthenticatedService.clear();
        this.router.navigate(['/index/login']);
      },
      err => {
        this.oldPasswordControl.setErrors({ invalidOldPassword: true });
      }
    );
  }
}
