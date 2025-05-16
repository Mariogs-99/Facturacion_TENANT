import { Component, Inject } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';

@Component({
  selector: 'app-modalbasico',
  templateUrl: './modalbasico.component.html',
  styleUrls: ['./modalbasico.component.css'],
})
export class ModalBasicoComponent {
  constructor(
    public dialog: MatDialog,
    //!Marcado como readonly
    private readonly dialogRef: MatDialogRef<ModalBasicoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  aceptarDialog() {
    this.data.callerComponent.aceptarDialog();
    this.dialogRef.close();
  }

  closeModal(): void {
    this.dialog.closeAll();
  }
}
