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

  hasAppend = new BehaviorSubject<boolean>(false);

  hashAppend$ = this.hasAppend.asObservable();

  subscription: Subscription = new Subscription();

  @ViewChild('multiFileContainer', { read: ViewContainerRef, static: true })
  container: ViewContainerRef;

  constructor(private comFacResolver: ComponentFactoryResolver) {}

  ngOnInit() {
    this.subscription.add(
      this.imageUrls$
        .pipe(
          filter(imageUrls => {
            if (!imageUrls || imageUrls.length === 0) {
              this.init();
              return false;
            }
            return true;
          })
        )
        .subscribe(imageUrls => {
          this.hasAppend.next(false);
          imageUrls.forEach(url => this.appendChooseImage(url, imageUrls.length));
        })
    );
    this.hashAppend$.subscribe((appendable) => {
      if (appendable) {
        this.appendChooseImage(null, this.imagesChoosedArray.length + 1);
      } else {
        this.removeAll();
      }
    });
  }

  init() {
    this.chooseImages.forEach((c, i) => {
      this.container.remove(i);
      this.chooseImages.splice(i, 1);
    });
    this.hasAppend.next(true);
  }

  appendChooseImage(url?: string, max = 1): ChooseImagesComponent {
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
      this.imagesChoosedArray.splice(index, 1);
      if (this.imagesChoosedArray.length >= 0) {
        dynamicComp.destroy();
      }
      this.imagesChoosed.next(this.imagesChoosedArray);
    });

    chooseImageComponent.onSelectFile.subscribe((file: File) => {
      const index = this.chooseImages.indexOf(chooseImageComponent);
      if (this.imagesChoosedArray[index]) {
        this.imagesChoosedArray[index] = file;
      } else {
        this.imagesChoosedArray.push(file);
        if (this.imagesChoosedArray.length === max) {
          this.hasAppend.next(true);
        }
      }

      this.imagesChoosed.next(this.imagesChoosedArray);
    });

    return chooseImageComponent;
  }

  removeAll() {
    this.chooseImages
      .filter(component => component.fileUrl == null)
      .forEach((component, i) => {
      this.container.remove(i);
      this.chooseImages.splice(i, 1);
    });
  }

  ngOnDestroy() {
    this.imagesChoosed.complete();
    this.subscription.unsubscribe();
    this.hasAppend.complete();
  }
}
