import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowProductContentComponent } from './show-product-content.component';

describe('ShowProductContentComponent', () => {
  let component: ShowProductContentComponent;
  let fixture: ComponentFixture<ShowProductContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowProductContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowProductContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
