import { map } from 'rxjs/operators';
import { StatisticalService } from './../../../services/statistical.service';
import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../../../assets/canvas/canvasjs.min';

const MONTH_PREFIX_VI = 'Tháng';

@Component({
  selector: 'app-overviews-chart',
  templateUrl: './overviews-chart.component.html',
  styleUrls: ['./overviews-chart.component.css']
})
export class OverviewsChartComponent implements OnInit {

  constructor(private statisticalService: StatisticalService) { }

  ngOnInit() {
    this.statisticalService.getRevenueOverMonthStatistical()
      .pipe(
        map(response => response.map(value => {
          const {revenue, month} = value;
          return {
            y: revenue,
            label: `${MONTH_PREFIX_VI} ${month}`
          }
        }))
      )
      .subscribe(datapoint => {
        this.initChart(datapoint);
      });
  }

  initChart(dataPoints: {y: number, label: string}[]) {
    console.log(dataPoints);
    const chart = new CanvasJS.Chart('chartContainer', {
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: `Doanh thu các tháng ${this.currentYear}`
      },
      data: [{
        type: 'column',
        dataPoints
      }]
    });

    chart.render();
  }

  get currentYear() {
    return new Date().getFullYear();
  }

}
