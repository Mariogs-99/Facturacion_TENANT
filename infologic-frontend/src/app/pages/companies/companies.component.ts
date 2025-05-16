import {
  AfterViewInit,
  Component,
  NgZone,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import Swal from 'sweetalert2';
import { CompaniesModel } from './companies.model';

import { LiveAnnouncer } from '@angular/cdk/a11y';
import { MatSort, Sort } from '@angular/material/sort';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';

import { Subscription } from 'rxjs';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { ValidacionesService } from 'src/app/core/services/validaciones.service';

@Component({
  selector: 'app-companies',
  styleUrls: ['companies.component.css'],
  templateUrl: './companies.component.html',

  styles: [],
})
export class CompaniesComponent implements OnInit, AfterViewInit {
  public registerForm: FormGroup;
  public permissions: any;
  subscribe: Subscription;

  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public utils: UtilsService,
    private formBuider: FormBuilder,
    private data: DataService,
    private util: UtilsService,
    private _liveAnnouncer: LiveAnnouncer,
    private sharedDataService: SharedDataService,
    private _data: DataService,
    private zone: NgZone,
    public validaciones: ValidacionesService
  ) {
    this.registerForm = this.formBuider.group({
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
      idDeptoCompany: new FormControl('', [Validators.required]),
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
      if (resp.component == 'CompaniesComponent' && resp.action == 'open') {
        this.openModal();
      }
    });
  }
  getControl(name: any): AbstractControl | null {
    return this.registerForm.get(name);
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
  new_company: Array<any> = [];
  old_image: string = '';
  imagenPrevia: string = '';
  files: any = [];
  loading: boolean = false;

  dataSource = new MatTableDataSource<any>();

  displayedColumns: string[] = ['name', 'address', 'logo', 'acciones'];
  currentPageNo: any = 1;
  totalRecords: number = 0;
  paginationSize: number = 10;
  sortBy: string = '';
  filterByColumn: string = '';
  Filtercolumn: string = '';
  paginationSizeOptions: number[] = [5, 10, 25, 50, 100];

  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  lenghth!: number;
  pageSizeOptions = [5, 10, 20];
  pageSize = 10;
  length = 200;
  lowValue: number = 0;
  highValue: number = 5;
  showFirstLastButtons = true;
  pageEvent!: PageEvent;

  @ViewChild('companiesModal') companiesmodal!: TemplateRef<any>;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  ngAfterViewInit(): void {
    this.zone.runOutsideAngular(() => {
      setTimeout(() => {
        this.dataSource.sort = this.sort;

        this.paginator.pageSizeOptions = [5, 10, 20];
        this.paginator.pageSize = 10;
        this.paginator.length = 200;

        this.dataSource.paginator = this.paginator;
      });
    });

    this.dataSource.paginator = this.paginator;
    this.dialog.afterAllClosed.subscribe(() => {
      this.data.sendMessage({ from: 'api', status: false });
    });
    this.inicializartabla();

    this.registerForm.get('idMuniCompany')?.valueChanges.subscribe((muni) => {
      if (muni) {
        let val: any = this.municipios.filter((item) => {
          return item.id == muni;
        })[0];

        this.registerForm.controls['nombreMunicipioCompany'].setValue(
          val.name as string
        );
        this.companies.nombreMunicipioCompany = val.name;
      }
    });

    this.registerForm.get('idGiroCompany')?.valueChanges.subscribe((giro) => {
      if (giro) {
        let val: any = this.giros.filter((item) => {
          return item.id == giro;
        })[0];

        this.registerForm.controls['nombreGiroCompany'].setValue(
          val.name as string
        );
        this.companies.nombreGiroCompany = val.name;
      }
    });
    this.registerForm.get('idRecinto')?.valueChanges.subscribe((recinto) => {
      if (recinto) {
        let val: any = this.recinto.filter((item) => {
          return item.id == recinto;
        })[0];

        this.registerForm.controls['nombreRecintoCompany'].setValue(
          val.name as string
        );
        this.companies.nombreRecintoCompany = val.name;
      }
    });
    this.registerForm.get('idRegimen')?.valueChanges.subscribe((regimen) => {
      if (regimen) {
        let val: any = this.regimen.filter((item) => {
          return item.id == regimen;
        })[0];

        this.registerForm.controls['nombreRegimenCompany'].setValue(
          val.name as string
        );
        this.companies.nombreRegimenCompany = val.name;
      }
    });
  }

  ngOnInit(): void {
    this.isupdating = false;

    this.sharedDataService.setVariable('editarCompany', false);
  }

  openModal() {
    this.sharedDataService.setVariable('editarCompany', false);
    this.imagenPrevia = '';
    this.companies = new CompaniesModel();
    this.registerForm.reset(this.companies);
    this.isupdating = false;
  }

  closeModal() {
    this.registerForm.reset();
    this.dialog.closeAll();
  }

  editar(element: any) {
    this.sharedDataService.setVariable('editarCompany', true);

    this.companies = element;
  }

  navigateTo(child: string) {
    this.util.goToPage(child);
  }

  eliminar(element: any, i: number) {
    Swal.fire({
      title: 'Eliminar Registro',
      text: 'Se dispone a eliminar la empresa ' + element.nameCompany,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.api
          .doRequest('/company/' + element.idCompany, this.companies, 'delete')
          .then((res: any) => {
            if (res.code == 200) {
              this.closeModal();
              this.utils.showSWAL(
                'EMPRESA ELIMINADA',
                'la empresa fue eliminada con éxito',
                'OK'
              );

              this.dataSource.data.splice(i, 1);
              this.dataSource = new MatTableDataSource<any>(
                this.dataSource.data
              );
            }

            if (res.code != 200) {
              this.utils.showSWAL(
                'ERROR',
                'la empresa no fue eliminado \n' + res.data,
                'OK'
              );
            }
          });
      }
    });
  }

  inicializartabla() {
    this.dataSource = new MatTableDataSource<any>();

    this.api
      .doRequest('/company/index', this.companies, 'get')
      .then((res: any) => {
        if (res.code == 200) {
          this.dataSource = new MatTableDataSource<any>(res.data.companies);

          this.dataSource.paginator = this.paginator;

          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
          this.municipios = res.data.catalogos.municipios;
          this.sharedDataService.setVariable('catalogos', res.data.catalogos);
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

  filtervaluesCompanies(event: any = '', opt: any = 1) {
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

  public onFileSelected(event: any) {
    const imagen = event.target.files[0];

    if (imagen.type.startsWith('image/')) {
      this.files.push(imagen);
    }
  }

  public borrarImagen() {
    this.imagenPrevia = '';
  }

  applyFilterCompanies(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  exportXlsx(): void {
    let printData: any = [];
    this.dataSource.data.forEach((ele: any) => {
      printData.push({
        address: ele.address,
        description: ele.description,
        email: ele.email,
        id: ele.id,
        idDeptoCompany: ele.idDeptoCompany,
        idGiroCompany: ele.idGiroCompany,
        idMuniCompany: ele.idMuniCompany,
        name: ele.name,
        nit: ele.nit,
        nrc: ele.nrc,
        number: ele.number,
        phone: ele.phone,
        social_reason: ele.social_reason,
      });
    });
  }

  resetOptions() {
    this.filtervaluesCompanies('');
    this.filtervaluesCompanies('', 1);
    this.filtervaluesCompanies('', 2);
    this.filtervaluesCompanies('', 3);
    this.filtervaluesCompanies('', 4);
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  updateUrl(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = '../../../assets/images/gallery/Sin_imagen_disponible.jpg';
  }

  fieldNames = {
    name: 'Nombre',
    nombreGiroCompany: 'Giro Actividad Económica',
    nombreMunicipioCompany: 'Municipio',
    social_reason: 'Nombre Comercial',
    description: 'Descripcion',
    nit: 'NIT',
    nrc: 'NRC',
    address: 'Direccion',
    phone: 'Telefono',
    email: 'Correo Electronico',
    mh_user: 'Usuario minesterio de hacienda',
    mh_pass: 'Contraseña minesterio de hacienda',
    mh_clave_primaria_cert: 'Clave primaria certificado',
  };

  getRequiredFields(): string {
    const fieldDescriptions: { [key: string]: string } = {
      name: 'Nombre',
      nombreGiroCompany: 'Giro Actividad Económica',
      nombreMunicipioCompany: 'Municipio',
      social_reason: 'Nombre Comercial',
      description: 'Descripcion',
      nit: 'NIT',
      nrc: 'NRC',
      address: 'Direccion',
      phone: 'Telefono',
      email: 'Correo Electronico',
      mh_user: 'Usuario minesterio de hacienda',
      mh_pass: 'Contraseña minesterio de hacienda',
      mh_clave_primaria_cert: 'Clave primaria certificado',
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

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex + 1;
  }

  ngDoCheck(): void {
    const load_page =
      this.sharedDataService.getVariableValue('load-current-page');

    if (load_page) {
      this.inicializartabla();
      this.sharedDataService.setVariable('load-current-page', false);
    }
  }
}
