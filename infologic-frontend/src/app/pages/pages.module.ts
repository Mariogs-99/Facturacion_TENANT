import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTreeModule } from '@angular/material/tree';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { MaterialModule } from '../material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MenuComponent } from './menu/menu.component';
import { ModuleHeaderComponent } from '../components/base/module-header/module-header.component';
import { MenuItemsComponent } from './menu/views/menu-items/menu-items.component';
import { ManageMenuComponent } from './menu/views/manage-menu/manage-menu.component';
import { MenuDialogComponent } from './menu/dialogs/menu-dialog/menu-dialog.component';
import { ManageMenuItemsComponent } from './menu/views/menu-items/views/manage-menu-items/manage-menu-items.component';
import { RolesComponent } from './roles/roles.component';
import { PermissionsComponent } from './permissions/permissions.component';
import { ManagePermissionComponent } from './permissions/dialogs/manage-permission/manage-permission.component';
import { UserComponent } from './user/user.component';
import { ManageRolesComponent } from './roles/dialogs/manage-roles/manage-roles.component';
import { ManageUserComponent } from './user/dialogs/manage-user/manage-user.component';
import { VentasComponent } from './ventas/ventas.component';
import { InstitucionComponent } from './institucion/institucion.component';
import { ModalPdfComponent } from './ventas/modal-pdf/modal-pdf.component';
import { ModalAnulacionComponent } from './ventas/modal-anulacion/modal-anulacion.component';
import { ModalClientesComponent } from './ventas/modal-clientes/modal-clientes.component';
import { ModalVentasComponent } from './ventas/modal-ventas/modal-ventas.component';
import { TableHeaderComponent } from '../components/base/table-header/table-header.component';
import { CommonModule, DatePipe, registerLocaleData } from '@angular/common';
import {
  DateAdapter,
  ErrorStateMatcher,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
  MatNativeDateModule,
} from '@angular/material/core';
import { CustomErrorStateMatcher } from '../core/errorStateMatcher/CustomErrorStateMatcher';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { ResetPasswordComponent } from './autentificacion/reset-password/reset-password.component';
import { ForgotPasswordComponent } from './autentificacion/forgot-password/forgot-password.component';
import { ReportesComponent } from './reportes/reportes.component';
import { ComprasComponent } from './compras/compras.component';
import { ManageComprasComponent } from './compras/views/manage-compras/manage-compras.component';
import { CustomDateAdapter } from '../core/adapters/CustomDateAdapter';
import { MatDatepickerModule } from '@angular/material/datepicker';

import localeEs from '@angular/common/locales/es';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { ProductoComponent } from './producto/producto.component';
import { ManageProductoComponent } from './producto/dialogs/manage-producto/manage-producto.component';
import { ClienteComponent } from './cliente/cliente.component';
import { ManageClienteComponent } from './cliente/dialogs/manage-cliente/manage-cliente.component';
registerLocaleData(localeEs, 'es');

export const MY_FORMATS = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'DD/MM/YYYY',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@NgModule({
  declarations: [
    MenuComponent,
    MenuItemsComponent,
    ManageMenuComponent,
    MenuDialogComponent,
    ManageMenuItemsComponent,
    ReportesComponent,
    RolesComponent,
    PermissionsComponent,
    ManagePermissionComponent,
    ManageRolesComponent,
    UserComponent,
    ManageUserComponent,
    VentasComponent,
    InstitucionComponent,
    ModalPdfComponent,
    ModalAnulacionComponent,
    ModalClientesComponent,
    ModalVentasComponent,
    ResetPasswordComponent,
    ForgotPasswordComponent,
    ComprasComponent,
    ManageComprasComponent,
    ProductoComponent,
    ManageProductoComponent,
    ClienteComponent,
    ManageClienteComponent,
  ],
  exports: [ModuleHeaderComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatTreeModule,
    MatMenuModule,
    MatCardModule,
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
    ModuleHeaderComponent,
    TableHeaderComponent,
    NgxMaskDirective,
    NgxMaskPipe,
    DatePipe,
    MatDatepickerModule, 
    MatNativeDateModule, 
    CommonModule,
    PdfViewerModule
  ],
  providers: [
    { provide: DateAdapter, useClass: CustomDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
    { provide: MAT_DATE_LOCALE, useValue: 'es-ES' },
    { provide: ErrorStateMatcher, useClass: CustomErrorStateMatcher },
    provideNgxMask(),
  ],
  bootstrap: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class PagesModule {}
