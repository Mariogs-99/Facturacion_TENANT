import { Component } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { MatDialog } from '@angular/material/dialog';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { BranchModel } from '../branch.model';
import Swal from 'sweetalert2';

interface Catalogo {
  id?: number;
  name?: string;
  order?: number;
}

@Component({
  selector: 'app-modalbranch',
  templateUrl: './modalbranch.component.html',
  styleUrls: ['./modalbranch.component.css'],
})
export class ModalbranchComponent {
  public registerForm: FormGroup;
  public configBranchForm!: FormGroup;
  public opcionesSelectCompany: Array<any> = [];
  public filteredOptions: Array<any> = [];
  public municipios: Array<any> = [];
  tiposEstablecimientos: Array<any> = [];
  public ajustesPdf: Catalogo[] = [];

  public imagePrev: any;
  public configPdf!: any;
  public configData!: any;
  public index!: any;
  ranges: any;
  tipoRango: any;
  parentData: any;
  rangos: any;
  elementidSucursal: any;
  companies: Array<any> = [];
  new_branch: Array<any> = [];

  public branch = new BranchModel();
  public body = {};
  public isupdating: boolean = false;

  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public utils: UtilsService,
    private formBuider: FormBuilder,
    private sharedDataService: SharedDataService
  ) {
    this.registerForm = this.formBuider.group({
      nombre: new FormControl('', [
        Validators.required,
        Validators.maxLength(150),
      ]),
      direccion: new FormControl('', [
        Validators.required,
        Validators.maxLength(150),
      ]),
      telefono: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
      email: new FormControl('', [
        Validators.email,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/),
      ]),
      idDeptoBranch: new FormControl(''),
      nombreMunicipioBranch: new FormControl(''),
      idMuniBranch: new FormControl('', [Validators.required]),
      idCompany: new FormControl('', [Validators.required]),
      nombreCompany: new FormControl(''),
      idTipoEstablecimiento: new FormControl(''),
      codigoSucursal: new FormControl(''),
      misional: new FormControl(''),
      id: new FormControl(''),
    });

    this.configBranchForm = this.formBuider.group({
      pdfConfig: new FormControl(''),
    });
  }

  catalogos: any;
  ngOnInit() {
    this.isupdating = this.sharedDataService.getVariableValue('editarBranch');
    this.catalogos = this.sharedDataService.getVariableValue('catalogos');
    this.opcionesSelectCompany =
      this.sharedDataService.getVariableValue('companies');
    this.municipios = this.catalogos.municipios;

    this.tiposEstablecimientos = this.catalogos.tipos_establecimientos;

    if (this.isupdating) {
      this.branch = this.sharedDataService.getVariableValue('branch');

      const idCompanyValue = this.branch['idCompany'];
      const empresaSeleccionada = this.opcionesSelectCompany.find(
        (company: any) => company.idCompany === idCompanyValue
      );
      if (empresaSeleccionada) {
        this.registerForm.controls['idCompany'].setValue(empresaSeleccionada);
        this.registerForm.controls['idCompany'].updateValueAndValidity();
      }

      const idTipoEstablecimientoValue = this.branch['idTipoEstablecimiento'];
      const tipoEstablecimientoSeleccionado = this.tiposEstablecimientos.find(
        (tipo: any) => tipo.id === idTipoEstablecimientoValue
      );
      if (tipoEstablecimientoSeleccionado) {
        this.registerForm.controls['idTipoEstablecimiento'].setValue(
          tipoEstablecimientoSeleccionado
        );
        this.registerForm.controls[
          'idTipoEstablecimiento'
        ].updateValueAndValidity();
      }

      const idMunicipioValue = this.branch['idMuniBranch'];
      const municipioSeleccionado = this.catalogos.municipios.find(
        (municipio: any) => municipio.id === idMunicipioValue
      );
      if (municipioSeleccionado) {
        this.registerForm.controls['nombreMunicipioBranch'].setValue(
          municipioSeleccionado.name
        );
        this.registerForm.controls[
          'nombreMunicipioBranch'
        ].updateValueAndValidity();
      }

      this.fillData(this.branch);
    }
  }

  closeModal() {
    this.registerForm.reset();
    this.dialog.closeAll();
  }

  onKeyPress(event: KeyboardEvent, regex: RegExp) {
    if (!regex.test(event.key)) {
      event.preventDefault();
    }
  }

  onKeyPressNoSignos(event: KeyboardEvent) {
    this.onKeyPress(event, /^[a-zA-Z0-9 ]$/);
  }

  onKeyPressNumeros(event: KeyboardEvent) {
    this.onKeyPress(event, /^\d$/);
  }

  onKeyPressLetras(event: KeyboardEvent) {
    this.onKeyPress(event, /^[a-zA-Z ]$/);
  }

  compareObjects(o1: any, o2: any): boolean {
    if (typeof o1 == 'object') {
      return parseInt(o1.id) === o2;
    }
    return parseInt(o1) === parseInt(o2);
  }

  filtervaluesModalBranch(event: any = '', opt: any = 1) {
    if (event) {
      switch (opt) {
        case 1:
          let value = this.municipios.filter((item) => {
            if (item.name.toUpperCase().includes(event.toUpperCase())) {
              return item;
            }
          });
          this.filteredOptions = value;
          break;
        case 2:
          break;
      }
    }
  }

  resetOptions() {
    this.filtervaluesModalBranch('');
    this.filtervaluesModalBranch('', 1);
    this.filtervaluesModalBranch('', 2);
    this.filtervaluesModalBranch('', 3);
    this.filtervaluesModalBranch('', 4);
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
        this.registerForm.get('idMuniBranch')?.setValue(val2.id);
        this.registerForm.get('nombreMunicipioBranch')?.setValue(val2.name);
        this.registerForm.get('idDeptoBranch')?.setValue(val2.idDeptoBranch);
        break;

      case 2:
        break;
    }
  }

  toLower(event: any) {
    event.target.value = event.target.value.toLowerCase();
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^@.A-Za-z0-9_]/g, '').toLowerCase();
    input.value = value;
    this.registerForm.get('email')?.setValue(value);
  }


  fillData(element: any) {
    if (
      element['config'] != '' &&
      element['config'] != undefined &&
      element['config'] != 'undefined'
    ) {
      this.configData = element['config'][0].value;
      this.configBranchForm.controls['pdfConfig'].setValue(
        element['config'][0].value
      );
      this.imagePrev = this.ajustesPdf.filter(
        (item: any) => item.id == element['config'][0].value
      );
      this.imagePrev = this.imagePrev[0].order;
    } else {
      this.configData = null;
      this.configBranchForm.controls['pdfConfig'].setValue('');
    }

    for (const atr in this.branch) {
      try {
        if (element[atr] != undefined) {
          this.registerForm.controls[atr].setValue(element[atr]);
        }
      } catch (e) {}
    }
  }

  setBranch(rangos?: any) {
    this.rangos = rangos;

    this.branch = {
      nombre: this.registerForm.value['nombre'],
      email: this.registerForm.value['email'],
      direccion: this.registerForm.value['direccion'],
      telefono: this.registerForm.value['telefono'],
      codigoSucursal: this.registerForm.value['codigoSucursal'],
      misional: this.registerForm.value['misional'],
      idDeptoBranch: this.registerForm.value['idDeptoBranch'],
      idMuniBranch: this.registerForm.value['idMuniBranch'],
      idCompany: this.registerForm.value['idCompany'],
      nombreMunicipioBranch: this.registerForm.value['nombreMunicipioBranch'],
      idSucursal: this.branch.idSucursal,
      idTipoEstablecimiento: parseInt(
        this.registerForm.value['idTipoEstablecimiento']
      ),
      config_pdf: this.configPdf,
      config: this.configData,
      rango: this.rangos,
    };

    this.body = {
      branch: this.branch,
      ranges: this.rangos,
    };
  }

  guardar() {
    if (!this.registerForm.get('idMuniBranch')?.value) {
      this.registerForm.get('nombreMunicipioBranch')?.setValue('');
      return this.utils.showSWAL(
        'Municipio incorrecto',
        'Debe Escribir un municipio correcto',
        'OK',
        'warning'
      );
    }
    if (!this.registerForm.get('idMuniBranch')?.value) {
      return this.utils.showSWAL(
        'Municipio Incorrecto',
        'Debe Seleccionar un municipio válido',
        'OK',
        'warning'
      );
    }

    this.setBranch(this.parentData ?? null);

    return this.api.doRequest('/sucursal', this.body, 'post').then((res: any) => {
      if (res.code == 200) {
        this.closeModal();
        this.utils.showSWAL(
          'SUCURSAL AGREGADA',
          'la sucursal fue ingresada con éxito',
          'OK'
        );
        this.sharedDataService.setVariable('load-current-page', true);
      }
    });
  }

  preview() {
    Swal.fire({
      title: 'Tipo ' + this.imagePrev,
      text: 'Esta es una vista previa de una factura.',
      imageUrl: 'assets/images/plantillas_pdf/' + this.imagePrev + '.PNG',
      imageWidth: 250,
      imageHeight: 350,
      imageAlt: 'Vista previa',
    });
  }

  setPreview(test: any) {
    this.configPdf = test.value.id;
    this.imagePrev = test.value.order;
  }

  actualizar() {
    if (!this.registerForm.get('idMuniBranch')?.value) {
      this.registerForm.get('nombreMunicipioBranch')?.setValue('');
      return this.utils.showSWAL(
        'Municipio incorrecto',
        'Debe Escribir un municipio correcto',
        'OK',
        'warning'
      );
    }
    if (this.registerForm.invalid) {
      let errores = '';
      Object.keys(this.registerForm.controls).forEach((key) => {
        const control = this.registerForm.get(key);
        if (control != null) {
          const controlErrors = control.errors;
          if (controlErrors != null) {
            Object.keys(controlErrors).forEach((keyError) => {
              errores =
                'Campo inválido: ' +
                key +
                ', error: ' +
                keyError +
                ', valor: ' +
                controlErrors[keyError];
            });
          }
        }
      });
      return this.utils.showSWAL('ERROR EN FORMULARIO', errores);
    }

    this.setBranch();

    return this.api
      .doRequest('/sucursal/' + this.branch.idSucursal, this.branch, 'put')
      .then((res: any) => {
        if (res.code == 200) {
          return Swal.fire({
            title: 'Se actualizó con exito',
            html: '',
            icon: 'success',
            confirmButtonText: 'aceptar',
          }).then(() => {
            this.closeModal();
            return this.sharedDataService.setVariable(
              'load-current-page',
              true
            );
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
