import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseImagesComponent } from './choose-images.component';

describe('ChooseImagesComponent', () => {
  let component: ChooseImagesComponent;
  let fixture: ComponentFixture<ChooseImagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChooseImagesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
