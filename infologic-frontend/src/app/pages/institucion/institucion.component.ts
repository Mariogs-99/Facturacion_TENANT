import { Component, ViewChild } from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';
import { AuthService } from 'src/app/core/services/auth.service';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { MatDialog } from '@angular/material/dialog';
import { DataService } from 'src/app/core/services/data.service';

import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { Subscription } from 'rxjs';
import { CompaniesModel } from '../companies/companies.model';
import { ValidacionesService } from 'src/app/core/services/validaciones.service';

@Component({
  selector: 'app-institucion',
  templateUrl: './institucion.component.html',
  styleUrls: ['./institucion.component.css'],
})
export class InstitucionComponent {
  @ViewChild(MatAccordion)
  accordion!: MatAccordion;

  public institucionForm: FormGroup;
  public permissions: any;
  subscribe: Subscription;
  disable: boolean = true;
  public permissionsArr: string[] = [];

  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public utils: UtilsService,
    private formBuider: FormBuilder,
    private util: UtilsService,
    private _auth: AuthService,
    private sharedDataService: SharedDataService,
    private _data: DataService,
    public validaciones: ValidacionesService
  ) {
    this.permissionsArr = this._auth.getPermissions();

    this.accordion = new MatAccordion();

    this.institucionForm = this.formBuider.group({
      file: new FormControl(''),
      nameCompany: new FormControl('', [Validators.required]),
      nombreGiroCompany: new FormControl('', [Validators.required]),
      nombreRegimenCompany: new FormControl(''),
      nombreRecintoCompany: new FormControl(''),
      nombreMunicipioCompany: new FormControl('', [Validators.required]),
      socialReasonCompany: new FormControl('', [Validators.required]),
      descriptionCompany: new FormControl('', [Validators.required]),
      nit: new FormControl('', [Validators.required]),
      nrc: new FormControl('', [Validators.required]),
      addressCompany: new FormControl('', [Validators.required]),
      phoneCompany: new FormControl('', [Validators.required]),
      emailCompany: new FormControl('', [
        Validators.email,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/),
      ]),
      idDeptoCompany: new FormControl(''),
      idMuniCompany: new FormControl('', [Validators.required]),
      idGiroCompany: new FormControl('', [Validators.required]),
      idRecinto: new FormControl(''),
      idRegimen: new FormControl(''),
      idCompany: new FormControl(''),
      mhUser: new FormControl('', [Validators.required]),
      mhPass: new FormControl('', [Validators.required]),
      clavePrimariaCert: new FormControl('', [Validators.required]),
      active: new FormControl(''),
    });

    this.subscribe = this._data.getData().subscribe((resp) => {
      if (resp.component == 'InstitucionComponent' && resp.action == 'open') {
        this.disable = !this.disable;
        if (this.disable) {
          this.isupdating = false;
        } else {
          this.isupdating = true;
        }
      }
    });
  }

  ngOnInit(): void {
    this.inicializartabla();

    this.institucionForm
      .get('idMuniCompany')
      ?.valueChanges.subscribe((muni) => {
        if (muni) {
          let val: any = this.municipios.filter((item) => {
            return item.id == muni;
          })[0];

          this.institucionForm.controls['nombreMunicipioCompany'].setValue(
            val.name as string
          );
          this.companies.nombreMunicipioCompany = val.name;
        }
      });

    this.institucionForm
      .get('idGiroCompany')
      ?.valueChanges.subscribe((giro) => {
        if (giro) {
          let val: any = this.giros.filter((item) => {
            return item.id == giro;
          })[0];

          this.institucionForm.controls['nombreGiroCompany'].setValue(
            val.name as string
          );
          this.companies.nombreGiroCompany = val.name;
        }
      });
    this.institucionForm.get('idRecinto')?.valueChanges.subscribe((recinto) => {
      if (recinto) {
        let val: any = this.recinto.filter((item) => {
          return item.id == recinto;
        })[0];

        this.institucionForm.controls['nombreRecintoCompany'].setValue(
          val.name as string
        );
        this.companies.nombreRecintoCompany = val.name;
      }
    });
    this.institucionForm.get('idRegimen')?.valueChanges.subscribe((regimen) => {
      if (regimen) {
        let val: any = this.regimen.filter((item) => {
          return item.id == regimen;
        })[0];

        this.institucionForm.controls['nombreRegimenCompany'].setValue(
          val.name as string
        );
        this.companies.nombreRegimenCompany = val.name;
      }
    });
  }

  companies = new CompaniesModel();
  isupdating: boolean = false;
  inputChange: any = '';

  filteredOptions: Array<any> = [];
  filteredOptionsGiros: Array<any> = [];
  filteredOptionsRegimen: Array<any> = [];
  filteredOptionsRecinto: Array<any> = [];

  municipios: Array<any> = [];
  giros: Array<any> = [];
  regimen: Array<any> = [];
  recinto: Array<any> = [];

  inicializartabla() {
    let idCompany: any = '1';

    this.api
      .doRequest('/company/find/' + idCompany, this.companies, 'get')
      .then((res: any) => {
        if (res.code === 200) {
          this.companies = res.data.company;

          this.municipios = res.data.catalogos.municipios;
          this.sharedDataService.setVariable('catalogos', res.data.catalogos);
          this.giros = res.data.catalogos.giros;
          this.recinto = res.data.catalogos.recintos;
          this.regimen = res.data.catalogos.regimens;
        }
        this.fillData(this.companies);
      });
  }

  mascara: string = '99999999-9';
  maskDUINIT(event: any) {
    if (event) {
      if (event.length > 9) {
        this.mascara = '9999-999999-999-9';
      }

      if (event.length < 8) {
        this.mascara = '99999999-9';
      }
    }
  }

  fillData(element: any) {
    for (const atr in this.companies) {
      try {
        this.institucionForm.controls[atr].setValue(element[atr]);
      } catch (e) {}
    }
  }
  filtervaluesInstitucion(event: any = '', opt: any = 1) {
    if (event === '') {
      this.filteredOptions = this.municipios;
      this.filteredOptionsGiros = this.giros;
      this.filteredOptionsRegimen = this.regimen;
      this.filteredOptionsRecinto = this.recinto;
    } else {
      switch (opt) {
        case 1:
          this.filteredOptions = this.municipios;
          let value = this.municipios.filter((item) => {
            if (item.name.toLowerCase().includes(event.toLowerCase())) {
              return item;
            }
          });
          this.filteredOptions = value;

          break;
        case 2:
          this.filteredOptionsGiros = this.giros;
          let value2 = this.giros.filter((item) => {
            if (item.name.toLowerCase().includes(event.toLowerCase())) {
              return item;
            }
          });
          this.filteredOptionsGiros = value2;
          break;
        case 3:
          this.filteredOptionsRegimen = this.regimen;
          let value3 = this.regimen.filter((item) => {
            if (item.name.toLowerCase().includes(event.toLowerCase())) {
              return item;
            }
          });
          this.filteredOptionsRegimen = value3;
          break;
        case 4:
          this.filteredOptionsRecinto = this.recinto;
          let value4 = this.recinto.filter((item) => {
            if (item.name.toLowerCase().includes(event.toLowerCase())) {
              return item;
            }
          });
          this.filteredOptionsRecinto = value4;
          break;
      }
    }
  }

  selectChangeInstitucion(event: any, opt = 1) {
    let id = event.option.value;
    switch (opt) {
      case 1:
        let val2: any = this.municipios.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];
        this.institucionForm.get('idMuniCompany')?.setValue(val2.id);
        this.institucionForm.get('nombreMunicipioCompany')?.setValue(val2.name);
        this.institucionForm
          .get('idDeptoCompany')
          ?.setValue(val2.idDeptoCompany);
        break;

      case 2:
        let val: any = this.giros.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];
        this.institucionForm.get('idGiroCompany')?.setValue(val.id);
        this.institucionForm.get('nombreGiroCompany')?.setValue(val.name);
        break;
      case 3:
        let val3: any = this.recinto.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];
        this.institucionForm.get('idRecinto')?.setValue(val3.id);
        this.institucionForm.get('nombreRecintoCompany')?.setValue(val3.name);
        break;
      case 4:
        let val4: any = this.regimen.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];

        this.institucionForm.get('idRegimen')?.setValue(val4.id);
        this.institucionForm.get('nombreRegimenCompany')?.setValue(val4.name);
        break;
    }
  }

  resetOptions() {
    this.filtervaluesInstitucion('');
    this.filtervaluesInstitucion('', 1);
    this.filtervaluesInstitucion('', 2);
    this.filtervaluesInstitucion('', 3);
    this.filtervaluesInstitucion('', 4);
  }

  toLower(event: any) {
    event.target.value = event.target.value.toLowerCase();
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^@.A-Za-z0-9_]/g, '').toLowerCase();
    input.value = value;
    this.institucionForm.get('emailCompany')?.setValue(value);
  }

  closeModal() {
    this.institucionForm.reset();
    this.dialog.closeAll();
  }

  getRequiredFields(): string {
    const fielddescriptionCompanys: { [key: string]: string } = {
      nameCompany: 'Nombre',
      nombreGiroCompany: 'Giro Actividad Económica',
      nombreMunicipioCompany: 'Municipio',
      socialReasonCompany: 'Nombre Comercial',
      descriptionCompany: 'Descripcion',
      nit: 'NIT',
      nrc: 'NRC',
      addressCompany: 'Direccion',
      phoneCompany: 'Telefono',
      emailCompany: 'Correo Electronico',
      mhUser: 'Usuario minesterio de hacienda',
      mhPass: 'Contraseña minesterio de hacienda',
      clavePrimariaCert: 'Clave primaria certificado',
    };

    const requiredFields: string[] = [];
    Object.keys(this.institucionForm.controls).forEach((key) => {
      const control = this.institucionForm.get(key);
      if (control?.errors?.['required']) {
        requiredFields.push(fielddescriptionCompanys[key] || key);
      }
    });
    return `Completa los campos requeridos: ${requiredFields.join(', ')}`;
  }

  validarCampoModalCompany(
    formControlName: string,
    titulo: string,
    mensaje: string
  ): boolean {
    const valor = this.institucionForm.get(formControlName)?.value;
    if (!valor) {
      this.utils.showSWAL(titulo, mensaje, 'OK', 'warning');
      return false;
    }
    return true;
  }

  private validarYLimpiarCampoModalCompany(
    formControlName: string,
    campoAResetear: string,
    titulo: string,
    mensaje: string,
    condicionExtra: boolean = true
  ): boolean {
    const valor = this.institucionForm.get(formControlName)?.value;
    if (!valor && condicionExtra) {
      this.institucionForm.get(campoAResetear)?.setValue('');
      this.util.showSWAL(titulo, mensaje, 'OK', 'warning');
      return false;
    }
    return true;
  }

  guardar() {
    const validationErrors: string[] = [];

    const validations = [
      {
        id: 'idGiroCompany',
        name: 'nombreGiroCompany',
        errorTitle: 'Actividad Económica Incorrecta',
        errorMessage: 'Debe Escribir una actividad Económica válida',
      },
      {
        id: 'idMuniCompany',
        name: 'nombreMunicipioCompany',
        errorTitle: 'Municipio Incorrecto',
        errorMessage: 'Debe Escribir un municipio correcto',
      },
      {
        id: 'idRecinto',
        name: 'nombreRecintoCompany',
        errorTitle: 'Recinto fiscal incorrecto',
        errorMessage: 'Debe Escribir un recinto fiscal válido',
        condition: !!this.institucionForm.get('nombreRecintoCompany')?.value,
      },
      {
        id: 'idRegimen',
        name: 'nombreRegimenCompany',
        errorTitle: 'Regimen incorrecto',
        errorMessage: 'Debe Escribir un régimen válido',
        condition: !!this.institucionForm.get('nombreRegimenCompany')?.value,
      },
    ];

    validations.forEach((validation) => {
      if (
        !this.validarYLimpiarCampoModalCompany(
          validation.id,
          validation.name,
          validation.errorTitle,
          validation.errorMessage,
          validation.condition
        )
      ) {
        validationErrors.push(validation.errorTitle);
      }
    });

    this.setCompanies();

    const fieldValidations = [
      {
        id: 'descriptionCompany',
        errorTitle: 'Ingrese la Descripción de la Compañia',
        errorMessage: 'Debe Escribir una descripción válida',
      },
      {
        id: 'idMuniCompany',
        errorTitle: 'Municipio Incorrecto',
        errorMessage: 'Debe Seleccionar un municipio válido',
      },
      {
        id: 'idGiroCompany',
        errorTitle: 'Actividad Económica Incorrecta',
        errorMessage: 'Debe Escribir una actividad Económica válida',
      },
      {
        id: 'mhUser',
        errorTitle: 'Usuario del Ministerio de Hacienda Incorrecto',
        errorMessage: 'Debe Escribir un usuario MH válido',
      },
      {
        id: 'mhPass',
        errorTitle: 'Contraseña del Ministerio de Hacienda Incorrecta',
        errorMessage: 'Debe Escribir una Contraseña MH válida',
      },
      {
        id: 'clavePrimariaCert',
        errorTitle:
          'Clave del Certificado del Ministerio de Hacienda Incorrecta',
        errorMessage: 'Debe Escribir una clave del Certificado válida',
      },
      {
        id: 'phoneCompany',
        errorTitle: 'Teléfono de la Empresa Incorrecto',
        errorMessage: 'Debe Escribir un teléfono válido',
      },
    ];

    fieldValidations.forEach((fieldValidation) => {
      if (
        !this.validarCampoModalCompany(
          fieldValidation.id,
          fieldValidation.errorTitle,
          fieldValidation.errorMessage
        )
      ) {
        validationErrors.push(fieldValidation.errorTitle);
      }
    });

    if (this.institucionForm.get('emailCompany')?.invalid) {
      validationErrors.push(
        'Correo de la Empresa Incorrecto: Debe Escribir un correo válido'
      );
    }

    if (this.institucionForm.invalid) {
      validationErrors.push(
        'Formulario Incompleto: Debe llenar todos los campos Requeridos'
      );
    }

    if (validationErrors.length > 0) {
      const errorMessages = validationErrors.join('\n');
      this.utils.showSWAL(
        'Errores de Validación',
        errorMessages,
        'OK',
        'warning'
      );
    }
    this.api.doRequest('company', this.companies, 'post').then((res: any) => {
      if (res.code == 200) {
        this.closeModal();
        this.utils.showSWAL(
          'EMPRESA AGREGADA',
          'la empresa fue ingresada con éxito',
          'OK'
        );

        this.sharedDataService.setVariable('load-current-page', true);
      }

      if (res.code != 200) {
        this.utils.showSWAL(res.message, res.data, 'OK');
      }
    });
  }

  setCompanies() {
    this.companies = {
      nameCompany: this.institucionForm.value['nameCompany'],
      emailCompany: this.institucionForm.value['emailCompany'],
      descriptionCompany: this.institucionForm.value['descriptionCompany'],
      nit: this.institucionForm.value['nit'],
      nrc: this.institucionForm.value['nrc'],
      addressCompany: this.institucionForm.value['addressCompany'],
      phoneCompany: this.institucionForm.value['phoneCompany'],
      idGiroCompany: this.institucionForm.value['idGiroCompany'],
      nombreGiroCompany: this.institucionForm.value['nombreGiroCompany'],
      idRecinto: this.institucionForm.value['idRecinto'],
      idRegimen: this.institucionForm.value['idRegimen'],
      nombreRecintoCompany: this.institucionForm.value['nombreRecintoCompany'],
      nombreRegimenCompany: this.institucionForm.value['nombreRegimenCompany'],
      socialReasonCompany: this.institucionForm.value['socialReasonCompany'],
      idMuniCompany: this.institucionForm.value['idMuniCompany'],
      nombreMunicipioCompany:
        this.institucionForm.value['nombreMunicipioCompany'],

      idCompany: this.institucionForm.value['idCompany'],
      file: this.institucionForm.value['file'],
      mhUser: this.institucionForm.value['mhUser'],
      mhPass: this.institucionForm.value['mhPass'],
      clavePrimariaCert: this.institucionForm.value['clavePrimariaCert'],
      active: this.institucionForm.value['active'],
      idActividadMH: 1087,          // ← agregado
      idDeptoCompany: 6,
    };
  }

  actualizarInstitucion() {
    if (this.institucionForm.invalid) {
      let errores: string[] = [];

      const showErrorAndReset = (fieldKey: string, message: string) => {
        this.institucionForm.get(fieldKey)?.setValue('');
        return this.util.showSWAL(fieldKey, message, 'OK', 'warning');
      };

      Object.keys(this.institucionForm.controls).forEach((key) => {
        const control = this.institucionForm.get(key);
        if (control?.errors) {
          errores.push(`Campo inválido: ${key}`);
        }
      });

      const validations = [
        {
          field: 'idGiroCompany',
          errorField: 'nombreGiroCompany',
          message: 'Debe Escribir una actividad Económica válida',
        },
        {
          field: 'idMuniCompany',
          errorField: 'nombreMunicipioCompany',
          message: 'Debe Escribir un municipio correcto',
        },
        {
          field: 'nombreRecintoCompany',
          relatedField: 'idRecinto',
          message: 'Debe Escribir un recinto fiscal válido',
        },
        {
          field: 'nombreRegimenCompany',
          relatedField: 'idRegimen',
          message: 'Debe Escribir un régimen válido',
        },
        { field: 'id_muni', message: 'Debe Seleccionar un municipio válido' },
        {
          field: 'id_giro',
          message: 'Debe Escribir una actividad Económica válida',
        },
      ];

      for (const { field, errorField, relatedField, message } of validations) {
        const fieldValue = this.institucionForm.get(field)?.value;
        const relatedValue = relatedField
          ? this.institucionForm.get(relatedField)?.value
          : null;

        if (!fieldValue || (relatedField && fieldValue && !relatedValue)) {
          return showErrorAndReset(errorField ?? field, message);
        }
      }

      if (errores.length > 0) {
        return this.utils.showSWAL(
          'Formulario Incompleto',
          'Debe llenar todos los campos requeridos: ' + errores.join(', '),
          'OK',
          'warning'
        );
      }

      return this.util.showSWAL(
        'ERROR EN FORMULARIO',
        errores.join(', '),
        'error'
      );
    }

    this.setCompanies();

    return this.api
      .doRequest('/company/' + this.companies.idCompany, this.companies, 'put')
      .then((res: any) => {
        if (res.code == 200) {
          this.sharedDataService.setVariable('load-current-page', true);

          return this.utils
            .showSWAL(
              'EMPRESA ACTUALIZADA',
              'la empresa fue actualizada con éxito',
              'OK',
              'success'
            )
            .then((resp: boolean) => {
              if (resp) {
                window.location.reload();
              }
            });
        }
        return this.utils.showSWAL(
          'ERROR AL ACTUALIZAR',
          res.data,
          'OK',
          'error'
        );
      });
  }
}
