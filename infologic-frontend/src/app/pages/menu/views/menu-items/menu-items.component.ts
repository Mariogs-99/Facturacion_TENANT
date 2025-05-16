import { Component, Input, OnDestroy, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { PageResponse } from 'src/app/core/model/PageResponse.model';
import { MenuItems } from 'src/app/core/model/menuItems.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-menu-items',
  templateUrl: './menu-items.component.html',
})
export class MenuItemsComponent implements OnDestroy {
  @Input() data: any;
  subscribe!: Subscription;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNo: any = 1;
  totalRecords: number = 0;
  paginationSize: number = 5;
  paginationSizeOptions: number[] = [5, 10, 25, 50, 100];

  displayedColumns: string[] = [
    'nameMenuItems',
    'type',
    'uriMenuItems',
    'actions',
  ];
  dataSource = new MatTableDataSource<MenuItems>();
  public hide: boolean = false;
  public updating: boolean = false;
  public idMenu: number | undefined;

  constructor(
    private _data: DataService,
    public dialogRef: MatDialogRef<MenuItemsComponent>,
    private api: ApiService,
    private utils: UtilsService
  ) {
    this.subscribe = this._data.getData().subscribe((resp: any) => {
      if (resp.from == 'UpdateMenuComponent') {
        this.idMenu = resp.data.menu.idMenu;
        this.setMenuItems(resp.data.pages);
      }

      if (resp.from == 'ManageMenuItemsComponent' && resp.action == 'cancel') {
        this.hide = false;
        this.updating = false;
      }

      if (
        (resp.from == 'ManageMenuItemsComponent' && resp.action == 'update') ||
        resp.action == 'create'
      ) {
        this.idMenu = resp.data;
        this.getPage(this.currentPageNo, this.paginationSize);
      }
    });
  }
  ngOnDestroy(): void {
    this.subscribe.unsubscribe();
  }

  delete(data: MenuItems, index: any) {
    this.utils
      .SWALYESNO(
        'Se eliminará "' + data.nameMenuItems + '"',
        'Estas seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest('/menu-items/' + data.idMenuItems, {}, 'delete')
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
                  this.idMenu = data.idMenu;
                  this.getPage(this.currentPageNo, this.paginationSize);
                });
            });
        }
      });
  }

  create() {
    this._data.sendMessage({
      from: 'MenuItemsComponent',
      action: 'open',
    });
  }

  index(idMenu: number | undefined) {
    this.api.doRequest('/menu/' + idMenu, {}, 'get').then((resp) => {
      if (resp.code != 200) {
        this.utils.showSWAL(
          'No se pudo completar la transaccion',
          '',
          'Aceptar',
          'error'
        );
      } else {
        this.hide = false;
        this.updating = false;
        this.dataSource = new MatTableDataSource<MenuItems>(
          resp.data.menuItems
        );
        this.totalRecords = resp.data.menuItems.length;
        this.dataSource.sort = this.sort;
      }
    });
  }

  update(item: any, index: any) {
    this.updating = true;
    this.hide = true;

    this._data.sendMessage({
      from: 'MenuItemsComponent',
      action: 'update',
      data: item,
    });
  }

  setMenuItems(menuItems: PageResponse) {
    this.dataSource = new MatTableDataSource<MenuItems>(menuItems.content);
    this.totalRecords = menuItems.totalElements;
    this.dataSource.sort = this.sort;
  }

  getPage(page: number = 0, size: number = 5) {
    this.hide = false;
    this.updating = false;
    this.api
      .getPage(
        '/menu-items/' + this.idMenu + '/page',
        undefined,
        undefined,
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
      })
      .catch((error) => {
        console.error('index menu', error);
      });
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;

    this.getPage(this.currentPageNo, this.paginationSize);
  }
}
