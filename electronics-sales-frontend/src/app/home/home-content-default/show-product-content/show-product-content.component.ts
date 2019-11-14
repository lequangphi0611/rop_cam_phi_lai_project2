import { finalize } from 'rxjs/operators';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Page } from './../../../models/page.model';
import { Observable, of } from 'rxjs';
import { ProductService } from './../../../services/product.service';
import { Component, OnInit, Input } from '@angular/core';
import { ProductBanner } from '../home-content-default.component';

@Component({
  selector: 'app-show-product-content',
  templateUrl: './show-product-content.component.html',
  styleUrls: [
    '../../home.component.css',
    '../../../../assets/css/magiczoom.css',
  ],
})
export class ShowProductContentComponent implements OnInit {
  @Input() productBanner: ProductBanner;

  products$: Observable<ProductView[]>;


  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.products$ = this.productService
      .fetchProduct(this.productBanner.option);


  }
}
