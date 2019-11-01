import { Router } from '@angular/router';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-product-box',
  templateUrl: './product-box.component.html',
  styleUrls: ['./product-box.component.css']
})
export class ProductBoxComponent implements OnInit {

  @Input() product: ProductView;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  goToDetail(id: number): void {
    this.router.navigate([`/index/product/${id}`]);
  }
}
