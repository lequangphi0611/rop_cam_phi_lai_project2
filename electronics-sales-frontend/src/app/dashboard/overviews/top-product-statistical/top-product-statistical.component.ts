import { Component, OnInit, Input } from '@angular/core';
import { ProductStatisticalView } from 'src/app/models/view-model/product-statistical';

@Component({
  selector: 'app-top-product-statistical',
  templateUrl: './top-product-statistical.component.html',
  styleUrls: ['../../dashboard.component.css', './top-product-statistical.component.css']
})
export class TopProductStatisticalComponent implements OnInit {

  @Input() title: string;

  @Input() productStatisticals: ProductStatisticalView[];

  constructor() { }

  ngOnInit() {
    console.log(this.productStatisticals);
  }

}
