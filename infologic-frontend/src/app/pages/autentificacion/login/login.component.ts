import { Component } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ResetPasswordComponent } from '../reset-password/reset-password.component';
import { ManageUserComponent } from '../../user/dialogs/manage-user/manage-user.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  public loginForm: FormGroup;
  public hide: boolean = true;
  constructor(
    private formBuilder: FormBuilder,
    private auth: AuthService,
    private api: ApiService,
    private util: UtilsService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      usuario: new FormControl('', [Validators.required]),
      clave: new FormControl('', [Validators.required]),
    });
  }

  disableEnter($event: Event) {
    $event.preventDefault();
    $event.stopPropagation();
  }

  login() {
    this.api

      .login(this.loginForm.value, 'authenticate')
      .then((res: JsonResponse) => {
        if (res.code != 200) {
          return this.util.showSWAL(
            'Acceso Inválido',
            res.data.error ?? res.data,
            'Aceptar',
            'error'
          );
        }

        localStorage.setItem('cnr-info', JSON.stringify(res.data));
        localStorage.setItem('menu', JSON.stringify(res.data.menu));
        this.auth.token = res.data.token;
        this.auth.userName = res.data.username;

        return this.router.navigateByUrl('pages').then(() => {
          if (res.data.reset_password) {
            this.util
              .SWALYESNO(
                'Debes cambiar tu contraseña!',
                'Quieres cambiarla ahora?',
                'Cambiar ahora',
                'Recuerdame más tarde'
              )
              .then((e) => {
                if (e) {
                  this.changePassword(res.data.user);
                }
              });
          }

          if (
            res.data.requerido_facturacion &&
            Object.keys(res.data.requerido_facturacion).length > 0
          ) {
            this.util
              .SWALYESNO(
                'Debes actualizar tu perfil!',
                'Quieres actualizar ahora?',
                'Actualizar ahora',
                'Recuerdame más tarde'
              )
              .then((e) => {
                if (e) {
                  this.util.openModal({
                    title: 'Actualización de Usuario',
                    botonAceptar: 'Actualizar',
                    componentToLoad: ManageUserComponent,
                    callerComponent: this,
                    footer: false,
                    data: {
                      user: {
                        idUser: 0,
                        usuario: res.data.username,
                      },
                      requerido: res.data.requerido_facturacion,
                      origin: 'login',
                    },
                  });
                }
              });
          }
        });
      })
      .catch((error: any) => {
        const errorData = error.error.data;
        const isCode4 = errorData?.codigo == 4 || errorData?.error?.codigo == 4;

        const mensaje = isCode4
          ? 'Debes cambiar tu contraseña para continuar'
          : errorData?.mensaje ||
            errorData?.error?.mensaje ||
            'Error desconocido';

        if (error.error.code == 401) {
          return this.util.showSWAL(
            'Acceso Inválido',
            errorData,
            'Aceptar',
            'error'
          );
        }

        return this.util
          .showSWAL('Ha ocurrido un problema', mensaje, 'Aceptar', 'error')
          .then((action: any) => {
            if (isCode4) {
              this.changePassword();
            }
          });
      })
      .finally(() => {
        this.router.navigate(['pages']);
      });
  }

  changePassword(user: any = null) {
    this.util.openModal(
      {
        title: 'Actualizacion de contraseña',
        botonAceptar: 'Aceptar',
        componentToLoad: ResetPasswordComponent,
        callerComponent: this,
        data: {
          user: user,
          from: 'root',
          log: 'no-log',
        },
        footer: false,
      },
      '40%',
      '50%',
      '100%',
      '100%'
    );
  }
}
