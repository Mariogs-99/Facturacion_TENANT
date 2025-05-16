import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
@Component({
  selector: 'app-contenido-modal',
  templateUrl: './contenido-modal.component.html',
})
export class ContenidoModalComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialog: MatDialog,
    //!Establecido como readonly 
    private readonly sharedDataService: SharedDataService
  ) {}

  public closeModal() {
    this.dialog.closeAll();
    this.sharedDataService.setVariable('UHRI-disabled', false);
  }
}
