import { StatisticalService } from './../../services/statistical.service';
import { Component, OnInit } from '@angular/core';
import { ProductStatisticalView } from 'src/app/models/view-model/product-statistical';

const DEFAULT_LIMIT_ELEMENT = 5;

@Component({
  selector: 'app-overviews',
  templateUrl: './overviews.component.html',
  styleUrls: ['../dashboard.component.css', './overviews.component.css']
})
export class OverviewsComponent implements OnInit {

  revenueProductStatisticals: ProductStatisticalView[];

  constructor(private statisticalService: StatisticalService) { }

  ngOnInit() {
    this.fetchRevenueProductStatisticals(DEFAULT_LIMIT_ELEMENT);
  }

  fetchRevenueProductStatisticals(top?: number) {
    this.statisticalService.getRevenueProductStatistical(top)
      .subscribe(response => this.revenueProductStatisticals = response);
  }

}
