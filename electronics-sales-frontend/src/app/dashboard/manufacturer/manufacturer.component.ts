import { ManufacturerFormDialogComponent } from './manufacturer-form-dialog/manufacturer-form-dialog.component';
import { MatPaginator } from '@angular/material/paginator';
import { finalize, map, tap, takeUntil, filter } from 'rxjs/operators';
import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { ManufacturerView } from './../../models/view-model/manufacturer.view.model';
import { Observable, BehaviorSubject, pipe, Subject } from 'rxjs';
import { ManufacturerService } from './../../services/manufacturer.service';
import { Component, OnInit, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-manufacturer',
  templateUrl: './manufacturer.component.html',
  styleUrls: ['./manufacturer.component.css']
})
export class ManufacturerComponent implements OnInit, AfterViewInit, OnDestroy {

  dataSource: ManufacturerDataSource;

  private unscription$ = new Subject();

  displayedColumns = [
    'logo',
    'name',
    'edit',
    'delete',
  ];

  valueChanged$ = new BehaviorSubject<boolean>(false);

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  countManufacturer: number;

  defaultPage = 0;

  defaultSize = 5;

  readonly defautDialogConfig: MatDialogConfig = {
    autoFocus: false,
    disableClose: false
  };

  constructor(
    private manufacturerService: ManufacturerService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit() {
    this.dataSource = new ManufacturerDataSource(this.manufacturerService);
    this.dataSource.loadManufacturer(this.defaultPage, this.defaultSize);
    this.fetchManufacturerCount();

    this.valueChanged$
      .pipe(filter(v => v ))
      .subscribe(() => this.loadManufacturers());
  }

  fetchManufacturerCount() {
    this.manufacturerService
    .fetchAll()
    .pipe(map(v => v.length))
    .subscribe(v => this.countManufacturer = v);
  }

  ngAfterViewInit(): void {
    this.paginator.page
      .pipe(
        tap(() => this.valueChanged$.next(true)
      ))
      .subscribe();
  }

  loadManufacturers() {
    this.dataSource.loadManufacturer(
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }

  openAddForm() {
    const dialogConfig = {...this.defautDialogConfig};

    dialogConfig.data = {
      edit: false
    };

    const dialogRef = this.dialog.open(ManufacturerFormDialogComponent, dialogConfig);

    dialogRef.afterClosed()
    .pipe(
      filter(v => v),
      tap(() => {
        this.countManufacturer++;
        this.paginator.pageIndex = this.defaultPage;
        this.valueChanged$.next(true);
      })
    )
    .subscribe();
  }

  openEditForm(manufacturer: ManufacturerView) {
    const dialogConfig = {...this.defautDialogConfig};

    dialogConfig.data = {
      edit: true,
      manufacturer
    };

    const dialogRef = this.dialog.open(ManufacturerFormDialogComponent, dialogConfig);

    dialogRef.afterClosed()
    .pipe(
      filter(v => v),
      tap(() => this.valueChanged$.next(true))
    )
    .subscribe();
  }

  deleteManufacturer(manufacturerId: number) {
    this.manufacturerService
      .delete(manufacturerId)
      .subscribe(() => {
        this.countManufacturer--;
        this.valueChanged$.next(true);
        this.snackbar.open('Xóa thành công !', 'đóng', {
          duration: 2000
        });
      });
  }

  trackById(index: number, item: ManufacturerView) {
    return item.id;
  }

  ngOnDestroy() {
    this.unscription$.next();
    this.unscription$.complete();
  }

}

export class ManufacturerDataSource extends DataSource<ManufacturerView> {

  private manufacturers = new BehaviorSubject<ManufacturerView[]>([]);

  private loading = new BehaviorSubject<boolean>(false);

  loading$ = this.loading.asObservable();

  constructor(private manufacturerService: ManufacturerService) {
    super();

  }

  connect(collectionViewer: CollectionViewer): Observable<ManufacturerView[] | readonly ManufacturerView[]> {
    return this.manufacturers.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.loading.complete();
    this.manufacturers.complete();
  }

  loadManufacturer(page = 0, size?: number) {
    this.loading.next(true);
    this.manufacturerService
      .fetchAll(page, size)
      .pipe(finalize(() => this.loading.next(false)))
      .subscribe((response) => {
        this.manufacturers.next(response);
      });
  }

}
