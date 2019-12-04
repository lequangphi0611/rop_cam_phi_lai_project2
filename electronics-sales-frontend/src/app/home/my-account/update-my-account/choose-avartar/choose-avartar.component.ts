import { map, filter } from 'rxjs/operators';
import {
  Component,
  OnInit,
  Input,
  ViewChild,
  ElementRef,
  Output,
  EventEmitter,
  OnDestroy
} from '@angular/core';
import { fromEvent } from 'rxjs';

// const BASE_64 = 'data:image/png;base64,';

@Component({
  selector: 'app-choose-avartar',
  templateUrl: './choose-avartar.component.html',
  styleUrls: ['./choose-avartar.component.css']
})
export class ChooseAvartarComponent implements OnInit, OnDestroy {
  defaultImage = '/assets/img/avatar_user.png';

  currentFileUrl: any;

  @ViewChild('fileInput', { static: true }) fileInput: ElementRef;

  @Output() fileChange = new EventEmitter<File>(true);

  constructor() {}

  ngOnInit() {
    fromEvent<any>(this.fileInput.nativeElement, 'change')
      .pipe(
        map(event => event.target.files),
        map(files => files[0]),
        filter(file => file != null)
      )
      .subscribe(file => this.readFile(file));
  }

  readFile(file: File) {
    this.fileChange.emit(file);
    const reader = new FileReader();
    reader.onload = event => {
      const urlResult = event.target['result'] as string;
      const fileUrl = `data:image/png;base64,${urlResult.substr(
        urlResult.indexOf(',') + 1
      )}`;
      this.currentFileUrl = fileUrl;
    };
    reader.readAsDataURL(file);
  }



  clear() {
    this.currentFileUrl = null;
    this.fileChange.next(null);
  }

  get image() {
    return this.currentFileUrl ? this.currentFileUrl : this.defaultImage;
  }

  set fileUrl(fileUrl: string) {
    if (!fileUrl) {
      this.clear();
    }

    this.currentFileUrl = fileUrl;
    fetch(fileUrl)
      .then(res => res.blob())
      .then(blob => {
        const file = new File([blob], 'filename');
        this.fileChange.next(file);
      });
  }

  ngOnDestroy() {
    this.fileChange.complete();
  }
}
