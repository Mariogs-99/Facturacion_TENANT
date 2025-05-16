import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Reporte, Sucursal } from 'src/app/core/model/reportes/resportes.model';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { ReportService } from 'src/app/core/services/report.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import {
  DateAdapter,
  MAT_DATE_LOCALE,
  MAT_DATE_FORMATS,
} from '@angular/material/core';
import localeEs from '@angular/common/locales/es';
import { registerLocaleData } from '@angular/common';
import { CustomDateAdapter } from 'src/app/core/adapters/CustomDateAdapter';
import { PuntoDeVenta } from '../../core/model/reportes/resportes.model';

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

@Component({
  selector: 'app-reportes',
  templateUrl: './reportes.component.html',
  styleUrls: ['./reportes.component.css'],
  providers: [
    { provide: DateAdapter, useClass: CustomDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
    { provide: MAT_DATE_LOCALE, useValue: 'es-ES' },
  ],
})
export class ReportesComponent {
  fechaInicial!: Date;
  fechaFinal!: Date;
  fecha!: Date;

  yearsBefore: number = 10;
  yearsAfter: number = 0;

  selectedReporte!: string;
  selectedModulo!: string;
  selectedReporteTesoreria!: string;
  selectedFormato!: string;
  selectedFormatoTes!: string;

  selectedSucursal: any = null;
  selectedPuntoDeVenta: any = null;


  pdfSRC: any = '';
  pdfSRCTes: any = '';
  zoom: number = 1.0;
  zoom2: number = 1.0;

  constructor(
    private http: HttpClient,
    private sanitizer: DomSanitizer,
    private api: ApiService,
    private reportService: ReportService,
    public util: UtilsService,
    public auth: AuthService,
    private sharedDataService: SharedDataService
  ) {}

  fechaInicialStr = '';
  fechaFinalStr = '';
  fechaStr = '';

  modulos: any[] = [];

  reportesContabilidad: any[] = [];
  reportesTesoreria: Reporte[] = [];

  sucursales: Sucursal[] = [];
  puntoDeVenta: PuntoDeVenta[] =[];
  sucursalesAll: Sucursal[] = [];

  ngOnInit(): void {
    this.selectedFormato = 'pdf';
    this.selectedSucursal = null;

    const today = new Date();
    this.fecha = today;
    this.selectedAnio = today.getFullYear();
    this.fechaInicial = new Date(today.getFullYear(), today.getMonth(), 1);
    this.fechaFinal = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    this.selectedMonth = new Date().getMonth() + 1;

    setTimeout(() => {
      this.api
        .doRequest('/report/listarTesoreria', {}, 'get')
        .then((res: any) => {
          this.reportesTesoreria = res;
          if (this.reportesTesoreria.length > 0) {
            this.selectedReporteTesoreria =
              this.reportesTesoreria[0].reporteName;
            this.setFormItems(this.selectedReporteTesoreria);
          }
        });

      this.api
        .doRequest('/report/reportesPorSeccion', {}, 'get')
        .then((res: any) => {
          this.reportesContabilidad = res;

          if (this.reportesContabilidad.length > 0) {
            this.selectedReporte = this.reportesContabilidad[0].reporteName;
          }
        });

      this.api
        .doRequest('/report/listarModulos', {}, 'get')
        .then((res: any) => {
          this.modulos = res;

          if (this.modulos.length > 0) {
            this.selectedModulo = this.modulos[0];
          }
        });

      this.api
        .doRequest('/report/listarSucursal', {}, 'get')
        .then((res: any) => {
          this.sucursales = res;
          this.sucursalesAll = res;

          if (this.sucursales.length > 0) {
            this.selectedSucursal = this.sucursales[0].id;
          }
        });
    }, 500);

    this.api
    .doRequest('/report/listarSucursalPV', {}, 'get')
    .then((res: any) => {
      this.puntoDeVenta = res;

      if (this.puntoDeVenta.length > 0) {
        this.selectedPuntoDeVenta = this.puntoDeVenta[0].codigoSucursal
        ;
      }
    });

    this.startYear = new Date().getFullYear() - this.yearsBefore;
    this.endYear = new Date().getFullYear() + this.yearsAfter;
    this.years = Array.from(
      { length: this.endYear - this.startYear + 1 },
      (_, index) => this.startYear + index
    ).reverse();
  }

  cargarReporte() {
    this.fechaInicialStr = this.formatDate(this.fechaInicial);
    this.fechaFinalStr = this.formatDate(this.fechaFinal);

    this.loadPdf(
      this.fechaInicialStr,
      this.fechaFinalStr,
      this.selectedReporte,
      this.selectedFormato
    );
  }

  selectedMes!: string;
  selectedFecha!: string;

  USUARIO: boolean = false;
  CODIGO_SUCURSAL: boolean = false;
  PUNTO_DE_VENTA: boolean = false;
  MODULO: boolean = false;
  MES: boolean = false;
  ANIO: boolean = false;
  FECHA_DESDE: boolean = false;
  FECHA_HASTA: boolean = false;
  FECHA: boolean = false;

  setFormItems(reportId: any) {
    this.sucursales = this.sucursalesAll;

    switch (reportId) {
      case 'rpt_invalidaciones_dte':
        this.USUARIO = true;
        this.CODIGO_SUCURSAL = true;
        this.MES = true;
        this.ANIO = true;
        this.FECHA_DESDE = false;
        this.FECHA_HASTA = false;
        this.FECHA = false;
        this.MODULO = false;
        this.PUNTO_DE_VENTA = false;
        break;

      case 'rpt_consolidado_derpatamentales':
        this.USUARIO = false;
        this.CODIGO_SUCURSAL = false;
        this.MES = true;
        this.ANIO = true;
        this.FECHA_DESDE = false;
        this.FECHA_HASTA = false;
        this.FECHA = false;
        this.MODULO = false;
        this.PUNTO_DE_VENTA = false;

        break;

      case 'rpt_departamental_diario':
        this.USUARIO = false;
        this.CODIGO_SUCURSAL = false;
        this.MES = false;
        this.ANIO = false;
        this.FECHA_DESDE = false;
        this.FECHA_HASTA = false;
        this.FECHA = true;
        this.MODULO = false;
        this.PUNTO_DE_VENTA = true;

        break;

      case 'rpt_dia_exento_gravado':
        this.USUARIO = false;
        this.CODIGO_SUCURSAL = false;
        this.MES = true;
        this.ANIO = true;
        this.FECHA_DESDE = false;
        this.FECHA_HASTA = false;
        this.FECHA = false;
        this.MODULO = true;
        this.PUNTO_DE_VENTA = false;

        break;

      case 'rpt_facturacion_delegacion':
        this.USUARIO = false;
        this.CODIGO_SUCURSAL = true;
        this.MES = false;
        this.ANIO = false;
        this.FECHA_DESDE = true;
        this.FECHA_HASTA = true;
        this.FECHA = false;
        this.MODULO = false;
        this.PUNTO_DE_VENTA = false;

        this.sucursales = this.sucursales.filter(
          (sucursal) => sucursal.nombreSucursal !== 'SAN SALVADOR'
        );
        break;

      case 'rpt_dte_sucursales':
      case 'rpt_cantidad_dte':
      case 'rpt_rango_fechas':
        this.USUARIO = false;
        this.CODIGO_SUCURSAL = false;
        this.MES = false;
        this.ANIO = false;
        this.FECHA_DESDE = true;
        this.FECHA_HASTA = true;
        this.FECHA = false;
        this.MODULO = false;
        this.PUNTO_DE_VENTA = false;

        break;

      default:
        this.USUARIO = false;
        this.CODIGO_SUCURSAL = false;
        this.MES = false;
        this.ANIO = false;
        this.FECHA_DESDE = false;
        this.FECHA_HASTA = false;
        this.FECHA = false;
        this.MODULO = false;
        this.PUNTO_DE_VENTA = false;

        break;
    }
  }

  cargarReporteTesoreria() {
    this.fechaInicialStr = this.formatDate(this.fechaInicial);
    this.fechaFinalStr = this.formatDate(this.fechaFinal);
    this.fechaStr = this.formatDate(this.fecha);

    if (
      this.selectedSucursal == null &&
      this.CODIGO_SUCURSAL &&
      this.selectedReporteTesoreria !== 'rpt_invalidaciones_dte'
    ) {
      return this.util.showSWAL(
        'Seleccionar Sucursal',
        'Debe Seleccionar una Sucursal para Continuar',
        'Aceptar',
        'error'
      );
    }

    // Construcción del body del reporte
    const getNullableValue = (condition: boolean, value: any) =>
      condition ? value : null;

if (this.selectedReporteTesoreria == 'rpt_departamental_diario') {
  this.selectedSucursal = this.selectedPuntoDeVenta;
}

    const body = {
      reporteName: this.selectedReporteTesoreria,
      formato: this.selectedFormato,
      fechaDesde: getNullableValue(this.FECHA_DESDE, this.fechaInicialStr),
      fechaHasta: getNullableValue(this.FECHA_HASTA, this.fechaFinalStr),
      usuario: null,
      sucursal: getNullableValue(
        this.CODIGO_SUCURSAL||this.PUNTO_DE_VENTA,
(this.selectedSucursal == 1? null: this.selectedSucursal )      ),
      PuntoDeVenta: getNullableValue( this.PUNTO_DE_VENTA , this.selectedPuntoDeVenta),
      mes: getNullableValue(
        this.MES,
        this.selectedMonth?? (new Date().getMonth() + 1).toString().padStart(2, '0')
      ),
      anio: getNullableValue(this.ANIO, this.selectedAnio),
      fecha: getNullableValue(this.FECHA, this.fechaStr),
      modulo: getNullableValue(this.MODULO, this.selectedModulo),
    };

    // Generar el reporte
    return this.generarReporteTes(body);
  }

  async generarReporteTes(body: any) {
    try {
      this.reportService.generarReporteBase64Tes(body).subscribe(
        (response: string) => {
          this.pdfSRCTes = 'data:application/pdf;base64,' + response;
        },
        (error) => {
          console.error('Error al descargar el reporte:', error);
        }
      );
    } catch (error) {
      console.error('Error loading PDF', error);
    }
  }

  private getFileMimeType(format: string): string {
    switch (format) {
      case 'pdf':
        return 'application/pdf';
      case 'xls':
        return 'application/vnd.ms-excel';
      case 'csv':
        return 'text/csv';
      default:
        return 'application/octet-stream';
    }
  }

  private formatDate(date: Date): string {
    const d = new Date(date);
    const month = '' + (d.getMonth() + 1);
    const day = '' + d.getDate();
    const year = d.getFullYear();

    return [year, month.padStart(2, '0'), day.padStart(2, '0')].join('-');
  }

  async loadPdfboton(
    fechaDesde: string,
    fechaHasta: string,
    reporteName: string,
    formato: string
  ) {
    try {
      const body = { fechaDesde, fechaHasta, reporteName, formato };

      this.reportService.generarReporte(body).subscribe(
        (response: Blob) => {
          const blob = new Blob([response], {
            type: this.getFileMimeType(body.formato),
          });

          const url = window.URL.createObjectURL(blob);

          const link = document.createElement('a');
          link.href = url;
          link.download = `${reporteName}.${formato}`;
          link.click();
          window.URL.revokeObjectURL(url);
        },
        (error) => {
          console.error('Error al descargar el reporte:', error);
        }
      );
    } catch (error) {
      console.error('Error loading PDF', error);
    }
  }

  async loadPdfbotonTes(body: any) {
    try {
      this.reportService.generarReporteTes(body).subscribe(
        (response: Blob) => {
          const blob = new Blob([response], {
            type: this.getFileMimeType(body.formato),
          });

          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `${body.reporteName}.${body.formato}`;
          link.click();

          window.URL.revokeObjectURL(url);
        },
        (error) => {
          console.error('Error al descargar el reporte:', error);
        }
      );
    } catch (error) {
      console.error('Error loading PDF', error);
    }
  }

  downloadReport(formato: string) {
    this.fechaInicialStr = this.formatDate(this.fechaInicial);
    this.fechaFinalStr = this.formatDate(this.fechaFinal);
    this.selectedFormato = formato;

    if (!this.selectedReporte) {
      return this.util.showSWAL(
        'Seleccionar Tipo de Reporte',
        'Debe Seleccionar un Tipo de Reporte para Continuar',
        'Aceptar',
        'error'
      );
    }

    if (!this.fechaInicial || !this.fechaFinal || !this.selectedReporte) {
      return this.util.showSWAL(
        'Faltan Datos',
        '<b>Todos los Campos Son Obligatorios </b>',
        'Aceptar',
        'error'
      );
    }

    return this.loadPdfboton(
      this.fechaInicialStr,
      this.fechaFinalStr,
      this.selectedReporte,
      this.selectedFormato
    );
  }

  downloadReportTes(formato: string) {
    this.fechaInicialStr = this.formatDate(this.fechaInicial);
    this.fechaFinalStr = this.formatDate(this.fechaFinal);
    this.selectedFormatoTes = formato;

    this.fechaInicialStr = this.formatDate(this.fechaInicial);
    this.fechaFinalStr = this.formatDate(this.fechaFinal);

    this.fechaStr = this.formatDate(this.fecha);

    const fechaInicio = this.FECHA_DESDE ? this.fechaInicialStr : null;
    const fechaFin = this.FECHA_HASTA ? this.fechaFinalStr : null;
    const Sucursal = this.CODIGO_SUCURSAL ? this.selectedSucursal : null;
    const PuntoDeVenta = this.PUNTO_DE_VENTA ? this.selectedPuntoDeVenta : null;

    const mes = this.MES
      ? (new Date().getMonth() + 1).toString().padStart(2, '0')
      : null;
    const anio = this.ANIO ? this.selectedAnio : null;
    const fecha = this.FECHA ? this.fechaStr : null;
    const reporteTesoreria = this.selectedReporteTesoreria;

    const body = {
      reporteName: reporteTesoreria,
      formato: formato,
      fechaDesde: fechaInicio,
      fechaHasta: fechaFin,
      usuario: null,
      sucursal: this.selectedReporteTesoreria == 'rpt_departamental_diario'? PuntoDeVenta : Sucursal,
      PuntoDeVenta: PuntoDeVenta,
      mes: mes,
      anio: anio,
      fecha: fecha,
    };

    return this.loadPdfbotonTes(body);
  }

  async loadPdf(
    fechaDesde: string,
    fechaHasta: string,
    reporteName: string,
    formato: string = 'pdf'
  ) {
    try {
      const body = { fechaDesde, fechaHasta, reporteName, formato };

      if (!reporteName) {
        return this.util.showSWAL(
          'Seleccionar Tipo de Reporte',
          'Debe Seleccionar un Tipo de Reporte para Continuar',
          'Aceptar',
          'error'
        );
      }

      if (!fechaDesde || !fechaHasta || !reporteName) {
        return this.util.showSWAL(
          'Faltan Datos',
          '<b>Todos los Campos Son Obligatorios </b>',
          'Aceptar',
          'error'
        );
      }

      this.reportService.generarReporteBase64(body).subscribe(
        (response: string) => {
          this.pdfSRC = 'data:application/pdf;base64,' + response;
        },
        (error) => {
          console.error('Error al descargar el reporte:', error);
        }
      );
    } catch (error) {
      console.error('Error loading PDF', error);
    }
  }

  selectedMonth: any = new Date().getMonth() + 1;
  months: { id: string; name: string }[] = [
    { id: '01', name: 'Enero' },
    { id: '02', name: 'Febrero' },
    { id: '03', name: 'Marzo' },
    { id: '04', name: 'Abril' },
    { id: '05', name: 'Mayo' },
    { id: '06', name: 'Junio' },
    { id: '07', name: 'Julio' },
    { id: '08', name: 'Agosto' },
    { id: '09', name: 'Septiembre' },
    { id: '10', name: 'Octubre' },
    { id: '11', name: 'Noviembre' },
    { id: '12', name: 'Diciembre' },
  ];

  onMonthSelectionChange(): void {
    this.sharedDataService.setVariable('Mes', this.selectedMonth);
  }

  setCurrentMonth = (o1: any, o2: any): boolean => {
    this.sharedDataService.setVariable('Mes', this.selectedMonth);
    return parseInt(o1) === this.selectedMonth;
  };

  selectedAnio: any;
  startYear: number = 0;
  endYear: number = 0;
  years: number[] = [];
  selectedYear!: number;

  onYearSelectionChange(): void {
    if (this.selectedAnio === '') {
      this.selectedAnio = new Date().getFullYear().toString();
    }
    this.sharedDataService.setVariable('Año', this.selectedAnio);
  }

  adjustZoom(isZoomIn: boolean, option: number = 1) {
    const zoomMap: { [key: number]: 'zoom' | 'zoom2' } = {
      1: 'zoom',
      2: 'zoom2',
    };

    const zoomKey = zoomMap[option];
    if (!zoomKey) return;

    const zoomValue = this[zoomKey];
    const adjustment = isZoomIn ? 0.1 : -0.1;

    if (isZoomIn || (!isZoomIn && zoomValue > 0.2)) {
      this[zoomKey] = zoomValue + adjustment;
    }
  }

  zoomIn(option: number = 1) {
    this.adjustZoom(true, option);
  }

  zoomOut(option: number = 1) {
    this.adjustZoom(false, option);
  }

  downloadPDF(name: string = 'reporte contabilidad.pdf', option: number = 1) {
    const link = document.createElement('a');
    link.href = option == 1 ? this.pdfSRC : this.pdfSRCTes;
    link.download = name;
    link.click();
  }
}
