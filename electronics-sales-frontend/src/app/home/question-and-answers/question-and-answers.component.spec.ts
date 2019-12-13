import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionAndAnswersComponent } from './question-and-answers.component';

describe('QuestionAndAnswersComponent', () => {
  let component: QuestionAndAnswersComponent;
  let fixture: ComponentFixture<QuestionAndAnswersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuestionAndAnswersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuestionAndAnswersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
