import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
  HttpHeaders,
} from '@angular/common/http';
import { DataService } from './data.service';
import { AuthService } from './auth.service';
import { ProviderService } from './provider.service';

import { catchError, throwError } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { environment } from '../environments/environment';
import { JsonResponse } from '../model/JsonResponse.model';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private api: string = '';
  private authUri: string = '';
  minDescontar: any;
  idhabdescId: any;
  id: any;
  montoDescontar!: number;
  router: any;

  constructor(
    private http: HttpClient,
    private data: DataService,
    private auth: AuthService,
    public provider: ProviderService,
    public dialog: MatDialog
  ) {
    this.api = this.api + environment.apiServerUrl + 'api/v1';
    this.authUri = this.authUri + environment.apiServerUrl + 'api/auth/';
  }

  login(data: any, authUrl: string = 'authenticate'): Promise<JsonResponse> {
    return new Promise((resolve, reject) => {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
      });

      this.data.subscription = this.http
        .post<any>(this.authUri + authUrl, data, { headers })
        .subscribe(
          (response) => {
            return resolve(response);
          },
          (error) => {
            return reject(new Error(error));
          }
        );
    });
  }

  logout(authUrl: string = 'logout'): Promise<JsonResponse> {
    let token = this.auth.getToken() != null ? this.auth.getToken() : '';
    return new Promise((resolve) => {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
      });

      this.data.subscription = this.http
        .post<any>(
          this.authUri + authUrl,
          {
            token: 'Bearer ' + token,
          },
          { headers }
        )
        .subscribe(
          (response) => {
            return resolve(response);
          },
          (error) => {
            return resolve(error);
          }
        );
    });
  }

  doRequest(url: string, data: any, type: string): Promise<JsonResponse> {
    this.data.sendMessage({ from: 'api', status: true });

    return new Promise((resolve, reject) => {
      let token = this.auth.getToken() != null ? this.auth.getToken() : '';
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token,
      });
      try {
        switch (type) {
          case 'post':
            this.data.subscription = this.http
              .post<any>(this.api + url, data, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  return reject(new Error(JSON.stringify(error)));
                }
              );
            break;
          case 'get':
            this.data.subscription = this.http
              .get<any>(this.api + url, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  return reject(new Error(JSON.stringify(error)));
                }
              );
            break;
          case 'delete':
            this.data.subscription = this.http
              .delete<any>(this.api + url, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  const errors: Error = new Error("Something went wrong"); //!Error modificado
                  return reject(errors);
                }
              );
            break;
          case 'put':
            this.data.subscription = this.http
              .put<any>(this.api + url, data, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  const errors: Error = new Error("Something went wrong"); // ✅ Ahora sí es un Error
                  return reject(errors);
                }
              );
            break;
        }
      } catch (error) {
        console.error('error : ', error);
      }
    });
  }
  doRequest2(url: string, data: any, type: string): Promise<JsonResponse> {
    this.data.sendMessage({ from: 'api', status: true });

    return new Promise((resolve, reject) => {
      let token = this.auth.getToken() != null ? this.auth.getToken() : '';
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token,
      });
      try {
        switch (type) {
          case 'post':
            this.data.subscription = this.http
              .post<any>(url, data, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  const error2: Error = new Error("Something went wrong"); // ✅ Ahora sí es un Error
                  return reject(error2);
                }
              );
            break;
          case 'get':
            this.data.subscription = this.http
              .get<any>(url, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  const error2: Error = new Error("Something went wrong"); // ✅ Ahora sí es un Error
                  return reject(error2);
                }
              );
            break;
          case 'delete':
            this.data.subscription = this.http
              .delete<any>(url, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  const error2: Error = new Error("Something went wrong");
                  return reject(error2);
                }
              );
            break;
          case 'put':
            this.data.subscription = this.http
              .put<any>(url, data, { headers })
              .subscribe(
                (response) => {
                  return resolve(this.handleResponse(response));
                },
                (error: HttpErrorResponse) => {
                  this.handleerror(error);
                  const error2: Error = new Error("Something went wrong"); // ✅ Ahora sí es un Error
                  return reject(error2);
                }
              );
            break;
        }
      } catch (error) {
        console.error('error : ', error);
      }
    });
  }

  getPage(
    url: string,
    search?: string,
    sortBy?: string,
    page?: number,
    size?: number
  ): Promise<JsonResponse> {
    let params = new HttpParams();
    if (search) {
      params = params.append('filterBy', '' + search);
    }
    if (sortBy) {
      params = params.append('sortBy', '' + sortBy);
    }
    if (page) {
      params = params.append('page', '' + page);
    }
    if (size) {
      params = params.append('size', '' + size);
    }
    const headers = new HttpHeaders().set(
      'Authorization',
      `Bearer ${this.auth.getLocalData('cnr-info', 'token')}`
    );

    return new Promise((resolve) => {
      this.http
        .get<any>(this.api + url, {
          observe: 'response',
          params: params,
          headers: headers,
        })
        .pipe(
          catchError((error: HttpErrorResponse) => {
            return throwError(error);
          })
        )
        .subscribe(
          (respose) => {
            return resolve(this.handleResponse(respose.body));
          },
          (error) => {
            return resolve(this.handleerror(error));
          }
        );
    });
  }

  putRecord(url: string, body?: any) {
    const headers = new HttpHeaders().set(
      'Authorization',
      `Bearer ${this.auth.getLocalData('cnr-info', 'token')}`
    );
    return this.http.put<any>(this.api + url, body, {
      observe: 'response',
      headers: headers,
    });
  }
  postRecord(url: string, body?: any) {
    const headers = new HttpHeaders().set(
      'Authorization',
      `Bearer ${this.auth.getLocalData('cnr-info', 'token')}`
    );
    return this.http.post<any>(this.api + url, body, {
      observe: 'response',
      headers: headers,
    });
  }

  searchRecord(url: string, id: number) {
    const headers = new HttpHeaders().set(
      'Authorization',
      `Bearer ${this.auth.getLocalData('cnr-info', 'token')}`
    );
    return this.http.get<any>(this.api + url + '/' + id, {
      observe: 'response',
      headers: headers,
    });
  }
  deleteRecord(url: string, id: number) {
    const headers = new HttpHeaders().set(
      'Authorization',
      `Bearer ${this.auth.getLocalData('cnr-info', 'token')}`
    );
    return this.http.delete<any>(this.api + url + '/' + id, {
      observe: 'response',
      headers: headers,
    });
  }

  handleResponse(res: any) {
    this.data.sendMessage({ from: 'api', status: false });
    return res;
  }

  handleerror(res: any) {
    this.data.sendMessage({ from: 'api', status: false });
    if (res.status == 500) {
      return res.error;
    }
  }

  getPdfBase64(url: string): Promise<string> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${this.auth.getToken()}`,
    });

    return this.http
      .get(url, { headers, responseType: 'text' })
      .pipe(catchError(this.handleError))
      .toPromise()
      .then((response: string | undefined) => response ?? '');
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError('Something bad happened; please try again later.');
  }

  descargarDocumento(url: string, docName: string, body?: any): Promise<any> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.auth.getToken()}`,
    });

    if (body != undefined) {
      return this.http
        .post(this.api + url, body, { headers, responseType: 'blob' as 'json' })
        .toPromise()
        .then((blob: any) => {
          saveAs(blob, docName);

          return true;
        })
        .catch((error) => {
          console.error(
            'ApiService POST => Error descargando el archivo PDF',
            error
          );

          return false;
        });
    }

    return this.http
      .get(this.api + url, { headers, responseType: 'blob' as 'json' })
      .toPromise()
      .then((blob: any) => {
        saveAs(blob, docName);

        return true;
      })
      .catch((error) => {
        console.error(
          'ApiService  GET => Error descargando el archivo PDF',
          error
        );

        return false;
      });
  }
}
