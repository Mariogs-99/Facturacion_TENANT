import { Component } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent {
  public recoverForm: FormGroup;
  public hide: boolean = true;
  constructor(
    private formBuilder: FormBuilder,
    private api: ApiService,
    private util: UtilsService,
    public router: Router
  ) {
    this.recoverForm = this.formBuilder.group({
      usuario: new FormControl('', [Validators.required]),
    });
  }

  disableEnter($event: Event) {
    $event.preventDefault();
    $event.stopPropagation();
  }

  recuperar() {
    /** Prueba con login para reset de password */

    this.api
      .login(this.recoverForm.value, 'recuperar-sisucc')
      .then((res: JsonResponse) => {
        this.util
          .showSWAL(
            'Correo enviado',
            'para completar este proceso debes revisar tu correo',
            'Aceptar',
            'success'
          )
          .then(() => {
            this.router.navigateByUrl('login');
          });
      })
      .catch((error: any) => {
        console.error('error autentificacion => ', error);

        return this.util.showSWAL(
          'Ha ocurrido un problema',
          error.error.data.error ?? error.error.data,
          'Aceptar',
          'error'
        );
      });
  }
}
