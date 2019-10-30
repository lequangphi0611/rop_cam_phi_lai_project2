import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Page } from './../../../models/page.model';
import { Observable } from 'rxjs';
import { ProductService } from './../../../services/product.service';
import { Component, OnInit, Input } from '@angular/core';
import { ProductBanner } from '../home-content-default.component';

@Component({
  selector: 'app-show-product-content',
  templateUrl: './show-product-content.component.html',
  styleUrls: ['../../home.component.css', '../../../../assets/css/magiczoom.css']
})
export class ShowProductContentComponent implements OnInit {

  @Input() productBanner: ProductBanner;

  productPage$: Observable<Page<ProductView>>;

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productPage$ = this.productService.fetchProduct(this.productBanner.option);
  }

}
