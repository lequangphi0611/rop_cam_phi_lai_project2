import { Observable } from 'rxjs';
import { ProductParameterView } from './../../../models/view-model/product-parameter.view';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-product-detailed-parameter-list',
  templateUrl: './product-detailed-parameter-list.component.html',
  styleUrls: ['./product-detailed-parameter-list.component.css']
})
export class ProductDetailedParameterListComponent implements OnInit {

  @Input() parameters$: Observable<ProductParameterView[]>;

  constructor() { }

  ngOnInit() {
  }

}
