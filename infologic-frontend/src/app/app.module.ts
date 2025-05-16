import { NgModule, CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ModalBasicoComponent } from './components/base/modal-basico/modalbasico.component';
import { BaseComponent } from './components/shared/base/base.component';
import { ContenidoModalComponent } from './components/base/contenido-modal/contenido-modal.component';
import { NavbarComponent } from './components/shared/navigation/navbar/navbar.component';
import { SidenavComponent } from './components/shared/navigation/sidenav/sidenav.component';
import { CompaniesComponent } from './pages/companies/companies.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ModalheaderComponent } from './components/base/modalheader/modalheader.component';
import { ModalcompanyComponent } from './pages/companies/modalcompany/modalcompany.component';
import { TableHeaderComponent } from './components/base/table-header/table-header.component';
import { MaterialModule } from './material.module';
import { LoginComponent } from './pages/autentificacion/login/login.component';
import { AuthRoutingModule } from './pages/autentificacion/auth.routing';
import { PagesModule } from './pages/pages.module';
import { PagesComponent } from './pages/pages.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ModalbranchComponent } from './pages/branch/modalbranch/modalbranch.component';
import { BranchComponent } from './pages/branch/branch.component';
import { RangesComponent } from './pages/branch/ranges/ranges.component';
import { ModuleHeaderComponent } from './components/base/module-header/module-header.component';
import { NetworkInterceptor } from './core/interceptors/network.interceptor';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { ErrorStateMatcher } from '@angular/material/core';
import { CustomErrorStateMatcher } from './core/errorStateMatcher/CustomErrorStateMatcher';
import { CountUpModule } from 'ngx-countup';

import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { PdfViewerModule } from 'ng2-pdf-viewer';

registerLocaleData(localeEs, 'es');


@NgModule({
  declarations: [
    LoginComponent,
    PagesComponent,
    AppComponent,
    DashboardComponent,
    ModalBasicoComponent,
    BaseComponent,
    ContenidoModalComponent,
    ModalBasicoComponent,
    NavbarComponent,
    SidenavComponent,
    CompaniesComponent,
    BranchComponent,
    ModalheaderComponent,
    ModalcompanyComponent,
    ModalbranchComponent,
    RangesComponent,
  ],
  imports: [
    MaterialModule,
    PagesModule,
    BrowserModule,
    AppRoutingModule,
    AuthRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    TableHeaderComponent,
    ModuleHeaderComponent,
    MatButtonModule,
    BrowserModule,
    CountUpModule, PdfViewerModule

  ],
  providers: [

    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    { provide: HTTP_INTERCEPTORS, useClass: NetworkInterceptor, multi: true },
    { provide: ErrorStateMatcher, useClass: CustomErrorStateMatcher },
    { provide: LOCALE_ID, useValue: 'es-ES' }

  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }
