import { UserAuthenticatedService } from './services/user-authenticated.service';
import { map } from 'rxjs/operators';
import { UserService } from './services/user.service';
import { Component } from '@angular/core';
import { pipe } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  {

  constructor(private userAuthService: UserAuthenticatedService) {
    this.userAuthService.load();
  }

}
