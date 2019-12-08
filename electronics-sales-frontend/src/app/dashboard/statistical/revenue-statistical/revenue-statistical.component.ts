import { StatisticalService } from './../../../services/statistical.service';
import { RevenueStatisticalDataSource } from './revenue-statistical-datasource';
import { Component, OnInit } from '@angular/core';

const DISPLAYED_COLUMN = [
  'createdTime',
  'mỉnRevenue',
  'maxRevenue',
  'totalRevenue'
]

@Component({
  selector: 'app-revenue-statistical',
  templateUrl: './revenue-statistical.component.html',
  styleUrls: ['./revenue-statistical.component.css']
})
export class RevenueStatisticalComponent implements OnInit {

  dataSource: RevenueStatisticalDataSource;

  displayed = DISPLAYED_COLUMN;

  constructor(private statisticalService: StatisticalService) { }

  ngOnInit() {
    this.dataSource = new RevenueStatisticalDataSource(this.statisticalService);
    this.dataSource.load();
    this.dataSource.connect().subscribe(console.log);
  }

}
