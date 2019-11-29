import { MatDialogRef, MatDialog, MAT_DIALOG_DATA, MatDialogConfig } from '@angular/material/dialog';
import { Component, OnInit, Inject } from '@angular/core';

export interface ConfirmDialogData {

  message: string;

  actionName?: string;

  action(): void;
}

const DEFAULT_ACTION_NAME = 'OK';

const DEFAULT_CONFIG: MatDialogConfig = {
  autoFocus: false,
  disableClose: true
};

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA)
    private data: ConfirmDialogData
  ) { }

  static open(dialog: MatDialog, data: ConfirmDialogData, config: MatDialogConfig = DEFAULT_CONFIG) {
    const configs = {data, ...config};
    return dialog.open(ConfirmDialogComponent, configs);
  }

  ngOnInit() {
  }

  get message() {
    return this.data.message;
  }

  get actionName() {
    const {actionName} = this.data;
    return actionName ? actionName : DEFAULT_ACTION_NAME;
  }

  action() {
    this.data.action();
    this.dialogRef.close();
  }

}
