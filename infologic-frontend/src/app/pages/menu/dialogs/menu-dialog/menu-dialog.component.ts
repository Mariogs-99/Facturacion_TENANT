import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-menu-dialog',
  templateUrl: './menu-dialog.component.html',
})
export class MenuDialogComponent {
  public disabledItems: boolean = true;

  constructor(@Inject(MAT_DIALOG_DATA) public modalData: any) {
    if (modalData.data.menu != null) {
      this.disabledItems = false;
    }
  }
}
