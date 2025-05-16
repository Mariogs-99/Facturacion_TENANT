import { AfterViewInit, Component, Inject } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  FormBuilder,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { PasswordValidatorService } from 'src/app/core/services/password-validator.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
})
export class ResetPasswordComponent implements AfterViewInit {
  public resetForm: FormGroup;
  public hide: boolean = true;
  public hideNew: boolean = true;
  public hideReNew: boolean = true;
  public userName: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private util: UtilsService,
    private api: ApiService,
    private auth: AuthService,
    public dialogRef: MatDialogRef<ResetPasswordComponent>,
    private resetPwd: PasswordValidatorService,
    @Inject(MAT_DIALOG_DATA) public dialogData: any
  ) {
    this.resetForm = this.formBuilder.group(
      {
        usuario: new FormControl({ value: '', disabled: false }, [
          Validators.required,
        ]),
        clave: new FormControl('', [Validators.required]),
        nuevaClave: new FormControl('', [
          Validators.required,
          Validators.minLength(8),
          this.hasLowerCaseValidator,
          this.hasUpperCaseValidator,
          this.hasNumberValidator,
          this.noSpaceValidator,
        ]),
        claveConfirmada: new FormControl('', [Validators.required]),
      },
      {
        validators: this.resetPwd.passwordMatchValidator(
          'nuevaClave',
          'claveConfirmada'
        ),
      }
    );
  }

  ngAfterViewInit(): void {
    if (this.dialogData.data.from == 'root') {
      return this.resetForm.controls['usuario'].enable();
    }
    this.userName = this.getUserName(this.dialogData, this.auth);

    this.resetForm.controls['usuario'].setValue(this.userName);
  }

  getUserName(dialogData: any, auth: any): string {
    if (dialogData.user !== undefined) {
      return dialogData.user;
    } else {
      const localUsername = auth.getLocalData('cnr-info', 'username');
      return localUsername || '';
    }
  }

  reset() {
    let userData = {
      usuario:
        this.userName != ''
          ? this.userName
          : this.resetForm.controls['usuario'].value,
      clave: this.resetForm.controls['clave'].value,
      nuevaClave: this.resetForm.controls['nuevaClave'].value,
      claveConfirmada: this.resetForm.controls['claveConfirmada'].value,
    };
    this.util
      .SWALYESNO('Se cambiara tu contraseña', 'Este proceso es irreversible')
      .then((resp) => {
        if (resp) {
          if (this.dialogData.data.log == 'no-log') {
            return this.api
              .login(userData, 'cambiar-sisucc')
              .then((resp: any) => {
                this.util
                  .showSWAL(
                    'Su contraseña ha sido cambiado',
                    '',
                    'Aceptar',
                    'success'
                  )
                  .then(() => {
                    return this.dialogRef.close();
                  });
              })
              .catch((error: any) => {
                return this.util.showSWAL(
                  'Ha ocurrido un problema',
                  error.error.data.error ?? error.error.data,
                  'Aceptar',
                  'error'
                );
              });
          }

          return this.api
            .doRequest('/users/cambiar-sisucc', userData, 'post')
            .then((resp: JsonResponse) => {
              this.util
                .showSWAL(
                  'Su contraseña ha sido cambiado',
                  '',
                  'Aceptar',
                  'success'
                )
                .then(() => {
                  return this.dialogRef.close();
                });
            })
            .catch((error: any) => {
              return this.util.showSWAL(
                'Ha ocurrido un problema',
                error.error.data.error ?? error.error.data,
                'Aceptar',
                'error'
              );
            });
        }

        return this.dialogRef.close();
      });
  }

  disableEnter($event: Event) {
    $event.preventDefault();
    $event.stopPropagation();
  }

  hasLowerCaseValidator(control: FormControl): { [key: string]: any } | null {
    const hasLowerCase = /[a-z]/.test(control.value);
    return hasLowerCase ? null : { lowercase: true };
  }

  hasUpperCaseValidator(control: FormControl): { [key: string]: any } | null {
    const hasUpperCase = /[A-Z]/.test(control.value);
    return hasUpperCase ? null : { uppercase: true };
  }

  hasNumberValidator(control: FormControl): { [key: string]: any } | null {
    const hasNumber = /\d/.test(control.value);
    return hasNumber ? null : { number: true };
  }

  noSpaceValidator(control: FormControl): { [key: string]: any } | null {
    const hasSpace = /\s/.test(control.value);
    return hasSpace ? { space: true } : null;
  }

  passwordsMatchValidator(group: FormGroup): { [key: string]: any } | null {
    const password = group.controls['nuevaClave'].value;
    const confirmPassword = group.controls['claveConfirmada'].value;
    return password === confirmPassword ? null : { mismatch: true };
  }

  get nuevaClave() {
    return this.resetForm.get('nuevaClave');
  }

  get claveConfirmada() {
    return this.resetForm.get('claveConfirmada');
  }

  getErrorMessage(controlName: string): string {
    const control = this.resetForm.controls[controlName];
    if (control.hasError('required')) {
      return 'Este campo es requerido';
    } else if (control.hasError('minlength')) {
      return 'La clave debe tener al menos 8 caracteres';
    } else if (control.hasError('lowercase')) {
      return 'La clave debe tener letras minúsculas';
    } else if (control.hasError('uppercase')) {
      return 'La clave debe tener letras mayúsculas';
    } else if (control.hasError('number')) {
      return 'La clave debe tener números';
    } else if (control.hasError('space')) {
      return 'La clave no puede tener espacios en blanco';
    }
    return '';
  }
}
