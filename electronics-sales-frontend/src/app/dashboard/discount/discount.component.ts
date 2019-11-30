import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiscountFormDialogComponent } from './discount-form-dialog/discount-form-dialog.component';

@Component({
  selector: 'app-discount',
  templateUrl: './discount.component.html',
  styleUrls: ['./discount.component.css']
})
export class DiscountComponent implements OnInit {

  constructor(private dialog: MatDialog) { }

  ngOnInit() {
  }

  openAddForm() {
    DiscountFormDialogComponent.open(this.dialog);
  }

}
