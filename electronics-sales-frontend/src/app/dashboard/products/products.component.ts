import { map, filter } from 'rxjs/operators';
import { ActivatedRoute, Params } from '@angular/router';
import { ChooseImagesComponent } from './../../choose-images/choose-images.component';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { of, BehaviorSubject } from 'rxjs';
import { ProductDataView } from './products-data/products-data.component';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['../dashboard.component.css', './products.component.css'],
})
export class ProductsComponent implements OnInit, OnDestroy {
  selectedIndex = 0;

  currentProduct: ProductDataView;

  productEditable = new BehaviorSubject<ProductDataView>(null);

  productEditable$ = this.productEditable.asObservable();

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
  }

  onEdit(product: ProductDataView) {
    this.productEditable.next(product);
    this.selectedIndex = 1;
  }

  changeSelectedIndex(index: number) {
    this.selectedIndex = index;
  }

  resetCurrentProductValue() {
    this.productEditable.next(null);
  }

  ngOnDestroy() {
    this.productEditable.complete();
  }
}
