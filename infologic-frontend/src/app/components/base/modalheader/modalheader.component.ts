import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';


@Component({
  selector: 'app-modal-header',
  templateUrl: './modalheader.component.html',
  template: `
    <div mat-dialog- class="w-100">
      <mat-toolbar style="background: #3c4557; color: white; display: flex; justify-content: space-between; align-items: center;">
        <div style="width: 20%;">
          <img alt="desc cnr fin recreativo" src="./assets/images/Logo_Gobierno.svg" alt="" style="height: 33px" />
        </div>

        <div style="width: 60%; text-align: center;">
          <span class="modal-tittle">{{ data.title }}</span>
        </div>

        <div style="width: 20%; text-align: right;">
          <button mat-icon-button (click)="closeModal()">
            <mat-icon>remove_circle</mat-icon>
          </button>
        </div>
      </mat-toolbar>
    </div>
  `,
})
export class ModalheaderComponent {


  constructor(
    public dialog: MatDialog,

  ) { }

  @Input() data: any;

  closeModal() {
    this.dialog.closeAll();
  }

}



