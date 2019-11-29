import { Router } from '@angular/router';
import { CartDataService } from './../../cart-data.service';
import { UserInfo } from './../../../models/user-info.model';
import { DiscountType } from './../../../models/types/discount.type';
import { CheckoutItemView } from './../checkout.component';
import { TransactionDto } from './../../../models/dtos/transaction.dto';
import { TransactionService } from './../../../services/transaction.service';
import { UserInfoService } from './../../../services/user-info.service';
import { UserDto } from './../../../models/dtos/user.dto';
import { User } from './../../../models/view-model/user.view.model';
import { filter, takeUntil, map, switchMap } from 'rxjs/operators';
import { UserAuthenticatedService } from './../../../services/user-authenticated.service';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subject, Observable, of } from 'rxjs';
import { TransactionDetailedDto } from 'src/app/models/dtos/transaction-detailed.dto';

import * as Regex from '../../../models/RegexPattern';

const PHONE_REGEX_PATTERN = Regex.pattern.phone;

const EMAIL_PATTERN = Regex.pattern.email;

const CHECK_COUNT_FORM_BUILD_CONFIG = {
  firstname: ['', [Validators.required]],
  lastname: ['', [Validators.required]],
  phoneNumber: ['', [Validators.required, Validators.pattern(PHONE_REGEX_PATTERN)]],
  email: ['', [Validators.required, Validators.pattern(EMAIL_PATTERN)]],
  address: ['', [Validators.required]]
};

@Component({
  selector: 'app-checkout-infomation',
  templateUrl: './checkout-infomation.component.html',
  styleUrls: ['./checkout-infomation.component.css']
})
export class CheckoutInfomationComponent implements OnInit, OnDestroy {
  @Input() checkoutItems: CheckoutItemView[] = [];

  checkoutForm: FormGroup;

  unscription$ = new Subject();

  logined = false;

  currentUser: User;

  constructor(
    private formBuilder: FormBuilder,
    private userAuthService: UserAuthenticatedService,
    private userInfoService: UserInfoService,
    private transactionService: TransactionService,
    private cartData: CartDataService,
    private router: Router
  ) {}

  ngOnInit() {
    this.checkoutForm = this.formBuilder.group(CHECK_COUNT_FORM_BUILD_CONFIG);

    this.userAuthService.user$
      .pipe(takeUntil(this.unscription$))
      .subscribe(user => {
        if (user) {
          this.logined = true;
          this.currentUser = user;
          this.setValue(user);
        } else {
          this.logined = false;
          this.resetValue();
        }
      });
  }

  get firstnameControl() {
    return this.checkoutForm.get('firstname');
  }

  get lastnameControl() {
    return this.checkoutForm.get('lastname');
  }

  get phoneNumberControl() {
    return this.checkoutForm.get('phoneNumber');
  }

  get emailControl() {
    return this.checkoutForm.get('email');
  }

  get addressControl() {
    return this.checkoutForm.get('address');
  }

  setValue(user: User) {
    const { firstname, lastname, email, phoneNumber, address } = user;
    this.firstnameControl.setValue(firstname);
    this.lastnameControl.setValue(lastname);
    this.emailControl.setValue(email);
    this.phoneNumberControl.setValue(phoneNumber);
    this.addressControl.setValue(address);
  }

  resetValue() {
    this.firstnameControl.setValue(null);
    this.lastnameControl.setValue(null);
    this.emailControl.setValue(null);
    this.phoneNumberControl.setValue(null);
    this.addressControl.setValue(null);
  }

  getUserInfor(): UserInfo {
    const baseInfo = {
      firstname: this.firstnameControl.value,
      lastname: this.lastnameControl.value,
      email: this.emailControl.value,
      address: this.addressControl.value,
      phoneNumber: this.phoneNumberControl.value
    };
    if (!this.logined) {
      return baseInfo;
    }
    const user = { ...this.currentUser, ...baseInfo };
    return user;
  }

  saveOrUpdateUserInfo(): Observable<UserInfo> {
    const user = this.getUserInfor();
    if (this.logined) {
      return this.userInfoService.update(user);
    }
    return this.userInfoService.create(user);
  }

  mapCheckoutItemToTransactionDetailed(
    checkoutItem: CheckoutItemView
  ): TransactionDetailedDto {
    const { productId, quantity, product } = checkoutItem;
    const { price, discount } = product;
    let { discountType, discountValue } = {
      discountType: null,
      discountValue: null
    };
    if (discount) {
      discountType = discount.discountType;
      discountValue = discount.discountValue;
    }
    return {
      productId,
      quantity,
      price,
      discountType,
      discountValue
    } as TransactionDetailedDto;
  }

  getTransationDto(user: UserInfo): TransactionDto {
    const transactionDetaileds = this.checkoutItems
      .filter(item => item.product != null)
      .map(this.mapCheckoutItemToTransactionDetailed);
    return {
      customerInfoId: user.id,
      transactionDetaileds
    };
  }

  onSubmit() {
    this.saveOrUpdateUserInfo()
      .pipe(
        map(user => this.getTransationDto(user)),
        switchMap(transaction => {
          console.log(transaction);
          return this.transactionService.create(transaction);
        })
      )
    .subscribe(
      (result) => this.onSuccess(result),
      (error) => this.onError(error)
    );
  }

  onSuccess(result?: TransactionDto) {
    this.cartData.clear();
    this.router.navigate(['index', 'order-success', result.id]);
  }

  onError(err?: any) {
    console.log(err);
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
