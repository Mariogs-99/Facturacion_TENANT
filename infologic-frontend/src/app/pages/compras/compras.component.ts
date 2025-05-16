import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { Category } from 'src/app/core/model/category.model';
import { Compras } from 'src/app/core/model/compras/compras.model';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ManageComprasComponent } from './views/manage-compras/manage-compras.component';

@Component({
  selector: 'app-compras',
  templateUrl: './compras.component.html',
})
export class ComprasComponent implements OnInit {
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNo: any = 1;
  totalRecords: number = 0;
  paginationSize: number = 10;
  paginationSizeOptions: number[] = [5, 10, 25, 50, 100];

  displayedColumns: string[] = [
    'numeroControl',
    'fechaEmision',
    'totalOperacion',
    'actions',
  ];
  dataSource = new MatTableDataSource<Compras>();

  subscribe: Subscription;

  public documentosTipo: Category[] = [];
  public operacionTipo: Category[] = [];

  constructor(
    private api: ApiService,
    private utils: UtilsService,
    private _data: DataService
  ) {
    this.subscribe = this._data.getData().subscribe((resp) => {
      if (resp.component == 'ComprasComponent' && resp.action == 'open') {
        this.update({}, null, false);
      }
    });
  }
  ngOnInit(): void {
    this.indexCompras();
  }

  indexCompras(page: number = 0, size: number = 10) {
    this.api
      .getPage('/compras/page', undefined, undefined, page, size)
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
        } else {
          this.operacionTipo = resp.data.operacion_tipo;
          this.documentosTipo = resp.data.documento_tipo;
          this.dataSource = resp.data.pages.content;
          this.totalRecords = resp.data.pages.totalElements;
          this.dataSource.sort = this.sort;
        }
      });
  }

  update(data: Compras, index: any, update: boolean = true) {
    const modalData = this.utils.openModal({
      title: update ? 'Actualización de Compra' : 'Añadir Nueva Compra',
      botonAceptar: update ? 'Actualizar' : 'Agregar',
      componentToLoad: ManageComprasComponent,
      callerComponent: this,
      footer: false,
      data: {
        compras: data,
        operacion_tipo: this.operacionTipo,
        documento_tipo: this.documentosTipo,
      },
    });

    modalData.afterClosed().subscribe((result) => {
      if (result.action == 'update' && result.result) {
        this.indexCompras();
      }
    });
  }

  delete(data: Compras, index: any) {
    this.utils
      .SWALYESNO(
        'Se eliminará "' + data.numeroControl + '"',
        'Estas seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest('/compras/' + data.idCompra, {}, 'delete')
            .then((resp: JsonResponse) => {
              if (resp.code != 200) {
                return this.utils.showSWAL(
                  'No se eliminó el registro de compra',
                  '',
                  'Aceptar',
                  'warning'
                );
              }
              return this.utils
                .showSWAL(
                  'Compra eliminada correctamente',
                  '',
                  'Aceptar',
                  'success'
                )
                .then(() => {
                  this.indexCompras();
                });
            })
            .catch((error) => {
              console.error('Error al eliminar la compra:', error);
            });
        }
      })
      .catch((error) => {
        console.error('Error en la confirmación de eliminación:', error);
      });
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;

    this.indexCompras(this.currentPageNo, this.paginationSize);
  }
}
