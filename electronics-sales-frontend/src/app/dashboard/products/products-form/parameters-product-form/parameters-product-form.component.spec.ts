import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParametersProductFormComponent } from './parameters-product-form.component';

describe('ParametersProductFormComponent', () => {
  let component: ParametersProductFormComponent;
  let fixture: ComponentFixture<ParametersProductFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParametersProductFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParametersProductFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
