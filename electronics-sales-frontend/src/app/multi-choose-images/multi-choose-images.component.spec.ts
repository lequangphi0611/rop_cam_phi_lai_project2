import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultiChooseImagesComponent } from './multi-choose-images.component';

describe('MultiChooseImagesComponent', () => {
  let component: MultiChooseImagesComponent;
  let fixture: ComponentFixture<MultiChooseImagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultiChooseImagesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultiChooseImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
