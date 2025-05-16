import { BooleanInput } from '@angular/cdk/coercion';
import { SelectionModel } from '@angular/cdk/collections';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  Inject,
  ViewChild,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormGroupDirective,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSelect } from '@angular/material/select';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Category } from 'src/app/core/model/category.model';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Roles } from 'src/app/core/model/roles/roles.model';
import { User } from 'src/app/core/model/users/user.model';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { Branch } from 'src/app/pages/branch/branch.model';
import { Company } from 'src/app/pages/companies/companies.model';
import { ManageMenuComponent } from 'src/app/pages/menu/views/manage-menu/manage-menu.component';

@Component({
  selector: 'app-manage-user',
  templateUrl: './manage-user.component.html',
})
export class ManageUserComponent implements AfterViewInit {
  public userForm: FormGroup;
  @ViewChild(FormGroupDirective) formGroupDirective!: FormGroupDirective;
  public hide: boolean = true;

  dataSource = new MatTableDataSource<Roles>();
  displayedColumns: string[] = ['select', 'nameRole', 'descriptionRole'];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild('company') company!: MatSelect;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  currentPageNo: any = 1;
  totalRecords: number = 0;
  paginationSize: number = 10;
  paginationSizeOptions: number[] = [5, 10, 25, 50, 100];
  roles: Roles[] = [];
  docTypes: Category[] = [];
  docTypeSelected: number | undefined;
  public docTypeMask: string | undefined = '';

  companies: Company[] = [];
  companySelected: number | undefined;

  branches: Branch[] = [];
  branchSelected: number | undefined;

  ranges: Category[] = [];
  rangeSelected: number | undefined;
  public rolesIds: any[] = [];

  public selection = new SelectionModel<number | undefined>(true, []);
  public updating: boolean = false;
  public user: User = {};

  public isUserActive: BooleanInput = false;
  public isTestMode: BooleanInput = false;
  public origin: string = '';
  public requeridos: any = {};

  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    public dialogRef: MatDialogRef<ManageMenuComponent>,
    private formBuilder: FormBuilder,
    private api: ApiService,
    private utils: UtilsService,
    private cdr: ChangeDetectorRef,
    private auth: AuthService
  ) {
    this.user = dialogData.data.user;
    this.origin = dialogData.data.origin;
    this.requeridos = dialogData.data.requerido;

    this.userForm = this.formBuilder.group({
      idUser: new FormControl('', this.updating ? [Validators.required] : null),
      firstname: new FormControl('', [Validators.required]),
      lastname: new FormControl('', [Validators.required]),
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$'),
      ]),
      docType: new FormControl('', [Validators.required]),
      docNumber: new FormControl('', [Validators.required]),

      phone: new FormControl('', [Validators.required]),
      isActive: new FormControl(true),
      testMode: new FormControl(false),
      idCompany: new FormControl('', [Validators.required]),
      idBranch: new FormControl('', [Validators.required]),
      idPosition: new FormControl('', [Validators.required]),
      rolIds: new FormControl([], [Validators.required]),
      usuario: new FormControl(
        '',
        this.updating ? [Validators.required] : null
      ),
      carnet: new FormControl(''),
      tipo: new FormControl(''),
      password: new FormControl(
        '',
        this.updating ? [Validators.required] : null
      ),
      resetPassword: new FormControl(false),
    });

    this.rolesPage();
  }

  ngAfterViewInit(): void {
    this.company.disabled = true;
    this.cdr.detectChanges();

    if(this.requeridos != undefined && this.requeridos != null){
      Object.keys(this.requeridos).forEach((key) => {
        this.userForm.controls[key].setErrors({ required: true });
        this.userForm.get(key)?.markAsDirty();
        this.userForm.get(key)?.markAsTouched();
      });
    }

    this.user?.idUser != undefined
      ? this.setUser(this.user?.idUser, this.user?.usuario)
      : this.index();
  }
  index() {
    this.api.doRequest('/users/list', {}, 'get').then((resp: JsonResponse) => {
      if (resp.code != 200) {
        this.utils.showSWAL(
          'No encontro el Usuario seleccionado',
          '',
          'Aceptar',
          'error'
        );
      } else {
        this.roles = resp.data.roles;
        this.docTypes = resp.data.docTypes;
        this.companies = resp.data.company;
        this.branches = resp.data.branch;
        this.ranges = resp.data.cargoAdmin;

        this.companySelected = this.companies[0]?.idCompany;
      }
    });
  }

  setUser(idUser: number | undefined, username: string | null = null) {
    this.updating = true;
    this.docTypes = [];

    this.api
      .doRequest(
        username != null ? '/users/byusername' : '/users/' + idUser,
        username != null ? { usuario: username } : null,
        username != null ? 'post' : 'get'
      )
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'No encontro el Usuario seleccionado',
            '',
            'Aceptar',
            'error'
          );
        }

        this.user = resp.data.user;
        this.roles = resp.data.roles;
        this.docTypes = resp.data.docTypes;
        this.docTypeMask = this.docTypes.find(
          (mask) => mask.idCatalogo == this.user.docType
        )?.alterno;
        this.companies = [resp.data.company];
        this.branches = resp.data.branch;
        this.ranges = resp.data.cargoAdmin;

        this.docTypeSelected = resp.data.user.docType;

        resp.data.user.rolIds.length > 0 && resp.data.user.rolIds != undefined
          ? this.selection.select(resp.data.user.rolIds)
          : this.selection.clear();

        this.isUserActive = this.user.isActive;
        this.isTestMode = this.user.testMode;

        this.userForm.patchValue({
          idUser: this.user.idUser,
          firstname: this.user.firstname,
          lastname: this.user.lastname,
          email: this.user.email,
          docType: this.user.docType,
          docNumber: this.user.docNumber,

          phone: this.user.phone,
          isActive: this.user.isActive,
          testMode: this.user.testMode,
          idCompany: this.user?.idCompany,
          idBranch: this.user.idBranch,
          idPosition: this.user.idPosition,
          rolIds: this.user.rolIds,
        });

        return this.checkCheck();
      })
      .catch((error) => {
        this.dialogRef.close();
      });
  }

  rolesPage(page: number = 0, size: number = 10) {
    this.api
      .getPage('/roles/page', undefined, undefined, page, size)
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'Ha ocurrido un error',
            '',
            'Aceptar',
            'warning'
          );
        }

        this.roles = resp.data.pages.content;
        this.dataSource = new MatTableDataSource<Roles>(
          resp.data.pages.content
        );
        this.totalRecords = resp.data.pages.totalElements;
        this.dataSource.sort = this.sort;
        return this.checkCheck();
      });
  }

  update() {
    this.updating = this.userForm.controls['idUser'].value != '';

    if (this.userForm.controls['rolIds'].value.length <= 0) {
      return this.utils.showSWAL(
        'Roles vacios',
        'Al menos un Rol es requerido',
        'Aceptar',
        'warning'
      );
    }

    return this.api
      .doRequest(
        this.updating
          ? '/users/' + this.userForm.controls['idUser'].value
          : '/users',
        this.userForm.value,
        this.updating ? 'put' : 'post'
      )
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            this.updating
              ? 'No se actualizo el Usuario'
              : 'No se creo el Usuario',
            resp.data ?? '',
            'Aceptar',
            'error'
          );
        }

        this.auth.setLocalData('cnr-info', 'requerido_facturacion', {});

        return this.utils
          .showSWAL(
            this.updating
              ? 'Usuario actualizado correctamente'
              : 'Usuario creado correctamente',
            '',
            'Aceptar',
            'success'
          )
          .then(() => {
            this.dialogRef.close({
              action: 'update',
              result: true,
            });
          });
      })
      .catch((error: any) => {
        return this.utils.showSWAL(
          this.updating
            ? 'No se actualizo el Usuario'
            : 'No se creo el Usuario',
          error.error.data ?? '',
          'Aceptar',
          'error'
        );
      });
  }

  public compareWith(object1: any, object2: any) {
    return object1 && object2 && object1 === object2;
  }

  changeChk(event: MatSlideToggleChange, field: string = '') {
    this.utils
      .SWALYESNO('Cambiaras este estado', '¿Estas seguro?', 'Si, Estoy Seguro!')
      .then((resp) => {
        if (resp) {
          return this.userForm.controls[field].setValue(event.checked);
        }

        event.source.writeValue(this.userForm.controls[field].value);
      });
  }

  public maskChange(maskInp: any) {
    this.userForm.get('docNumber')!.reset();
    this.docTypeSelected = maskInp.idCatalogo;
    this.docTypeMask = maskInp.alterno;
  }

  changecheck(checked: boolean, row: any) {
    const index = this.rolesIds.findIndex((item) => item === row.idRole);

    if (checked && index <= 0) {
      this.selection.select(row.idRole);
      this.rolesIds.push(row.idRole);
    } else {
      this.selection.deselect(row.idRole);
      this.rolesIds.splice(index, 1);
    }

    this.userForm.controls['rolIds'].setValue(this.rolesIds);
  }

  isAllSelected() {
    let selectedItems: any[] = [...this.selection.selected];

    selectedItems.splice(0, 1);
    return selectedItems.length === this.dataSource.data.length;
  }

  toggleAllRows() {
    let selectedItems: any[] = [...this.selection.selected];

    selectedItems.splice(0, 1);

    if (this.isAllSelected() || selectedItems.length > 0) {
      this.selection.clear();
      this.rolesIds = [];
      this.userForm.controls['rolIds'].setValue([]);
    } else {
      this.selection.select(...this.dataSource.data.map((item) => item.idRole));
      this.rolesIds = [...this.selection.selected];
      this.userForm.controls['rolIds'].setValue(this.rolesIds);
    }
  }

  /**
   * revisa linea por linea si algun rol ya esta seleccionado y lo marca en el checkbox
   */
  checkCheck() {
    this.rolesIds = this.userForm.controls['rolIds'].value;
    this.dataSource.data.forEach((row) => {
      this.userForm.controls['rolIds'].value.forEach((select_: number) => {
        if (row.idRole === select_) {
          this.selection.select(row.idRole);
        }
      });
    });
  }

  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;

    this.rolesPage(this.currentPageNo, this.paginationSize);
  }

  compareFn(o1: any, o2: any) {
    return o1.idCatalogo === o2;
  }
}
