import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/user.service';
import { ConfirmDialogComponent } from './../../../confirm/confirm-dialog.component';
import { EmployeeReceiver } from './../../../models/employee.receiver';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Component, OnInit, Input, ViewChild, AfterViewInit, EventEmitter, Output, OnDestroy, ElementRef } from '@angular/core';
import { EmployeeDataSource } from '../employees.component';
import { MatSort, Sort } from '@angular/material/sort';
import { Subject, fromEvent } from 'rxjs';
import { takeUntil, map, debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';

const EMPLOYEES_COLUMN = [
  'avartar',
  'fullname',
  'username',
  'gender',
  'birthday',
  'email',
  'phoneNumber',
  'address',
  'editAction',
  'deleteAction'
];

const DATE_FORMAT_PATTERN = 'dd-MM-yyyy';

@Component({
  selector: 'app-employees-data',
  templateUrl: './employees-data.component.html',
  styleUrls: ['./employees-data.component.css']
})
export class EmployeesDataComponent implements OnInit, AfterViewInit, OnDestroy {

  @Input() datasource: EmployeeDataSource;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  @ViewChild('filter', { static: true }) filter: ElementRef;

  @Output() pageChange = new EventEmitter<PageEvent>(true);

  @Output() editActionClicked = new EventEmitter<EmployeeReceiver>(true);

  @Output() filterChange = new EventEmitter<string>(true);

  @Output() sortChange = new EventEmitter<Sort>(true);

  @Output() deleteSuccess = new EventEmitter<any>(true);

  unscription$ = new Subject();

  displayedColumns = EMPLOYEES_COLUMN;

  dateFormatPattern = DATE_FORMAT_PATTERN;

  constructor(
    private dialog: MatDialog,
    private userService: UserService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    this.sort.sortChange
    .pipe(takeUntil(this.unscription$))
    .subscribe(value => this.sortChange.emit(value));

    this.paginator.page
      .pipe(takeUntil(this.unscription$))
      .subscribe(page => {
        this.pageChange.emit(page);
      });

    fromEvent<any>(this.filter.nativeElement, 'keyup')
      .pipe(
        takeUntil(this.unscription$),
        map(even => even.target.value),
        debounceTime(400),
        distinctUntilChanged()
      )
      .subscribe(filterKey => this.filterChange.emit(filterKey));
  }

  trackById(index: number, item: EmployeeReceiver) {
    return item.id;
  }

  onEditActionClick(employee: EmployeeReceiver) {
    this.editActionClicked.emit(employee);
  }

  onDeleteActionClick(employee: EmployeeReceiver) {
    ConfirmDialogComponent.open(this.dialog, {
      message: `Bạn có thực sự muốn xóa ${employee.fullname} ?`,
      actionName: 'Xóa',
      action: () => {
        this.userService
          .remove(employee.id)
          .subscribe(
            () => this.removeSuccess(),
            (err) => this.removeError(err)
        );
      }
    });
  }

  removeError(err: any): void {
    this.snackbar.open(`Có lỗi xảy ra !`, `Đóng`, { duration: 2000});
  }

  removeSuccess(): void {
    this.snackbar.open(`Xóa thành công !`, `Đóng`, { duration: 2000});
    this.deleteSuccess.emit();
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
  }
}
