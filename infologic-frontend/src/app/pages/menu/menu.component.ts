import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Menu } from 'src/app/core/model/menu.model';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { MenuDialogComponent } from './dialogs/menu-dialog/menu-dialog.component';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['../../../assets/styles/material-dashboard.css'],
})
export class MenuComponent implements OnInit, OnDestroy {
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNoMenu: any = 1;
  totalRecordsMenu: number = 0;
  paginationSizeMenu: number = 10;
  paginationSizeMenuOptions: number[] = [5, 10, 25, 50, 100];

  displayedColumns: string[] = ['nameMenu', 'descriptionMenu'];
  dataSource = new MatTableDataSource<Menu>();
  categories: any;
  subscribe: Subscription;

  public permissionsArrMenu: string[] = [];

  constructor(
    private api: ApiService,
    private utils: UtilsService,
    private _data: DataService,
    public _auth: AuthService
  ) {
    this.permissionsArrMenu = _auth.getPermissions();

    if (
      this.permissionsArrMenu.includes('Actualizar') ||
      this.permissionsArrMenu.includes('Eliminar')
    ) {
      this.displayedColumns.push('actions');
    }

    this.subscribe = this._data.getData().subscribe((resp) => {
      if (resp.component == 'MenuComponent' && resp.action == 'open') {
        this.update(null, null, false);
      }
    });
  }
  ngOnInit(): void {
    this.indexMenu();
  }

  ngOnDestroy(): void {
    if (this.subscribe) {
      this.subscribe.unsubscribe();
    }
  }

  openModal() {
    throw new Error('Method not implemented.');
  }

  indexMenu(page: number = 0, size: number = 10) {
    if (!this.permissionsArrMenu.includes('Index')) {
      return this.utils.showSWAL(
        'No tiene permisos para visualizar',
        'Contacte al Administrador',
        'Aceptar',
        'warning'
      );
    }
    return this.api
      .getPage('/menu/page', undefined, undefined, page, size)
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
        } else {
          this.categories =
            resp.data?.categories != undefined
              ? resp.data?.categories
              : this.categories;

          this.dataSource = resp.data.pages.content;
          this.totalRecordsMenu = resp.data.pages.totalElements;
          this.dataSource.sort = this.sort;
        }
      })
      .catch((error) => {
        console.error('index menu', error);
      });
  }

  update(data: any, index: any, update: boolean = true) {
    const modalData = this.utils.openModal({
      title: update ? 'Actualización de Menú' : 'Añadir Nuevo Menú',
      botonAceptar: update ? 'Actualizar' : 'Agregar',
      componentToLoad: MenuDialogComponent,
      callerComponent: this,
      footer: false,
      data: {
        menu: data,
        categories: this.categories,
      },
    });

    modalData.afterClosed().subscribe((result) => {
      if (result.action == 'update' && result.result) {
        this.indexMenu();
      }
    });
  }

  deleteMenu(data: Menu, index: any) {
    this.utils
      .SWALYESNO(
        'Se eliminará "' + data.nameMenu + '"',
        'Estas seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest('/menu/' + data.idMenu, {}, 'delete')
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
                  this.indexMenu();
                });
            });
        }
      });
  }

  paginateMenu(pe: PageEvent): void {
    this.paginationSizeMenu = pe.pageSize;
    this.currentPageNoMenu = pe.pageIndex;

    this.indexMenu(this.currentPageNoMenu, this.paginationSizeMenu);
  }
}
