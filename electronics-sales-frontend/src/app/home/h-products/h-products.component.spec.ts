import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HProductsComponent } from './h-products.component';

describe('HProductsComponent', () => {
  let component: HProductsComponent;
  let fixture: ComponentFixture<HProductsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HProductsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
