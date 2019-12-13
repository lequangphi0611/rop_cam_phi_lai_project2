import { switchMap, tap, map, filter, debounceTime } from 'rxjs/operators';
import { ManufacturerView } from './../../../models/view-model/manufacturer.view.model';
import { BehaviorSubject, pipe, of, Observable, fromEvent } from 'rxjs';
import { ManufacturerService } from './../../../services/manufacturer.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import {
  Component,
  OnInit,
  Inject,
  ViewChild,
  AfterViewInit,
  EventEmitter,
  Output,
  ElementRef,
} from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ManufacturerDto } from 'src/app/models/dtos/manufacturer.dto';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChooseImagesComponent } from 'src/app/choose-images/choose-images.component';

@Component({
  selector: 'app-manufacturer-form-dialog',
  templateUrl: './manufacturer-form-dialog.component.html',
  styleUrls: ['./manufacturer-form-dialog.component.css'],
})
export class ManufacturerFormDialogComponent implements OnInit, AfterViewInit {
  manufacturerForm: FormGroup;

  file: File;

  @Output() saveSuccess = new EventEmitter(true);

  @ViewChild(ChooseImagesComponent, { static: true })
  chooseImages: ChooseImagesComponent;

  @ViewChild('manufacturerNameInput', {static: true})
  manufacturerNameInput: ElementRef;

  constructor(
    private formBuilder: FormBuilder,
    private manufacturerService: ManufacturerService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<ManufacturerFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data
  ) {}

  ngOnInit() {
    this.initForm();
  }

  ngAfterViewInit() {
    fromEvent<any>(this.manufacturerNameInput.nativeElement, 'input')
      .pipe(
        debounceTime(500),
        map(event => event.target.value),
        filter(value => value && value.trim().length > 0),
        switchMap(value => this.checkExistsManufacturerName(value)),
        filter(exists => exists)
      ).subscribe(() => {
        this.manufacturerNameControl.setErrors({
          existsName: true,
        });
      });
  }

  checkExistsManufacturerName(manufacturerName: string): Observable<boolean> {
    if (this.editMode && this.manufacturer.manufacturerName.toLowerCase() === manufacturerName.toLowerCase()) {
      return of(false);
    }
    return this.manufacturerService.existsByName(manufacturerName);
  }

  get manufacturer() {
    return !this.data.manufacturer
      ? null
      : (this.data.manufacturer as ManufacturerView);
  }

  get manufacturerName() {
    return !this.manufacturer ? null : this.manufacturer.manufacturerName;
  }

  get manufacturerId() {
    return !this.manufacturer ? null : this.manufacturer.id;
  }

  get fileUrl() {
    return !this.manufacturer
      ? null
      : `/api/images/${this.manufacturer.imageId}`;
  }

  get editMode() {
    return !this.data.edit ? false : this.data.edit;
  }

  initForm() {
    this.manufacturerForm = this.formBuilder.group({
      manufacturerName: [this.manufacturerName, [Validators.required]],
    });
  }

  get manufacturerNameControl() {
    return this.manufacturerForm.get('manufacturerName');
  }

  close() {
    this.dialogRef.close(false);
  }

  onFileSelected(file: File) {
    this.file = file;
  }

  onFileRemoved() {
    this.file = null;
  }

  getManufacturer(): ManufacturerDto {
    return {
      manufacturerName: this.manufacturerNameControl.value.trim(),
    };
  }

  submit() {
    const manufacturer = this.getManufacturer();
    of(this.editMode)
      .pipe(
        switchMap(edit => {
          if (!edit) {
            return this.manufacturerService.create(manufacturer, this.file);
          }
          manufacturer.id = this.manufacturerId;
          return this.manufacturerService.update(manufacturer, this.file);
        }),
        tap(
          (result) => this.onSuccess(result),
          err => this.onError(err)
        )
      )
      .subscribe();
  }
  onError(err): void {
    this.snackBar.open('Không Thành công !', 'Đóng', {
      duration: 2000,
    });
  }

  onSuccess(result: any): void {
    this.snackBar.open('Thành công !', 'Đóng', {
      duration: 2000,
    });
    this.dialogRef.close(true);
    this.saveSuccess.emit(result);
  }
}
