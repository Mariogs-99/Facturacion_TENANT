import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Producto } from 'src/app/core/model/producto/producto.model';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ManageProductoComponent } from './dialogs/manage-producto/manage-producto.component';
import { MatDialog } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-producto',
  templateUrl: './producto.component.html',
})
export class ProductoComponent implements OnInit {
  public productoForm: FormGroup;

  dataSource = new MatTableDataSource<Producto>();
  displayedColumns: string[] = ['clasificacion', 'codigoProducto', 'nombre', 'descripcion', 'tipo', 'precio', 'actions'];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNo: number = 1;
  totalRecords: number = 0;
  paginationSize: number = 10;
  paginationSizeOptions: number[] = [10, 25, 50, 100];
  subscribe: Subscription = new Subscription();
  public permissionsArrUser: string[] = [];

  constructor(
    private api: ApiService,
    private utils: UtilsService,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private auth: AuthService
  ) {
    this.permissionsArrUser = this.auth.getPermissions();

    this.productoForm = this.formBuilder.group({
      codigoProducto: new FormControl('', [Validators.required]),
      nombre: new FormControl('', [Validators.required]),
      descripcion: new FormControl(''),
      precio: new FormControl('', [Validators.required]),
      tipo: new FormControl(''),
      total: new FormControl(''),
    });
  }

  ngOnInit(): void {
    this.indexProducto();
  }

  indexProducto(page: number = 0, size: number = 10) {
    this.api
      .getPage('/productos/page', '', '', page, size)
      .then((resp: any) => {
        console.log("Respuesta de la API:", resp); // 🔍 Verificar respuesta
  
        if (!resp.pages || !resp.pages.content) {
          this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
          return;
        }
  
        // 🔥 Mapear los datos para cambiar `codigo_producto` a `codigoProducto`
        this.dataSource.data = resp.pages.content.map((producto: any) => ({
          clasificacion: producto.clasificacion,
          idProducto: producto.idProducto, 
          codigoProducto: producto.codigo_producto,
          descripcion: producto.descripcion,
          precio: producto.precio,
          nombre: producto.nombre,
          tipo: producto.tipo
        }));
  
        this.totalRecords = resp.pages.totalElements;
        this.dataSource.sort = this.sort;
      })
      .catch((error) => {
        console.error("Error en la API:", error);
        this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
      });
  }
  

  agregarProducto() {
    const nuevoProducto: Omit<Producto, 'idProducto'> = {
      clasificacion: '',
      codigoProducto: '',
      nombre: '',
      descripcion: '',
      precio: 0,
      tipo: ''
    };

    this.update(nuevoProducto as Producto); // Casting para evitar problemas de tipado
  }
  
  

  update(data: Producto | null) {
    const modalRef = this.dialog.open(ManageProductoComponent, {
      width: '600px',
      data: data ? { producto: { ...data } } : { producto: {} }
    });

    modalRef.afterClosed().subscribe(result => {
      if (result && result.action === 'update') {
        this.indexProducto();
      }
    });
  }

  delete(data: Producto) {
    this.utils
      .SWALYESNO(
        `Se eliminará "${data.nombre}"`,
        '¿Estás seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest(`/productos/${data.idProducto}`, {}, 'delete')
            .then((resp: JsonResponse) => {
              if (resp.code !== 200) {
                return this.utils.showSWAL(
                  'No se eliminó el producto',
                  '',
                  'Aceptar',
                  'warning'
                );
              }

              return this.utils
                .showSWAL(
                  'Producto eliminado correctamente',
                  '',
                  'Aceptar',
                  'success'
                )
                .then(() => {
                  this.indexProducto();
                });
            });
        }
      });
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;
    this.indexProducto(this.currentPageNo, this.paginationSize);
  }
}
