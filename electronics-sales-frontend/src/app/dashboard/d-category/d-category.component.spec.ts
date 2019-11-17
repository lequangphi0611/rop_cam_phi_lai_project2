import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DCategoryComponent } from './d-category.component';

describe('DCategoryComponent', () => {
  let component: DCategoryComponent;
  let fixture: ComponentFixture<DCategoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DCategoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
