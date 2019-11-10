import { ChooseImagesComponent } from './../choose-images/choose-images.component';
import { filter } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';
import {
  Component,
  OnInit,
  Input,
  OnDestroy,
  ViewChild,
  ViewContainerRef,
  ComponentFactoryResolver,
} from '@angular/core';

@Component({
  selector: 'app-multi-choose-images',
  templateUrl: './multi-choose-images.component.html',
  styleUrls: ['./multi-choose-images.component.css'],
})
export class MultiChooseImagesComponent implements OnInit, OnDestroy {
  @Input() class = 'col-12';

  imagesChoosed = new BehaviorSubject<any>(null);

  @Input() imagesInput: Observable<string[]>;

  imagesChoosed$ = this.imagesChoosed.asObservable();

  chooseImages: ChooseImagesComponent[] = [];

  imagesChoosedArray: any[] = [];

  @ViewChild('multiFileContainer', { read: ViewContainerRef, static: true })
  container: ViewContainerRef;

  constructor(private comFacResolver: ComponentFactoryResolver) {}

  ngOnInit() {
    this.appendChooseImage();
  }

  appendChooseImage(): ChooseImagesComponent {
    const chooseImageComponentElement = this.comFacResolver.resolveComponentFactory(
      ChooseImagesComponent
    );
    const dynamicComp = this.container.createComponent(
      chooseImageComponentElement
    );
    const chooseImageComponent = dynamicComp.instance;
    this.chooseImages.push(chooseImageComponent);
    chooseImageComponent.index = this.chooseImages.indexOf(
      chooseImageComponent
    );
    chooseImageComponent.onRemovedFile.subscribe(() => {
      const index = this.chooseImages.indexOf(
        chooseImageComponent
      );
      if (this.chooseImages.length > 1) {
        dynamicComp.destroy();
      }

      this.imagesChoosedArray.splice(index, 1);
      this.chooseImages.splice(index, 1);
      this.imagesChoosed.next(this.imagesChoosedArray);
    });

    chooseImageComponent.onSelectFile.subscribe((file: File) => {
      const index = this.chooseImages.indexOf(
        chooseImageComponent
      );
      if (this.imagesChoosedArray[index]) {
        this.imagesChoosedArray[index] = file;
      } else {
        this.imagesChoosedArray.push(file);
        this.appendChooseImage();
      }

      this.imagesChoosed.next(this.imagesChoosedArray);
    });

    return chooseImageComponent;
  }

  ngOnDestroy() {
    this.imagesChoosed.unsubscribe();
  }
}
