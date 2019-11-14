import { ChooseImagesComponent } from './../choose-images/choose-images.component';
import { filter, finalize } from 'rxjs/operators';
import { BehaviorSubject, Observable, of, Subscription } from 'rxjs';
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

  @Input() imageUrls$: Observable<string[]> = of(null);

  imagesChoosed$ = this.imagesChoosed.asObservable();

  chooseImages: ChooseImagesComponent[] = [];

  imagesChoosedArray: any[] = [];

  subscription: Subscription = new Subscription();

  @ViewChild('multiFileContainer', { read: ViewContainerRef, static: true })
  container: ViewContainerRef;

  constructor(private comFacResolver: ComponentFactoryResolver) {}

  ngOnInit() {
    this.subscription.add(
      this.imageUrls$
        .pipe(
          filter(imageUrls => {
            this.removeAll();
            if (!imageUrls || imageUrls.length === 0) {
              this.appendChooseImage();
              return false;
            }
            return true;
          })
        )
        .subscribe(imageUrls => {
          this.removeAll();
          imageUrls.forEach(url => this.appendChooseImage(url, imageUrls.length));
        })
    );
    this.appendChooseImage();
  }

  appendChooseImage(url?: string, max?: number): ChooseImagesComponent {
    console.log('create');
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
    chooseImageComponent.fileUrl = url;
    chooseImageComponent.onRemovedFile.subscribe(() => {
      const index = this.chooseImages.indexOf(chooseImageComponent);
      this.chooseImages.splice(index, 1);
      if (this.chooseImages.length >= 0) {
        dynamicComp.destroy();
      }
      this.imagesChoosedArray.splice(index, 1);
      this.imagesChoosed.next(this.imagesChoosedArray);
    });

    chooseImageComponent.onSelectFile.subscribe((file: File) => {
      const index = this.chooseImages.indexOf(chooseImageComponent);
      if (this.imagesChoosedArray[index]) {
        this.imagesChoosedArray[index] = file;
      } else {
        this.imagesChoosedArray.push(file);
        if (!max || max === 0 || (index + 1) === max) {
          this.appendChooseImage();
        }
      }

      this.imagesChoosed.next(this.imagesChoosedArray);
    });

    return chooseImageComponent;
  }

  removeAll() {
    this.chooseImages.forEach((component, i) => this.container.remove(i));
  }

  ngOnDestroy() {
    this.imagesChoosed.unsubscribe();
    this.subscription.unsubscribe();
  }
}
