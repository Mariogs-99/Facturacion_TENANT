import { Component, Inject, HostListener } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css'],
})
export class ConfirmDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      cancelText: string;
      confirmText: string;
      message: string;
      title: string;
      data: any;
      type: any;
    },
    //!Establecido como readonly
    private readonly matDialogRef: MatDialogRef<ConfirmDialogComponent>
  ) {}
  public cancel() {
    this.close(false);
  }
  public close(value: any) {
    this.matDialogRef.close(value);
  }
  public confirm() {
    this.close(true);
  }

  @HostListener('keydown.esc')
  public onEsc() {
    this.close(false);
  }
}
