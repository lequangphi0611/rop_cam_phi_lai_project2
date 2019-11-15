import { BehaviorSubject, Observable } from 'rxjs';
import {
  Component,
  OnInit,
  EventEmitter,
  Output,
  OnDestroy,
  Input,
  ViewChild,
} from '@angular/core';
import { filter, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-choose-images',
  templateUrl: './choose-images.component.html',
  styleUrls: ['./choose-images.component.css'],
})
export class ChooseImagesComponent implements OnInit, OnDestroy {
  @Input() class = 'col-12';

  @Input() index = 0;

  @Output() onSelectFile = new EventEmitter<any>(true);

  @Output() onRemovedFile = new EventEmitter<any>(true);

  currentFile: any = null;

  private filesSelected = new BehaviorSubject(null);

  filesSelected$ = this.filesSelected.asObservable();

  @Input() fileUrl: any;

  constructor() {}

  ngOnInit() {
    if (this.fileUrl) {
      fetch(this.fileUrl)
        .then(res => res.blob())
        .then(blob => {
          const file = new File([blob], 'filename');
          this.onSelectFile.emit(file);
          this.filesSelected.next(file);
        });
    }
    this.onFilesSelectedSubcription();
  }

  onFilesSelectedSubcription() {
    this.filesSelected$
      .pipe(
        filter(file => {
          if (file) {
            return true;
          }
          this.fileUrl = null;
          return false;
        })
      )
      .subscribe(file => {
        this.currentFile = file;
        const reader = new FileReader();
        reader.onload = event => {
          const urlResult = event.target['result'] as string;
          const fileUrl = `data:image/png;base64,${urlResult.substr(urlResult.indexOf(',') + 1)}`;
          this.fileUrl = fileUrl;
        };
        reader.readAsDataURL(file);
      });
  }

  async onSelectedFile(event: Event) {
    const files = event.target['files'];
    let file = null;
    if (files.length === 0) {
      file = this.currentFile;
    } else {
      file = files[0];
    }
    this.onSelectFile.emit(file);
    this.filesSelected.next(file);
  }

  async onRemoveFile() {
    this.filesSelected.next(null);
    this.onRemovedFile.emit();
  }

  ngOnDestroy(): void {
    this.filesSelected.next(null);
    this.filesSelected.complete();
  }
}
