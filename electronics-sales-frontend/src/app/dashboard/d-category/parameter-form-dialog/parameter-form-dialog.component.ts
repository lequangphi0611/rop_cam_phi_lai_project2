import { MatSnackBar } from '@angular/material/snack-bar';
import { ParameterTypeDto } from './../../../models/dtos/paramter-type.dto';
import { Observable } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ParameterTypeService } from 'src/app/services/parameter-type.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, OnInit, Inject, Output, EventEmitter } from '@angular/core';
import { debounceTime, distinct, map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-parameter-form-dialog',
  templateUrl: './parameter-form-dialog.component.html',
  styleUrls: ['./parameter-form-dialog.component.css']
})
export class ParameterFormDialogComponent implements OnInit {

  @Output() saveSuccess = new EventEmitter(true);

  parameterTypeForm: FormGroup;

  parametersAvailable$: Observable<ParameterTypeDto[]>;

  constructor(
    private parameterTypeService: ParameterTypeService,
    private formBuilder: FormBuilder,
    private snackbar: MatSnackBar,
    private dialogRef: MatDialogRef<ParameterFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data
  ) { }

  ngOnInit() {
    this.parameterTypeForm = this.formBuilder.group({
      parameterTypeName: ['', [Validators.required]]
    });
    this.parametersAvailable$ = this.parameterTypeNameControl.valueChanges
      .pipe(
        debounceTime(200),
        distinct(),
        switchMap(v => this.parameterTypeService.fetchAll())
      );
  }

  get parameterTypeNameControl() {
    return this.parameterTypeForm.get('parameterTypeName');
  }

  get parameterTypeNameValue() {
    return this.parameterTypeNameControl.value;
  }

  close() {
    this.dialogRef.close(false);
  }

  onOptionSelect(parameterType: ParameterTypeDto) {
    this.parameterTypeNameControl.setValue(parameterType.parameterTypeName);
  }

  submit() {
    this.parameterTypeService.create({
      parameterTypeName: this.parameterTypeNameValue
    })
    .subscribe((result) => this.onSuccess(result), err => this.onError(err));
  }
  onError(err: any): void {
    this.snackbar.open('Không thành công', 'Đóng', {duration: 2000});
  }

  onSuccess(result: any) {
    this.saveSuccess.emit(result);
    this.snackbar.open('Thành công', 'Đóng', {duration: 2000});
    this.dialogRef.close();
  }

}
