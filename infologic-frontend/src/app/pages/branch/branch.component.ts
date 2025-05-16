import {
  Component,
  OnInit,
  ViewChild,
  TemplateRef,
  OnDestroy,
} from '@angular/core';
import { BranchModel } from './branch.model';
import { ApiService } from 'src/app/core/services/api.service';
import { MatPaginator } from '@angular/material/paginator';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { UtilsService } from 'src/app/core/services/utils.service';
import { MatDialog } from '@angular/material/dialog';

import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
  AbstractControl,
} from '@angular/forms';

import Swal from 'sweetalert2';
import { DataService } from 'src/app/core/services/data.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Rango } from 'src/app/core/model/range.model';
import { MatSort, Sort } from '@angular/material/sort';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { ModalbranchComponent } from './modalbranch/modalbranch.component';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { Subscription } from 'rxjs';

interface Catalogo {
  id?: number;
  name?: string;
  order?: number;
}

@Component({
  selector: 'app-branch',
  styleUrls: ['branch.component.css'],
  templateUrl: './branch.component.html',
  styles: [],
})
export class BranchComponent implements OnInit, OnDestroy {
  public permissions: any;

  public registerForm: FormGroup;
  public configBranchForm!: FormGroup;

  public ajustesPdf: Catalogo[] = [];

  public imagePrev: any;
  public configPdf!: any;
  public configData!: any;
  public index!: any;
  public opcionesSelectCompany: Array<any> = [];
  public filteredOptions: Array<any> = [];
  public municipios: Array<any> = [];
  tiposEstablecimientos: Array<any> = [];

  public branch = new BranchModel();
  public isupdating: boolean = false;

  public dataSource = new MatTableDataSource<any>();

  public displayedColumns: string[] = [
    'nombre',
    'nombreCompany',
    'misional',
    'codigoSucursal',
    'direccion',
    'telefono',
    'email',
  ];
  ranges: any;
  tipoRango: any;
  parentData: any;
  rangos: any;
  elementidSucursal: any;
  companies: Array<any> = [];
  new_branch: Array<any> = [];
  subscribe: Subscription;
  public permissionsArr: string[] = [];

  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public utils: UtilsService,
    private formBuider: FormBuilder,
    private data: DataService,
    private _auth: AuthService,
    private _liveAnnouncer: LiveAnnouncer,
    private sharedDataService: SharedDataService,
    private _data: DataService
  ) {
    this.permissionsArr = this._auth.getPermissions();

    if (
      this.permissionsArr.includes('Actualizar') ||
      this.permissionsArr.includes('Eliminar')
    ) {
      this.displayedColumns.push('actions');
    }

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
      idDeptoBranch: new FormControl('', [Validators.required]),
      nombreMunicipioBranch: new FormControl('', [Validators.required]),
      idMuniBranch: new FormControl('', [Validators.required]),
      idCompany: new FormControl('', [Validators.required]),
      nombreCompany: new FormControl(''),
      codigoSucursal: new FormControl(''),
      misional: new FormControl(''),
      idTipoEstablecimiento: new FormControl(''),
      idSucursal: new FormControl(''),
    });

    this.configBranchForm = this.formBuider.group({
      pdfConfig: new FormControl(''),
    });
    this.subscribe = this._data.getData().subscribe((resp) => {
      if (resp.component == 'BranchComponent' && resp.action == 'open') {
        this.openModal();
      }
    });
  }
  ngOnDestroy(): void {
    this.subscribe.unsubscribe();
  }

  getControl(name: any): AbstractControl | null {
    return this.registerForm.get(name);
  }

  @ViewChild('branchPaginator') branchPaginator!: MatPaginator;

  @ViewChild('tableBranch') tableBranch!: MatTable<BranchModel>;
  @ViewChild('tableRangos') tableRangos!: MatTable<Rango>;
  @ViewChild('branchModal') branchmodal!: TemplateRef<any>;
  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  ngOnInit() {
    this.isupdating = false;

    this.sharedDataService.setVariable('editarBranch', false);
  }

  companyName: string = '';
  ngAfterViewInit(): void {
    this.registerForm.get('idCompany')?.valueChanges.subscribe((company) => {
      let companyname = this.opcionesSelectCompany.filter((item) => {
        if (item.id == company) {
          this.companyName = item.name;
          return item.name;
        }
      });
      this.registerForm.get('nombreCompany')?.setValue(companyname);
    });

    this.dialog.afterAllClosed.subscribe((res) => {
      this.data.sendMessage({ from: 'api', status: false });
    });

    this.inicializartabla();

    this.registerForm.get('idMuniBranch')?.valueChanges.subscribe((muni) => {
      if (muni) {
        let val: any = this.municipios.filter((item) => {
          return item.id == muni;
        })[0];
        this.registerForm.controls['nombreMunicipioBranch'].setValue(
          val.name as string
        );
        this.branch.nombreMunicipioBranch = val.name;
      }
    });
  }

  openModal() {
    this.isupdating = false;

    this.sharedDataService.setVariable('editarBranch', false);
    this.isupdating = this.sharedDataService.getVariableValue('editarBranch');

    this.branch = new BranchModel();
    this.registerForm.reset(this.companies);
    this.isupdating = false;
    this.registerForm.reset(this.branch);

    this.utils.openModal({
      title: 'Gestión de Puntos de Venta',
      botonAceptar: 'Agregar',
      componentToLoad: ModalbranchComponent,
      callerComponent: this,
      data: null,
    });
  }

  closeModal() {
    this.registerForm.reset();
    this.dialog.closeAll();
    this.imagePrev = false;
  }

  editar(element: any) {
    this.sharedDataService.setVariable('editarBranch', true);

    this.sharedDataService.setVariable('branchCompany', true);
    this.sharedDataService.setVariable('branch', element);

    this.utils.openModal({
      title: 'Gestión de Puntos de Venta',
      botonAceptar: 'Editar',
      componentToLoad: ModalbranchComponent,
      callerComponent: this,
      data: element,
    });

    this.branch = element;
  }

  setearFormulario() {
    this.registerForm.patchValue({
      nombre: this.branches.find(
        (branch) => branch.ID_BRANCH == this.branch.idSucursal
      ).name,

      direccion: this.branch.direccion,
      idCompany: this.branch.idCompany,
      nombreMunicipioBranch: this.municipios.find(
        (muni) => muni.id == this.branch.idMuniBranch
      )?.name,
      telefono: this.branch.telefono,
      codigoSucursal: this.branch.codigoSucursal,
      misional: this.branch.misional,
      email: this.branch.email,
      idDeptoBranch: this.branch.idDeptoBranch,
      idMuniBranch: this.branch.idMuniBranch,
      idTipoEstablecimiento: this.branch.idTipoEstablecimiento,
      idSucursal: this.branch.idSucursal,
    });
  }

  eliminar(element: any, i: number) {
    Swal.fire({
      title: 'Desactivar Registro',
      text: 'Se dispone a desactivar la Sucursal ' + element.nombre,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, desactivar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.branch = element;

        this.api
          .doRequest('/sucursal/' + element.idSucursal, this.branch, 'delete')
          .then((res: any) => {
            if (res.code == 200) {
              this.closeModal();
              this.utils.showSWAL(
                'SUCURSAL ELIMINADA',
                'la sucursal fue eliminada con éxito',
                'OK',
                'success'
              );
              this.dataSource.data.splice(i, 1);
              this.dataSource = new MatTableDataSource<any>(
                this.dataSource.data
              );
              this.dataSource.paginator = this.branchPaginator;
            }

            if (res.code != 200) {
              this.utils.showSWAL(
                'ERROR',
                'la sucursal no fue eliminada \n' + res.data,
                'OK',
                'error'
              );
            }
          });
      }
    });
  }

  setPreview(test: any) {
    this.configPdf = test.value.id;
    this.imagePrev = test.value.order;
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

  branches: Array<any> = [];
  inicializartabla() {
    if (!this.permissionsArr.includes('Index')) {
      return this.utils.showSWAL(
        'No tiene permisos para visualizar',
        'Contacte al Administrador',
        'Aceptar',
        'warning'
      );
    }

    this.dataSource = new MatTableDataSource<any>();
    return this.api
      .doRequest('/sucursal/index', this.branch, 'get')
      .then((res: any) => {
        if (res.code == 200) {
          this.ajustesPdf = res.data.ajustes;

          this.tiposEstablecimientos = res.data.tipos_establecimientos;
          this.dataSource = new MatTableDataSource<any>(res.data.branches);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.branchPaginator;
          this.opcionesSelectCompany = res.data.companies;
          this.municipios = res.data.municipios;
          this.companies = res.data.companies;
          this.branches = res.data.branches;
          this.sharedDataService.setVariable('catalogos', res.data.catalogos);
          this.new_branch = res.data.branches.filter(
            (branch: any) => branch.new_branch === 1
          );

          this.municipios = res.data.catalogos.municipios;
          this.sharedDataService.setVariable('catalogos', res.data.catalogos);
          this.sharedDataService.setVariable('companies', res.data.companies);

          Swal.close();
        }
      });
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
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

  getRequiredFields(): string {
    const fieldDescriptions: { [key: string]: string } = {
      name: 'Nombre',
      direccion: 'Direccion',
      telefono: '7777-7777',
      codigoSucursal: 'codigo Sucursal',
      misional: 'Misional',
      email: 'Correo Electronico',
      nombreMunicipioBranch: 'Municipio',
    };

    const requiredFields: string[] = [];
    Object.keys(this.registerForm.controls).forEach((key) => {
      const control = this.registerForm.get(key);
      if (control?.errors?.['required']) {
        requiredFields.push(fieldDescriptions[key] || key);
      }
    });
    return `Completa los campos requeridos: ${requiredFields.join(', ')}`;
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

    return this.setBranch();
  }

  setBranch(rangos?: any) {
    this.rangos = rangos;

    this.branch = {
      nombre: this.registerForm.value['name'],
      email: this.registerForm.value['email'],
      direccion: this.registerForm.value['direccion'],
      telefono: this.registerForm.value['telefono'],
      codigoSucursal: this.registerForm.value['codigoSucursal'],
      misional: this.registerForm.value['misional'],
      idDeptoBranch: this.registerForm.value['idDeptoBranch'],
      idMuniBranch: this.registerForm.value['idMuniBranch'],
      idCompany: this.registerForm.value['idCompany'],
      nombreMunicipioBranch: this.registerForm.value['nombreMunicipioBranch'],
      idSucursal: this.registerForm.value['id'],
      idTipoEstablecimiento: parseInt(
        this.registerForm.value['idTipoEstablecimiento']
      ),
      config_pdf: this.configPdf,
      config: this.configData,
      rango: this.rangos,
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
    if (this.registerForm.invalid) {
      return this.utils.showSWAL(
        'Formulario Incompleto',
        'Debe llenar todos los campos Requeridos',
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

    return this.api
      .doRequest('branch/store', this.branch, 'post')
      .then((res: any) => {
        if (res.code == 200) {
          return this.utils.showSWAL(
            'SUCURSAL AGREGADA',
            'la sucursal fue ingresada con éxito',
            'OK'
          );
        }

        return this.utils.showSWAL(res.message, res.data, 'OK');
      });
  }

  compareObjects(o1: any, o2: any): boolean {
    if (typeof o1 == 'object') {
      return parseInt(o1.id) === o2;
    }
    return parseInt(o1) === parseInt(o2);
  }

  ngDoCheck(): void {
    const load_page =
      this.sharedDataService.getVariableValue('load-current-page');

    if (load_page) {
      this.inicializartabla();
      this.sharedDataService.setVariable('load-current-page', false);
    }
  }

  capitalizeWords(value: string): string {
    if (!value) return '';
    return value
      .split(' ')
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }
}
