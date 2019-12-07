import { StatisticalService } from './../../../services/statistical.service';
import { Component, OnInit, Renderer2, ViewChild, QueryList, ElementRef, AfterViewInit, ViewContainerRef } from '@angular/core';
import { CategoryStatistical } from 'src/app/models/view-model/category-statistical.model';

@Component({
  selector: 'app-category-statistical',
  templateUrl: './category-statistical.component.html',
  styleUrls: [
    '../../dashboard.component.css',
    './category-statistical.component.css'
  ]
})
export class CategoryStatisticalComponent implements OnInit, AfterViewInit {

  categoryStatistical: CategoryStatistical;

  constructor(private statisticalService: StatisticalService, private renderer: Renderer2) {}

  ngOnInit() {
    this.fetchCategoryStatistical();

  }

  ngAfterViewInit(): void {

  }

  fetchCategoryStatistical() {
    this.statisticalService
      .getCategoryStatistical()
      .subscribe(response => (this.categoryStatistical = response));
  }
}
