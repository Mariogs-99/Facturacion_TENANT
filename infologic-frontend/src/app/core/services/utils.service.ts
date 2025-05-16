import { Injectable, TemplateRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup } from '@angular/forms';

import { MatDialog } from '@angular/material/dialog';
import { ModalBasicoComponent } from '../../components/base/modal-basico/modalbasico.component';
import { MatSort } from '@angular/material/sort';
import { ScrollStrategy, ScrollStrategyOptions } from '@angular/cdk/overlay';
import Swal from 'sweetalert2';

type SwalIcon = 'success' | 'warning' | 'info' | 'question' | 'error';

@Injectable({
  providedIn: 'root',
})
export class UtilsService {
  public sorting: boolean = false;
  public EXCEL_TYPE: string =
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8';
  public EXCEL_EXT: string = '.xlsx';
  scrollStrategy: ScrollStrategy;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private readonly sso: ScrollStrategyOptions
  ) {
    this.scrollStrategy = this.sso.noop();
  }

  async goToPage(page: string, parent: boolean = true, data: any = {}) {
    try {
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';

      if (parent) {
        await this.router.navigate([page], { state: data });
      } else {
        await this.router
          .navigate([page], { queryParams: data })
          .then((res: any) => {});
      }
    } catch (error) {
      console.error('Error while navigating:', error);
    }
  }

  showSWAL(
    titulo: string = '',
    texto: string = '',
    boton: string = 'Aceptar',
    tipo: SwalIcon = 'success'
  ): Promise<any> {
    return new Promise<any>((resolve) => {
      return resolve(
        Swal.fire({
          title: titulo,
          html: texto,
          showCloseButton: true,
          allowOutsideClick: false,
          confirmButtonColor: '#008375',
          icon: tipo,
          confirmButtonText: boton,
        })
      );
    });
  }

  SWALYESNO(
    titulo: string = 'Esta seguro',
    message: string = 'De eliminar esto',
    btnText: string = 'Si, estoy seguro!',
    cancelText: string = 'Cancelar'
  ): Promise<any> {
    return new Promise<any>((resolve) => {
      Swal.fire({
        title: titulo,
        html: message,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#008375',
        cancelButtonColor: '#3c4557',
        confirmButtonText: btnText,
        cancelButtonText: cancelText,
      }).then((result) => {
        if (result.isConfirmed) {
          resolve(true);
        } else {
          resolve(false);
        }
      });
    });
  }
  is_loading: any;

  showloading() {
    this.is_loading = Swal.fire({
      html: 'Cargando la Información..',
      allowOutsideClick: false,
      showConfirmButton: false,
    });
  }

  dismissloading() {
    Swal.close();
  }

  validateForm(form: FormGroup) {
    let validate: boolean = true;
    for (const atr in form.value) {
      if (form.controls[atr].value != '' && form.controls[atr].value != null) {
        validate = true;
        return validate;
      } else {
        validate = false;
      }
    }
    return validate;
  }

  openModal(
    config: {
      title?: string;
      botonAceptar?: string;
      componentToLoad: any;
      callerComponent: any;
      disableClose?: true;
      data?: any;
      footer?: any;
    },
    minWidth = '50%',
    minHeight = '550px',
    maxWidth = '100%',
    maxHeight = '800px'
  ) {
    const defaultConfig = {
      title: 'Título predeterminado',
      botonAceptar: 'Aceptar',
      data: null,
      disableClose: true,
      footer: true,
    };

    const mergedConfig = { ...defaultConfig, ...config };

    const modalData = this.dialog.open(
      config.componentToLoad instanceof TemplateRef
        ? config.componentToLoad
        : ModalBasicoComponent,
      {
        minWidth: minWidth,
        minHeight: minHeight,
        maxHeight: maxHeight,
        maxWidth: maxWidth,
        panelClass: 'custom-dialog-container',
        data: mergedConfig,
        disableClose: true,
        scrollStrategy: this.scrollStrategy,
      }
    );

    modalData.afterClosed().subscribe((result) => {
      if (result) {
        return modalData;
      }
      return false;
    });

    return modalData;
  }

  localSortTable(column: string, sort: MatSort): MatSort {
    this.sorting = !this.sorting;
    sort.active = column;
    sort.direction = this.sorting ? 'desc' : 'asc';
    sort.start = sort.direction;
    return sort;
  }

 
  capitalize(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value.toUpperCase();

    const start = input.selectionStart;
    const end = input.selectionEnd;

    input.value = value;
    input.setSelectionRange(start, end);
  }

  capitalize2(event: KeyboardEvent): void {
    const input = event.target as HTMLInputElement;
    const start = input.selectionStart;
    const end = input.selectionEnd;

    input.value = input.value.toUpperCase();

    input.setSelectionRange(start, end);
  }

  fecha(timestamp: any) {
    const date = new Date(timestamp);

    const dia = date.getDate();
    const mes = date.getMonth() + 1;
    const año = date.getFullYear();

    const diaString = dia < 10 ? '0' + dia : dia;
    const mesString = mes < 10 ? '0' + mes : mes;

    return `${diaString}/${mesString}/${año}`;
  }


  tipo_DTE(tipoDTE: any) {
    let tipoDocumento: string;
  
    switch (tipoDTE) {
      case '01':
      case '35':
        tipoDocumento = 'FACTURA';
        break;
      case '03':
      case '36':
        tipoDocumento = 'COMPROBANTE_CREDITO_FISCAL';
        break;
      case '04':
      case '37':
        tipoDocumento = 'NOTA_REMISION';
        break;
      case '05':
      case '38':
        tipoDocumento = 'NOTA_CREDITO';
        break;
      case '06':
      case '39':
        tipoDocumento = 'NOTA_DEBITO';
        break;
      case '07':
      case '40':
        tipoDocumento = 'COMPROBANTE_RETENCION';
        break;
      case '08':
      case '41':
        tipoDocumento = 'COMPROBANTE_LIQUIDACION';
        break;
      case '09':
      case '42':
        tipoDocumento = 'DOCUMENTO_CONTABLE_LIQUIDACION';
        break;
      case '11':
      case '43':
        tipoDocumento = 'FACTURAS_EXPORTACION';
        break;
      case '14':
      case '44':
        tipoDocumento = 'FACTURA_SUJETO_EXCLUIDO';
        break;
      case '15':
      case '45':
        tipoDocumento = 'COMPROBANTE_DONACION';
        break;
      default:
        tipoDocumento = 'Tipo de documento no reconocido';
    }
  
    return tipoDocumento;
  }
  
}
