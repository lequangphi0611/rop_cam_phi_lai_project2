import { BehaviorSubject } from 'rxjs';
import { ManufacturerService } from './../../../services/manufacturer.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit, Inject, ViewChild, AfterViewInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ManufacturerDto } from 'src/app/models/dtos/manufacturer.dto';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChooseImagesComponent } from 'src/app/choose-images/choose-images.component';

@Component({
  selector: 'app-manufacturer-form-dialog',
  templateUrl: './manufacturer-form-dialog.component.html',
  styleUrls: ['./manufacturer-form-dialog.component.css']
})
export class ManufacturerFormDialogComponent implements OnInit{

  manufacturerForm: FormGroup;

  file: File;

  private changed = false;

  @ViewChild(ChooseImagesComponent, {static: true}) chooseImages: ChooseImagesComponent;

  constructor(
    private formBuilder: FormBuilder,
    private manufacturerService: ManufacturerService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<ManufacturerFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    this.manufacturerForm = this.formBuilder.group({
      manufacturerName : [null, [Validators.required]]
    });
  }

  get manufacturerNameControl() {
    return this.manufacturerForm.get('manufacturerName');
  }

  close() {
    this.dialogRef.close(this.changed);
  }

  onFileSelected(file: File) {
    this.file = file;
  }

  onFileRemoved() {
    this.file = null;
  }

  getManufacturer(): ManufacturerDto {
    return {
      manufacturerName: this.manufacturerNameControl.value
    };
  }

  submit() {
    const manufacturer = this.getManufacturer();
    this.manufacturerService.create(manufacturer, this.file)
      .subscribe(() => this.onSuccess());
  }

  onSuccess(): void {
    this.snackBar.open('Thành công !', 'đóng', {
      duration: 2000
    });
    this.changed = true;
    this.resetForm();
  }

  resetForm() {
    this.manufacturerNameControl.reset();
    this.chooseImages.onRemoveFile();
  }

}
