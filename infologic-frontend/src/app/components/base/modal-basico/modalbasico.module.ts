import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatMenuModule } from '@angular/material/menu';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { ModalBasicoRoutingModule } from './modalbasico-routing.module';
import { PdfViewerModule } from 'ng2-pdf-viewer';

@NgModule({
  declarations: [],
  imports: [
    MatCardModule,
    MatIconModule,
    MatFormFieldModule,
    MatTableModule,
    MatMenuModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    MatSelectModule,
    MatDialogModule,
    ModalBasicoRoutingModule,
    PdfViewerModule
  ],
  exports: [],
})
export class ModalBasicoModule {}
