import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-my-lazy-load-image',
  templateUrl: './my-lazy-load-image.component.html',
  styleUrls: ['./my-lazy-load-image.component.css']
})
export class MyLazyLoadImageComponent implements OnInit {

  @Input() default = '/assets/default.jpg';

  @Input() imageSrc: string;

  @Input() imgClass = `img-fluid`;

  constructor() { }

  ngOnInit() {
  }

}
