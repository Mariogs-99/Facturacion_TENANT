import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { AuthService } from '../services/auth.service';
import { UtilsService } from '../services/utils.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private clicked: boolean = false;

  constructor(private utils: UtilsService, private auth: AuthService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    Swal.close();

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (!this.clicked) {
          if (error.status === 401 && !req.url.includes('api/auth/authenticate')) {
            if (!this.clicked) {
              this.handleSessionExpiredError();
            }
          } else if (error.status === 0) {
            this.handleServerError(
              'No se pudo establecer comunicación con el servidor para realizar la petición.'
            );
          } else {
            this.handleServerError(
              error.error.message ?? 'No se pudo procesar petición.'
            );
          }
        }

        return throwError(error);
      })
    );
  }
  private handleSessionExpiredError(): void {
    this.clicked = true;
    Swal.close();

    setTimeout(() => {
      Swal.fire({
        title: '¡Tu sesión ha expirado!',
        text: 'Por favor, haz clic en Aceptar para recargar la página.',
        icon: 'warning',
        allowOutsideClick: false,
        confirmButtonText: 'Aceptar',
      })
        .then((result) => {
          if (result.isConfirmed) {
            this.clicked = false;
            this.utils.dismissloading();
            this.auth.LogOut();
            location.reload();
          }
        })
        .catch((error: any) => {
          console.error('error', error);
        });
    }, 200);
  }

  private handleServerError(message: string): void {
    Swal.fire({
      title: 'Error',
      text: message,
      icon: 'error',
      showCancelButton: false,
      confirmButtonText: 'Ok',
      showCloseButton: true,
      allowOutsideClick: false,
    });
  }
}
