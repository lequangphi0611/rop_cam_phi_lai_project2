import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-show-product-content',
  templateUrl: './show-product-content.component.html',
  styleUrls: ['../../home.component.css']
})
export class ShowProductContentComponent implements OnInit {

  @Input() title: string;

  constructor() { }

  ngOnInit() {
  }

}
