import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyLazyLoadImageComponent } from './my-lazy-load-image.component';

describe('MyLazyLoadImageComponent', () => {
  let component: MyLazyLoadImageComponent;
  let fixture: ComponentFixture<MyLazyLoadImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyLazyLoadImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyLazyLoadImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
