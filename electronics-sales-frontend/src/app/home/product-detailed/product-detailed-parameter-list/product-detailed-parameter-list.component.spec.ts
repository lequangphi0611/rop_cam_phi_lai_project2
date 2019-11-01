import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductDetailedParameterListComponent } from './product-detailed-parameter-list.component';

describe('ProductDetailedParameterListComponent', () => {
  let component: ProductDetailedParameterListComponent;
  let fixture: ComponentFixture<ProductDetailedParameterListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductDetailedParameterListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductDetailedParameterListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
