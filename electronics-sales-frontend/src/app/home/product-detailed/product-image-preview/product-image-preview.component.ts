import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-product-image-preview',
  templateUrl: './product-image-preview.component.html',
  styleUrls: ['../product-detailed-component.css']
})
export class ProductImagePreviewComponent implements OnInit {

  @Input() images: string[];

  displayImage: string;

  constructor() { }

  ngOnInit() {
  }

  onSelected(image: string) {
    this.displayImage = image;
  }
}
