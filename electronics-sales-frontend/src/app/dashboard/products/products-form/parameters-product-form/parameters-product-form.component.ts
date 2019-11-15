import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { map, takeUntil } from 'rxjs/operators';
import { Observable, Subscription, BehaviorSubject, Subject } from 'rxjs';
import { ParameterTypeDto } from './../../../../models/dtos/paramter-type.dto';
import { Component, OnInit, Input, OnDestroy, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-parameters-product-form',
  templateUrl: './parameters-product-form.component.html',
  styleUrls: [
    '../../../dashboard.component.css',
    './parameters-product-form.component.css',
  ],
})
export class ParametersProductFormComponent implements OnInit, OnDestroy {
  @Input() parameterTypes$: Observable<ParameterTypeDto[]>;

  @Output() onChange = new EventEmitter(true);

  @Output() onInit = new EventEmitter(true);

  unscription$ = new Subject<void>();

  clear = new BehaviorSubject(false);

  @Input() parametersProductForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.parameterTypes$
    .pipe(takeUntil(this.unscription$))
    .subscribe(parameterTypes => {
      parameterTypes.forEach(parameterType => {
        this.parameters.push(
          this.createParameterGroupForm(
            parameterType.id,
            parameterType.parameterTypeName,
            parameterType.parameterTypeValue
          )
        );
      });
      this.onChange.emit(this.parametersProductForm);
    });
    this.onInit.emit(this.parametersProductForm);
  }

  get parameters() {
    return this.parametersProductForm.get('parameters') as FormArray;
  }

  createParameterGroupForm(
    id?: number,
    parameterTypeName?: string,
    parameterValue?: string
  ) {
    return this.formBuilder.group({
      id: [id],
      parameterTypeName: [parameterTypeName],
      parameterValue: [parameterValue, [Validators.required]],
    });
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
  }

  onValuesChange() {
    this.onChange.emit(this.parametersProductForm);
  }
}
