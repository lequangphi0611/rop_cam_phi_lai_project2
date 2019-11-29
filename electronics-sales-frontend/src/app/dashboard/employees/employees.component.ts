import { Page } from './../../models/page.model';
import {
  EmployeeFetchOption,
  DEFAULT_EMPLOYEE_FETCH_OPTION,
  UserService
} from './../../services/user.service';
import { CollectionViewer } from '@angular/cdk/collections';
import { Observable, BehaviorSubject, Subject } from 'rxjs';
import { EmployeeReceiver } from './../../models/employee.receiver';
import { DataSource } from '@angular/cdk/table';
import { EmployeeFormDialogComponent } from './employee-form-dialog/employee-form-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import {
  Component,
  OnInit,
  AfterViewInit,
  ViewChild,
  OnDestroy
} from '@angular/core';
import { EmployeesDataComponent } from './employees-data/employees-data.component';
import { takeUntil, map } from 'rxjs/operators';
import { Sort } from '@angular/material/sort';

const FULLNAME_SORT = ['lastname', 'firstname'];

@Component({
  selector: 'app-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.css']
})
export class EmployeesComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild(EmployeesDataComponent, { static: true })
  employeesDataComponent: EmployeesDataComponent;

  datasource: EmployeeDataSource;

  private fetchEmployeeOption$ = new BehaviorSubject<EmployeeFetchOption>({
    ...DEFAULT_EMPLOYEE_FETCH_OPTION
  });

  currentOption: EmployeeFetchOption;

  unscription$ = new Subject();

  constructor(private dialog: MatDialog, private userService: UserService) {}

  ngOnInit() {
    this.datasource = new EmployeeDataSource(this.userService);
  }

  ngAfterViewInit(): void {
    this.fetchEmployeeOption$.subscribe(option => {
      this.currentOption = option;
      this.datasource.load(this.currentOption);
    });

    this.employeesDataComponent.pageChange
      .pipe(
        takeUntil(this.unscription$),
        map(page => {
          const { pageIndex, pageSize } = page;
          return {
            page: pageIndex,
            size: pageSize
          };
        }),
        map(option => this.mergeOption(this.currentOption, option))
      )
      .subscribe(option => this.fetchEmployeeOption$.next(option));

    this.employeesDataComponent.editActionClicked
      .pipe(takeUntil(this.unscription$))
      .subscribe(employee => this.openEditEmployeeDialog(employee));

    this.employeesDataComponent.filterChange
      .pipe(takeUntil(this.unscription$))
      .subscribe(filterKey => this.onFilterChange(filterKey));

    this.employeesDataComponent.sortChange
        .pipe(takeUntil(this.unscription$))
        .subscribe(sort => this.onSortChange(sort));

    this.employeesDataComponent.deleteSuccess
        .pipe(takeUntil(this.unscription$))
        .subscribe(() => this.datasource.load(this.currentOption));
  }

  mergeOption(
    ob1: EmployeeFetchOption = { ...DEFAULT_EMPLOYEE_FETCH_OPTION },
    ob2: EmployeeFetchOption = {}
  ): EmployeeFetchOption {
    return { ...ob1, ...ob2 };
  }

  openAddEmployeesDialog() {
    const dialogRef = EmployeeFormDialogComponent.open(this.dialog);

    dialogRef.componentInstance.saveSusccess.subscribe(() =>
      this.onSaveOrUpdateEmployeeSuccess()
    );
  }

  openEditEmployeeDialog(employee: EmployeeReceiver): void {
    const dialogRef = EmployeeFormDialogComponent.open(this.dialog, {
      data: {
        employee
      },
      autoFocus: false
    });
    dialogRef.componentInstance.saveSusccess.subscribe(() =>
      this.onSaveOrUpdateEmployeeSuccess()
    );
  }

  onSaveOrUpdateEmployeeSuccess() {
    this.fetchEmployeeOption$.next(this.currentOption);
  }

  onFilterChange(filterKey: string): void {
    this.fetchEmployeeOption$.next(
      this.mergeOption(this.currentOption, { search: filterKey })
    );
  }

  onSortChange(sort: Sort): void {
    const {active, direction} = sort;
    const option: EmployeeFetchOption = {};
    if (direction) {
      option.sort = active !== 'fullname' ? [active] : FULLNAME_SORT;
      option.direction = direction;
    }
    this.fetchEmployeeOption$.next(
      this.mergeOption(this.currentOption, option)
    );
  }

  ngOnDestroy(): void {
    this.unscription$.next();
    this.unscription$.complete();
  }
}

export class EmployeeDataSource extends DataSource<EmployeeReceiver> {
  private employees = new BehaviorSubject<EmployeeReceiver[]>([]);

  private page: Page<EmployeeReceiver>;

  constructor(private userService: UserService) {
    super();
  }

  connect(
    collectionViewer: CollectionViewer
  ): Observable<EmployeeReceiver[] | readonly EmployeeReceiver[]> {
    return this.employees.asObservable();
  }

  load(option?: EmployeeFetchOption) {
    this.userService.fetchEmployees(option).subscribe(result => {
      this.page = result;
      this.employees.next(result.content);
    });
  }

  get totalElement() {
    return !this.page ? 0 : this.page.totalElements;
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.employees.complete();
  }
}
