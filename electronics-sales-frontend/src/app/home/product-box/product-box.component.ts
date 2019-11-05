import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ProductView } from 'src/app/models/view-model/product.view.model';
import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-box',
  templateUrl: './product-box.component.html',
  styleUrls: ['./product-box.component.css']
})
export class ProductBoxComponent implements OnInit, OnDestroy {

  @Input() product: ProductView;

  image: string;

  subcription: Subscription;

  constructor(private router: Router, private productService: ProductService) { }

  ngOnInit() {
    this.subcription = this.productService.getImages(this.product.id)
      .subscribe(images => this.image = images[0]);
  }

  goToDetail(id: number): void {
    this.router.navigate([`/index/product/${id}`]);
  }

  ngOnDestroy(): void {
    this.subcription.unsubscribe();
  }
}
