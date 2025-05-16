import { Component, OnDestroy, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Permisos } from 'src/app/core/model/permisos/permisos.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { Roles } from 'src/app/core/model/roles/roles.model';
import { ManageRolesComponent } from './dialogs/manage-roles/manage-roles.component';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
})
export class RolesComponent implements OnDestroy {
  dataSource = new MatTableDataSource<Permisos>();
  displayedColumns: string[] = ['nameRole', 'descriptionRole'];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNo: any = 1;
  totalRecordsRoles: number = 0;
  paginationSizeRoles: number = 10;
  paginationSizeOptionsRoles: number[] = [5, 10, 25, 50, 100];
  subscribe: Subscription;
  public permissionsArrRoles: string[] = [];

  constructor(
    private api: ApiService,
    private utils: UtilsService,
    private _data: DataService,
    public _auth: AuthService
  ) {
    this.permissionsArrRoles = _auth.getPermissions();
    //!this.indexRol(); -----Se movio al ngOnInit para que se asigne roles al ejecutar la aplicacion
    if (
      this.permissionsArrRoles.includes('Actualizar') ||
      this.permissionsArrRoles.includes('Eliminar')
    ) {
      this.displayedColumns.push('actions');
    }

    this.subscribe = this._data.getData().subscribe((resp) => {
      if (resp.component == 'RolesComponent' && resp.action == 'open') {
        this.update({}, null, false);
      }
    });
  }

  ngOnInit() {
    this.indexRol(); //* Se ejecuta en el momento adecuado
  }

  ngOnDestroy(): void {
    this.subscribe.unsubscribe();
  }

  indexRol(page: number = 0, size: number = 10) {
    if (!this.permissionsArrRoles.includes('Index')) {
      return this.utils.showSWAL(
        'No tiene permisos para visualizar Roles',
        'Contacte al Administrador',
        'Aceptar',
        'warning'
      );
    }
    return this.api
      .getPage('/roles/page', undefined, undefined, page, size)
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
        } else {
          this.dataSource = resp.data.pages.content;
          this.totalRecordsRoles = resp.data.pages.totalElements;
          this.dataSource.sort = this.sort;
        }
      });
  }

  update(data: Roles, index: any, update: boolean = true) {
    const modalData = this.utils.openModal({
      title: update ? 'Actualización de Rol' : 'Añadir Nuevo Rol',
      botonAceptar: update ? 'Actualizar' : 'Agregar',
      componentToLoad: ManageRolesComponent,
      callerComponent: this,
      footer: false,
      data: {
        permiso: data,
      },
    });

    modalData.afterClosed().subscribe((result) => {
      if (result.action == 'update' && result.result) {
        this.indexRol();
      }
    });
  }

  deleteRoles(data: Roles, index: any) {
    this.utils
      .SWALYESNO(
        'Se eliminará "' + data.nameRole + '"',
        'Estas seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest('/roles/' + data.idRole, {}, 'delete')
            .then((resp: JsonResponse) => {
              if (resp.code != 200) {
                return this.utils.showSWAL(
                  'No se elimino el registro',
                  '',
                  'Aceptar',
                  'warning'
                );
              }

              return this.utils
                .showSWAL(
                  'Rol eliminado correctamente',
                  '',
                  'Aceptar',
                  'success'
                )
                .then(() => {
                  this.indexRol();
                });
            })
            .catch((error) => {
              console.error('Error eliminando rol', error);
              this.utils.showSWAL(
                'Ha ocurrido un error',
                '',
                'Aceptar',
                'warning'
              );
            });
        }
      });
  }

  paginate(pe: PageEvent): void {
    this.paginationSizeRoles = pe.pageSize;
    this.currentPageNo = pe.pageIndex;

    this.indexRol(this.currentPageNo, this.paginationSizeRoles);
  }
}
