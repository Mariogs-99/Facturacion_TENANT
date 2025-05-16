import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { User } from 'src/app/core/model/users/user.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ManageUserComponent } from './dialogs/manage-user/manage-user.component';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { ResetPasswordComponent } from '../autentificacion/reset-password/reset-password.component';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
})
export class UserComponent implements OnInit {
  public userForm: FormGroup;

  dataSource = new MatTableDataSource<User>();

  displayedColumns: string[] = ['email', 'usuario', 'carnet', 'isActive'];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNo: any = 1;
  totalRecords: number = 0;
  paginationSize: number = 10;
  paginationSizeOptions: number[] = [10, 25, 50, 100];
  subscribe: Subscription;
  public permissionsArrUser: string[] = [];
  filter: any = { filterBy: '', sortBy: '' };
  userFilter: any = { usuario: '', email: '', carnet: '' };

  constructor(
    private api: ApiService,
    private utils: UtilsService,
    private _data: DataService,
    private formBuilder: FormBuilder,
    public _auth: AuthService
  ) {
    this.permissionsArrUser = _auth.getPermissions();

    if (
      this.permissionsArrUser.includes('Actualizar') ||
      this.permissionsArrUser.includes('Eliminar')
    ) {
      this.displayedColumns.push('actions');
    }

    this.userForm = this.formBuilder.group({
      idUser: new FormControl('', [Validators.required]),
      firstname: new FormControl('', [Validators.required]),
      lastname: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      docType: new FormControl('', [Validators.required]),
      docNumber: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      phone: new FormControl('', [Validators.required]),
      isActive: new FormControl('', [Validators.required]),
      testMode: new FormControl('', [Validators.required]),
      idCompany: new FormControl('', [Validators.required]),
      idBranch: new FormControl('', [Validators.required]),
      idPosition: new FormControl('', [Validators.required]),
      rolIds: new FormControl([], [Validators.required]),
    });

    this.subscribe = this._data.getData().subscribe((resp) => {
      if (resp.component == 'UserComponent' && resp.action == 'open') {
        this.update({}, null, false);
      }

      if (resp.from === 'UsercomponentTable') {
        this._data
          .headerTableProLocal(resp, this.userFilter, 'UsercomponentTable')
          .then((response) => {
            if (resp.events === 'filter') {
              this.filter.filterBy = response.filterBy;
            }

            if (resp.value === 'none') {
              this.filter.sortBy = '';
            } else if (response.sortBy !== '') {
              this.filter.sortBy = response.sortBy;
            }

            this.indexUser(0, this.paginationSize);
          });
      }
    });
  }
  ngOnInit(): void {
    this.indexUser();
  }

  indexUser(page: number = 0, size: number = 10) {
    if (!this.permissionsArrUser.includes('Index')) {
      return this.utils.showSWAL(
        'No tiene permisos para visualizar',
        'Contacte al Administrador',
        'Aceptar',
        'warning'
      );
    }

    return this.api
      .getPage(
        '/users/page',
        this.filter.filterBy,
        this.filter.sortBy,
        page,
        size
      )
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
        } else {
          this.dataSource = resp.data.pages.content;
          this.totalRecords = resp.data.pages.totalElements;
          this.dataSource.sort = this.sort;
        }
      });
  }

  update(data: User, index: any, update: boolean = true) {
    const modalData = this.utils.openModal({
      title: update ? 'Actualización de Usuario' : 'Añadir Nuevo Usuario',
      botonAceptar: update ? 'Actualizar' : 'Agregar',
      componentToLoad: ManageUserComponent,
      callerComponent: this,
      footer: false,
      data: {
        user: data,
      },
    });

    modalData.afterClosed().subscribe((result) => {
      if (result.action == 'update' && result.result) {
        this.indexUser();
      }
    });
  }

  delete(data: User, index: any) {
    this.utils
      .SWALYESNO(
        'Se eliminará "' + data.firstname + '"',
        'Estas seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest('/users/' + data.idUser, {}, 'delete')
            .then((resp: JsonResponse) => {
              if (resp.code != 200) {
                return this.utils.showSWAL(
                  'No se elimino el usuario',
                  '',
                  'Aceptar',
                  'warning'
                );
              }

              return this.utils
                .showSWAL(
                  'Usuario eliminado correctamente',
                  '',
                  'Aceptar',
                  'success'
                )
                .then(() => {
                  this.indexUser();
                });
            });
        }
      });
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;

    this.indexUser(this.currentPageNo, this.paginationSize);
  }

  changeChk(event: MatSlideToggleChange, field: string, row: User) {
    this.userForm.patchValue(row);
    this.utils
      .SWALYESNO('Cambiaras este estado', '¿Estas seguro?', 'Si, Estoy Seguro!')
      .then((resp) => {
        if (resp) {
          this.userForm.controls[field].setValue(event.checked);

          return this.api
            .doRequest(
              '/users/status/' + row.idUser,
              this.userForm.value,
              'put'
            )
            .then((resp: JsonResponse) => {
              if (resp.code != 200) {
                return this.utils.showSWAL(
                  'No se actualizo usuario',
                  '',
                  'Aceptar',
                  'error'
                );
              }
              return this.utils
                .showSWAL(
                  'Se actualizo usuario correctamente',
                  '',
                  'Aceptar',
                  'success'
                )
                .then(() => {
                  this.indexUser();
                });
            });
        }

        return event.source.writeValue(this.userForm.controls[field].value);
      });
  }

  cambiarPassword() {
    this.utils.openModal(
      {
        title: 'Actualizacion de contraseña',
        botonAceptar: 'Aceptar',
        componentToLoad: ResetPasswordComponent,
        callerComponent: this,
        data: {
          from: 'root',
        },
        footer: false,
      },
      '40%',
      '50%',
      '100%',
      '100%'
    );
  }

  descargarPdf(tipo: string, activo: number, nombre: string) {
    this.api
      .descargarDocumento(`/report/resporteUsuarios/${activo}/${tipo}`, nombre)
      .then((resp: any) => {
        if (!resp) {
          return this.utils.showSWAL(
            'No se ha encontrado información con los parámetros seleccionados',
            'No se pudo descargar el documento',
            'Aceptar',
            'warning'
          );
        }

        return this.utils.showSWAL(
          'Descargando documento...',
          'Estará listo en breve',
          'Aceptar',
          'success'
        );
      });
  }
}
