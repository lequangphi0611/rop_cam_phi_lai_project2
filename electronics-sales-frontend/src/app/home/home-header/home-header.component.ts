import { User } from './../../models/view-model/user.view.model';
import { UserAuthenticatedService } from './../../services/user-authenticated.service';
import { map } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { NavItemData } from './sub-navigation/sub-navigation.component';
import { Component, OnInit } from '@angular/core';
import { CategoryView } from 'src/app/models/view-model/category.view.model';
import { CategoryService } from 'src/app/services/category.service';

const SUB_NAV_ITEMS: NavItemData[] = [
  {
    name: 'Trang chủ',
    link: '',
  },
  {
    name: 'Giới thiệu',
    link: 'about',
  },
  {
    name: 'Tin tức',
    link: 'news',
  },
  {
    name: 'Hỏi đáp',
    link: 'questions-answers',
  },
];

const NAV_ITEMS_RIGHT: {name: string, link: string, isLogin: boolean}[] = [
  {
    name: 'Giỏ hàng',
    link: 'cart',
    isLogin: false
  },
  {
    name: 'Đăng ký',
    link: 'register',
    isLogin: true
  },
  {
    name: 'Đăng nhập',
    link: 'login',
    isLogin: true
  },
];

@Component({
  selector: 'app-home-header',
  templateUrl: './home-header.component.html',
  styleUrls: ['../home.component.css'],
})
export class HomeHeaderComponent implements OnInit {

  navItems$: Observable<NavItemData[]>;

  navItemRight$: Observable<{name: string, link: string, isLogin: boolean}[]>;

  categories$: Observable<CategoryView[]>;

  currUser$: Observable<User>;

  constructor(
    private categoryService: CategoryService,
    public userAuthenticatedService: UserAuthenticatedService
  ) {}

  ngOnInit() {
    this.navItems$ = of(SUB_NAV_ITEMS);
    this.navItemRight$ = of(NAV_ITEMS_RIGHT);
    this.categories$ = this.categoryService
      .fetchCategories()
      .pipe(
        map(categories => {
          return categories;
        })
      );
    this.currUser$ = this.userAuthenticatedService.user$;
  }

  trackByCategory(index: number, item: CategoryView) {
    return item.id;
  }

  async logOut(): Promise<void> {
    this.userAuthenticatedService.clear();
  }


}
