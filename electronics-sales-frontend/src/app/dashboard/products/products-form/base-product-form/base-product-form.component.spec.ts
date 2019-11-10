import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseProductFormComponent } from './base-product-form.component';

describe('BaseProductFormComponent', () => {
  let component: BaseProductFormComponent;
  let fixture: ComponentFixture<BaseProductFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BaseProductFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseProductFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
