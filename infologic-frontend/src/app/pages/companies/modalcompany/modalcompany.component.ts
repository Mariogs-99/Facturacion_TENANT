import { Component, Inject } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { CompaniesModel } from '../companies.model';

import { ApiService } from 'src/app/core/services/api.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ValidacionesService } from 'src/app/core/services/validaciones.service';

@Component({
  selector: 'app-modalcompany',
  templateUrl: './modalcompany.component.html',
})
export class ModalcompanyComponent {
  public registerForm: FormGroup;

  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public utils: UtilsService,
    private formBuider: FormBuilder,
    private util: UtilsService,
    public validaciones: ValidacionesService,
    private sharedDataService: SharedDataService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.registerForm = this.formBuider.group({
      file: new FormControl(''),
      nameCompany: new FormControl('', [Validators.required]),
      nombreGiroCompany: new FormControl('', [Validators.required]),
      nombreRecintoCompany: new FormControl(''),
      nombreRegimenCompany: new FormControl(''),
      nombreMunicipioCompany: new FormControl('', [Validators.required]),
      socialReasonCompany: new FormControl('', [Validators.required]),
      descriptionCompany: new FormControl('', [Validators.required]),
      nit: new FormControl('', [Validators.required]),
      nrc: new FormControl('', [Validators.required]),
      idDeptoCompany: new FormControl(''),
      phoneCompany: new FormControl('', [Validators.required]),
      emailCompany: new FormControl('', [
        Validators.email,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/),
      ]),
      addressCompany: new FormControl('', [Validators.required]),
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
  }

  isupdating: any;
  catalogos: any;
  editcompany: any;
  ngOnInit() {
    this.fillData(this.data.data);
    this.isupdating = this.sharedDataService.getVariableValue('editarCompany');
    this.catalogos = this.sharedDataService.getVariableValue('catalogos');

    this.municipios = this.catalogos.municipios;
    this.giros = this.catalogos.giros;
    this.recinto = this.catalogos.recintos;
    this.regimen = this.catalogos.regimens;
  }
  ngAfterViewInit(): void {
    this.editcompany = this.sharedDataService.getVariableValue('company');
  }
  companies = new CompaniesModel();

  displayedColumns: string[] = [
    'logo',
    'nameCompany',
    'idDeptoCompany',
    'actions',
  ];
  inputChange: any = '';

  filteredOptions: Array<any> = [];
  filteredOptionsGiros: Array<any> = [];
  filteredOptionsRegimen: Array<any> = [];
  filteredOptionsRecinto: Array<any> = [];

  municipios: Array<any> = [];
  giros: Array<any> = [];
  regimen: Array<any> = [];
  recinto: Array<any> = [];
  new_company: Array<any> = [];
  old_image: string = '';
  imagenPrevia: string = '';
  files: any = [];
  loading: boolean = false;

  fillData(element: any) {
    for (const atr in this.companies) {
      try {
        this.registerForm.controls[atr].setValue(element[atr]);
      } catch (e) {}
    }
  }

  public onFileSelected(event: any) {
    const imagen = event.target.files[0];

    if (imagen.type.startsWith('image/')) {
      this.files.push(imagen);
    }
  }

  public borrarImagen(event: any) {
    this.imagenPrevia = '';
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

  filtervaluesModalCompany(event: any = '', opt: any = 1) {
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

  compareObjects(o1: any, o2: any): boolean {
    return parseInt(o1) === o2;
  }

  selectChange(event: any, opt = 1) {
    let id = event.option.value;
    switch (opt) {
      case 1:
        let val2: any = this.municipios.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];
        this.registerForm.get('idMuniCompany')?.setValue(val2.id);
        this.registerForm.get('nombreMunicipioCompany')?.setValue(val2.name);
        this.registerForm.get('idDeptoCompany')?.setValue(val2.idDeptoCompany);
        break;

      case 2:
        let val: any = this.giros.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];
        this.registerForm.get('idGiroCompany')?.setValue(val.id);
        this.registerForm.get('nombreGiroCompany')?.setValue(val.name);
        break;
      case 3:
        let val3: any = this.recinto.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];
        this.registerForm.get('idRecinto')?.setValue(val3.id);
        this.registerForm.get('nombreRecintoCompany')?.setValue(val3.name);
        break;
      case 4:
        let val4: any = this.regimen.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];

        this.registerForm.get('idRegimen')?.setValue(val4.id);
        this.registerForm.get('nombreRegimenCompany')?.setValue(val4.name);
        break;
    }
  }

  resetOptions() {
    this.filtervaluesModalCompany('');
    this.filtervaluesModalCompany('', 1);
    this.filtervaluesModalCompany('', 2);
    this.filtervaluesModalCompany('', 3);
    this.filtervaluesModalCompany('', 4);
  }

  toLower(event: any) {
    event.target.value = event.target.value.toLowerCase();
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^@.A-Za-z0-9_]/g, '').toLowerCase();
    input.value = value;
    this.registerForm.get('emailCompany')?.setValue(value);
  }

  closeModal() {
    this.registerForm.reset();
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
      idDeptoCompany: 'Direccion',
      phoneCompany: 'Telefono',
      emailCompany: 'Correo Electronico',
      mhUser: 'Usuario minesterio de hacienda',
      mhPass: 'Contraseña minesterio de hacienda',
      clavePrimariaCert: 'Clave primaria certificado',
    };

    const requiredFields: string[] = [];
    Object.keys(this.registerForm.controls).forEach((key) => {
      const control = this.registerForm.get(key);

      if (control?.errors?.['required']) {
        requiredFields.push(fielddescriptionCompanys[key] || key);
      }
    });
    return `Completa los campos requeridos: ${requiredFields.join(', ')}`;
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
        condition: !!this.registerForm.get('nombreRecintoCompany')?.value,
      },
      {
        id: 'idRegimen',
        name: 'nombreRegimenCompany',
        errorTitle: 'Regimen incorrecto',
        errorMessage: 'Debe Escribir un régimen válido',
        condition: !!this.registerForm.get('nombreRegimenCompany')?.value,
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

    if (this.registerForm.get('emailCompany')?.invalid) {
      validationErrors.push(
        'Correo de la Empresa Incorrecto: Debe Escribir un correo válido'
      );
    }

    if (this.registerForm.invalid) {
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

    this.old_image = '';
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
      nameCompany: this.registerForm.value['nameCompany'],
      emailCompany: this.registerForm.value['emailCompany'],
      descriptionCompany: this.registerForm.value['descriptionCompany'],
      nit: this.registerForm.value['nit'],
      nrc: this.registerForm.value['nrc'],
      addressCompany: this.registerForm.value['addressCompany'],
      phoneCompany: this.registerForm.value['phoneCompany'],
      idGiroCompany: this.registerForm.value['idGiroCompany'],
      nombreGiroCompany: this.registerForm.value['nombreGiroCompany'],
      idRecinto: this.registerForm.value['idRecinto'],
      idRegimen: this.registerForm.value['idRegimen'],
      nombreRecintoCompany: this.registerForm.value['nombreRecintoCompany'],
      nombreRegimenCompany: this.registerForm.value['nombreRegimenCompany'],
      socialReasonCompany: this.registerForm.value['socialReasonCompany'],
      idMuniCompany: this.registerForm.value['idMuniCompany'],
      nombreMunicipioCompany: this.registerForm.value['nombreMunicipioCompany'],
      idDeptoCompany: this.registerForm.value['idDeptoCompany'],
      idCompany: this.registerForm.value['idCompany'],
      file: this.registerForm.value['file'],
      mhUser: this.registerForm.value['mhUser'],
      mhPass: this.registerForm.value['mhPass'],
      clavePrimariaCert: this.registerForm.value['clavePrimariaCert'],
      active: this.registerForm.value['active'],
      old_image: this.old_image,
    };
  }

  actualizar() {
    if (this.registerForm.invalid) {
      const errores: string[] = [];
      const showError = (key: string, message: string) => {
        this.registerForm.get(key)?.setValue('');
        return this.util.showSWAL(key, message, 'OK', 'warning');
      };

      Object.keys(this.registerForm.controls).forEach((key) => {
        const control = this.registerForm.get(key);
        if (control?.errors) {
          errores.push(`Campo inválido: ${key}`);
        }
      });

      const validations = [
        {
          key: 'idGiroCompany',
          errorKey: 'nombreGiroCompany',
          message: 'Debe Escribir una actividad Económica válida',
        },
        {
          key: 'idMuniCompany',
          errorKey: 'nombreMunicipioCompany',
          message: 'Debe Escribir un municipio correcto',
        },
        {
          key: 'nombreRecintoCompany',
          relatedKey: 'idRecinto',
          message: 'Debe Escribir un recinto fiscal válido',
        },
        {
          key: 'nombreRegimenCompany',
          relatedKey: 'idRegimen',
          message: 'Debe Escribir un régimen válido',
        },
        { key: 'id_muni', message: 'Debe Seleccionar un municipio válido' },
        {
          key: 'id_giro',
          message: 'Debe Escribir una actividad Económica válida',
        },
      ];

      for (const { key, relatedKey, errorKey, message } of validations) {
        const value = this.registerForm.get(key)?.value;
        const relatedValue = relatedKey
          ? this.registerForm.get(relatedKey)?.value
          : null;

        if (!value || (relatedKey && value && !relatedValue)) {
          return showError(errorKey ?? key, message);
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

          this.closeModal();
          this.utils.showSWAL(
            'EMPRESA ACTUALIZADA',
            'la empresa fue actualizada con éxito',
            'OK',
            'success'
          );
        }
        if (res.code != 200) {
          this.utils.showSWAL('ERROR AL ACTUALIZAR', res.data, 'OK', 'error');
        }
      });
  }

  validarCampoModalCompany(
    formControlName: string,
    titulo: string,
    mensaje: string
  ): boolean {
    const valor = this.registerForm.get(formControlName)?.value;
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
    const valor = this.registerForm.get(formControlName)?.value;
    if (!valor && condicionExtra) {
      this.registerForm.get(campoAResetear)?.setValue('');
      this.util.showSWAL(titulo, mensaje, 'OK', 'warning');
      return false;
    }
    return true;
  }
}
