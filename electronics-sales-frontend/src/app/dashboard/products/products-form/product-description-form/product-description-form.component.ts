import { filter, takeUntil } from 'rxjs/operators';
import { Observable, BehaviorSubject, Subscription, of, Subject } from 'rxjs';
import { ParagraphDto } from './../../../../models/dtos/paragraph.dto';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import {
  Component,
  OnInit,
  Input,
  OnDestroy,
  EventEmitter,
  Output,
} from '@angular/core';

@Component({
  selector: 'app-product-description-form',
  templateUrl: './product-description-form.component.html',
  styleUrls: [
    '../../../dashboard.component.css',
    './product-description-form.component.css',
  ],
})
export class ProductDescriptionFormComponent implements OnInit, OnDestroy {
  @Input() descriptionForm: FormGroup;

  @Input() descriptions$: Observable<ParagraphDto[]> = of(null);

  @Output() onChange = new EventEmitter(true);

  @Output() onInit = new EventEmitter(true);

  unscription$ = new Subject<void>();

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {

    this.descriptions$
      .pipe(takeUntil(this.unscription$), filter(paragraphs => paragraphs && paragraphs.length > 0))
      .subscribe(paragraphs => {
        paragraphs.forEach(paragraph => this.addFormArray(paragraph));
      });
    this.onChange.emit(this.descriptionForm);
    this.onInit.emit(this.descriptionForm);
  }

  get descriptions() {
    return this.descriptionForm.get('descriptions') as FormArray;
  }

  addFormArray(paragraphDto?: ParagraphDto) {
    this.descriptions.push(this.initDescriptionControl(paragraphDto));
  }

  initDescriptionControl(paragraphDto?: ParagraphDto) {
    const title = paragraphDto ? paragraphDto.title : [null];
    const text = paragraphDto ? paragraphDto.text : [null];
    return this.formBuilder.group({
      title,
      text,
    });
  }

  removeFormAt(index: number) {
    this.descriptions.removeAt(index);
  }

  onValuesChange() {
    this.onChange.emit(this.descriptionForm);
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
