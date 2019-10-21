import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeContentDefaultComponent } from './home-content-default.component';

describe('HomeContentDefaultComponent', () => {
  let component: HomeContentDefaultComponent;
  let fixture: ComponentFixture<HomeContentDefaultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeContentDefaultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeContentDefaultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
