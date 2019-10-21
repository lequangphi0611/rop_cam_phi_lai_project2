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

const NAV_ITEMS_RIGHT: NavItemData[] = [
  {
    name: 'Giỏ hàng',
    link: 'cart',
  },
  {
    name: 'Đăng ký',
    link: 'register',
  },
  {
    name: 'Đăng nhập',
    link: 'login',
  },
];

@Component({
  selector: 'app-home-header',
  templateUrl: './home-header.component.html',
  styleUrls: ['../home.component.css'],
})
export class HomeHeaderComponent implements OnInit {
  navItems$: Observable<NavItemData[]>;

  navItemRight$: Observable<NavItemData[]>;

  categories$: Observable<CategoryView[]>;

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.navItems$ = of(SUB_NAV_ITEMS);
    this.navItemRight$ = of(NAV_ITEMS_RIGHT);
    this.categories$ = this.categoryService.fetchCategories().pipe(map(categories => {
      console.log({categories});
      return categories;
    }));
  }

  trackByCategory(index: number, item: CategoryView) {
    return item.id;
  }
}
