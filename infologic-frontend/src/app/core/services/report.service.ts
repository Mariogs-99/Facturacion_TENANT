import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { ApiService } from './api.service';
import { environment } from '../environments/environment';

export interface ReporteTesoreriaDTO {
  reporteName: string;
  formato: string;
  fechaDesde: string;
  fechaHasta: string;
  usuario: string;
  sucursal: string;
  mes: string;
  anio: string;
  fecha: string;
}
@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private apiUrlBack = environment.apiServerUrl + 'api/v1';

  constructor(
    private http: HttpClient,
    private auth: AuthService,
    private api: ApiService
  ) {}

  generarReporte(reporteDTO: any): Observable<Blob> {
    let token = this.auth.getToken() != null ? this.auth.getToken() : '';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token,
    });
    return this.http
      .post<Blob>(this.apiUrlBack + '/report/generar', reporteDTO, {
        headers: headers,
        responseType: 'blob' as 'json',
      })
      .pipe(catchError(this.handleError));
  }

  generarReporteBase64(reporteDTO: any): Observable<any> {
    let token = this.auth.getToken() != null ? this.auth.getToken() : '';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token,
    });

    return this.http
      .post(this.apiUrlBack + '/report/generar64', reporteDTO, {
        headers,
        responseType: 'text',
      })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    console.error('Ocurrió un error', error);
    throw new Error('Algo salió mal; por favor, intenta nuevamente más tarde.');
  }

  private baseUrl = this.apiUrlBack + '/report/generarPreview';
  generarReporteBase64Tes(reporteDTO: any): Observable<any> {
    let token = this.auth.getToken() != null ? this.auth.getToken() : '';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token,
    });

    return this.http
      .post(this.baseUrl, reporteDTO, { headers, responseType: 'text' })
      .pipe(catchError(this.handleError));
  }

  generarTesoreria(
    reporteTesoreriaDTO: ReporteTesoreriaDTO
  ): Observable<HttpResponse<Blob>> {
    const url = this.apiUrlBack + `/report/generarTesoreria`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Accept: 'application/pdf',
    });

    return this.http.post<Blob>(url, reporteTesoreriaDTO, {
      headers,
      responseType: 'blob' as 'json',
      observe: 'response',
    });
  }

  generarReporteTes(reporteDTO: any): Observable<Blob> {
    let token = this.auth.getToken() != null ? this.auth.getToken() : '';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token,
    });
    return this.http
      .post<Blob>(this.apiUrlBack + '/report/generarTesoreria', reporteDTO, {
        headers: headers,
        responseType: 'blob' as 'json',
      })
      .pipe(catchError(this.handleError));
  }
}
