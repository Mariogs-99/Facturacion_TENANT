// cliente.component.ts
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Cliente } from 'src/app/core/model/cliente/cliente.model';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ManageClienteComponent } from './dialogs/manage-cliente/manage-cliente.component';
import { MatDialog } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { ResetPasswordComponent } from '../autentificacion/reset-password/reset-password.component';
@Component({
  selector: 'app-cliente',
  templateUrl: './cliente.component.html',
})
export class ClienteComponent implements OnInit {
  public clienteForm: FormGroup;
  public usuarioActual: string = '';
  dataSource = new MatTableDataSource<Cliente>();
  displayedColumns: string[] = ['nombreCliente', 'tipoDocumento', 'numeroDocumento', 'nitCustomer', 'telefono', 'emailCustomer', 'estadoFiscal', 'actions'];
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
    this.usuarioActual = this.auth.getNombre();

    this.clienteForm = this.formBuilder.group({
      nombreCliente: new FormControl('', [Validators.required]),
      tipoDocumento: new FormControl('', [Validators.required]),
      numeroDocumento: new FormControl('', [Validators.required]),
      nitCustomer: new FormControl('', [Validators.required]),
      telefono: new FormControl('', [Validators.required]),
      emailCustomer: new FormControl('', [Validators.required, Validators.email]),
    });
  }

  ngOnInit(): void {
    this.indexCliente();
    this.usuarioActual = this.auth.getNombre();
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

  indexCliente(page: number = 0, size: number = 10) {
    this.api
      .getPage('/clientes/page', '', '', page, size)
      .then((resp: any) => {
        if (!resp.pages || !resp.pages.content) {
          this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
          return;
        }

        this.dataSource.data = resp.pages.content.map((cliente: any) => ({
          idCliente: cliente.idCliente,
          nombreCliente: cliente.nombre,
          tipoDocumento: cliente.tipoDocumento,
          numeroDocumento: cliente.numeroDocumento,
          nitCustomer: cliente.nit,
          telefono: cliente.telefono,
          emailCustomer: cliente.email,
          estadoFiscal: `${cliente.esConsumidorFinal ? 'Consumidor Final' : ''} ${cliente.esExtranjero ? 'Extranjero' : ''} ${cliente.esGobierno ? 'Gobierno' : ''} ${cliente.esGranContribuyente ? 'Gran Contribuyente' : ''}`.trim()
        }));

        this.totalRecords = resp.pages.totalElements;
        this.dataSource.sort = this.sort;
      })
      .catch((error) => {
        console.error("Error en la API:", error);
        this.utils.showSWAL('Ha ocurrido un error', '', 'Aceptar', 'warning');
      });
  }

  agregarCliente() {
    const nuevoCliente: Omit<Cliente, 'idCliente'> = {
      nombreCliente: '',
      tipoDocumento: '',
      numeroDocumento: '',
      nitCustomer: '',
      telefono: '',
      email: ''
    };

    this.update(nuevoCliente as Cliente);
  }

  update(data: Cliente | null) {
    const modalRef = this.dialog.open(ManageClienteComponent, {
      width: '600px',
      data: data ? { cliente: { ...data } } : { cliente: {} }
    });

    modalRef.afterClosed().subscribe(result => {
      if (result && result.action === 'update') {
        this.indexCliente();
      }
    });
  }

  delete(data: Cliente) {
    this.utils
      .SWALYESNO(
        `Se eliminará "${data.nombreCliente}"`,
        '¿Estás seguro?',
        'Eliminar',
        'Cancelar'
      )
      .then((resp: boolean) => {
        if (resp) {
          this.api
            .doRequest(`/clientes/${data.idCliente}`, {}, 'delete')
            .then((resp: JsonResponse) => {
              if (resp.code !== 200) {
                return this.utils.showSWAL(
                  'No se eliminó el cliente',
                  '',
                  'Aceptar',
                  'warning'
                );
              }

              return this.utils
                .showSWAL(
                  'Cliente eliminado correctamente',
                  '',
                  'Aceptar',
                  'success'
                )
                .then(() => {
                  this.indexCliente();
                });
            });
        }
      });
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;
    this.indexCliente(this.currentPageNo, this.paginationSize);
  }
}
