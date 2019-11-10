import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductDescriptionFormComponent } from './product-description-form.component';

describe('ProductDescriptionFormComponent', () => {
  let component: ProductDescriptionFormComponent;
  let fixture: ComponentFixture<ProductDescriptionFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductDescriptionFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductDescriptionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
