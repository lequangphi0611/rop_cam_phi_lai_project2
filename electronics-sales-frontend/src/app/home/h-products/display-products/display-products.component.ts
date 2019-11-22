import { ProductView } from './../../../models/view-model/product.view.model';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-display-products',
  templateUrl: './display-products.component.html',
  styleUrls: ['./display-products.component.css']
})
export class DisplayProductsComponent implements OnInit {

  @Input() products: ProductView[];

  constructor() { }

  ngOnInit() {
  }

}
