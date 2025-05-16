import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Permisos } from 'src/app/core/model/permisos/permisos.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ManagePermissionComponent } from './dialogs/manage-permission/manage-permission.component';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-permissions',
  templateUrl: './permissions.component.html',
})
export class PermissionsComponent implements OnInit, OnDestroy {
  dataSource = new MatTableDataSource<Permisos>();
  displayedColumnsPermission: string[] = [
    'namePermissions',
    'descriptionPermissions',
  ];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNoPermission: any = 1;
  totalRecordsPermission: number = 0;
  paginationSizePermission: number = 10;
  paginationSizePermissionOptions: number[] = [5, 10, 25, 50, 100];
  subscribe: Subscription;
  public permissionsArrPermisions: string[] = [];

  constructor(
    private api: ApiService,
    private utils: UtilsService,
    private _data: DataService,
    public _auth: AuthService
  ) {
    this.permissionsArrPermisions = _auth.getPermissions();

    if (
      this.permissionsArrPermisions.includes('Actualizar') ||
      this.permissionsArrPermisions.includes('Eliminar')
    ) {
      this.displayedColumnsPermission.push('actions');
    }

    this.subscribe = this._data.getData().subscribe((resp) => {
      if (resp.component == 'PermissionsComponent' && resp.action == 'open') {
        this.update({}, null, false);
      }
    });
  }
  ngOnInit(): void {
    this.indexPermissions();
  }

  ngOnDestroy(): void {
    this.subscribe.unsubscribe();
  }

  indexPermissions(page: number = 0, size: number = 10) {
    if (!this.permissionsArrPermisions.includes('Index')) {
      return this.utils.showSWAL(
        'No tiene permisos para visualizar permisos',
        'Contacte al Administrador',
        'Aceptar',
        'warning'
      );
    }
    return this.api
      .getPage('/permissions/page', undefined, undefined, page, size)
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
        } else {
          this.dataSource = resp.data.pages.content;
          this.totalRecordsPermission = resp.data.pages.totalElements;
          this.dataSource.sort = this.sort;
        }
      });
  }

  update(data: Permisos, index: any, update: boolean = true) {
    const modalData = this.utils.openModal({
      title: update ? 'Actualización de Permiso' : 'Añadir Nuevo Permiso',
      botonAceptar: update ? 'Actualizar' : 'Agregar',
      componentToLoad: ManagePermissionComponent,
      callerComponent: this,
      footer: false,
      data: {
        permiso: data,
      },
    });

    modalData.afterClosed().subscribe((result) => {
      if (result.action == 'update' && result.result) {
        this.indexPermissions();
      }
    });
  }

  delete(data: Permisos, index: any) {
    this.utils
      .SWALYESNO(
        'Se eliminará "' + data.namePermissions + '"',
        'Estas seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest('/permissions/' + data.idPermissions, {}, 'delete')
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
                .showSWAL('Eliminado correctamente', '', 'Aceptar', 'success')
                .then(() => {
                  this.indexPermissions();
                });
            });
        }
      });
  }

  paginatePermission(pe: PageEvent): void {
    this.paginationSizePermission = pe.pageSize;
    this.currentPageNoPermission = pe.pageIndex;

    this.indexPermissions(
      this.currentPageNoPermission,
      this.paginationSizePermission
    );
  }
}
