import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ApiService } from 'src/app/core/services/api.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-modal-ventas',
  templateUrl: './modal-ventas.component.html',
  styleUrls: ['../ventas.component.css'],
})
export class ModalVentasComponent {
  dataSourceSaved!: MatTableDataSource<any>;
  @ViewChild('branchPaginator') branchPaginator!: MatPaginator;

  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public util: UtilsService,
    private sharedDataService: SharedDataService,
     ) {}

  displayedColumnsSaved: string[] = [
    'acciones',
    'fecha',
    'nombre',
    'numeroDTE',
    'codigoGeneracion',
    'totalTransaccion',
  ];
  selectedData = {};
  selectedRows = <any>[];
  arrayData = <any>[];
  totalRecords: number = 0;
  totalRecordsSaved: number = 0;
  paginationSize: number = 10;
  paginationSizeOptions: number[] = [10, 25, 50, 100];

  currentPageNo: any = 1;

  ngOnInit(): void {
    this.cargar_datatable();
  }

  transacciones = <any>[];

  cliente: any = {};
  onCheckBoxChange(event: any, row: any) {
    this.selectedData = {};
    let tipoDTE = this.sharedDataService.getVariableValue('tipoDTE');

    if (row.checked) {
      this.transacciones.push(row);

      this.cliente = row.cliente;

      
      

      this.selectedRows.push(row.codigoGeneracion);

      this.selectedData = {
        monto: row.totalTransaccion,
        totalTransaccion: row.totalTransaccion+row.totalIva-row.ivaRetenido,
        ivaRetenido: row.ivaRetenido,
        fecha: this.timestampToDate(row.createdAt),
        fechaEmision: this.formatDate(this.timestampToDate(row.createdAt)),
        nombre: row.cliente.nombre,
        items: row.items,
        tipoDte: row.tipoDTE ?? 'FCF',
        codigoGeneracion: row.codigoGeneracion ?? '3455',
        serieDocumento: this.getSerieDTE(row.numeroDTE) ?? 'DTE-01-ASDFDR',
        nroPreimpreso: this.getNumeroPreimpreso(row.numeroDTE) ?? '789',
        tipoGeneracion: '2',
        subTotal: row.totalTransaccion,
        iva: row.totalIva,
        idDocumentoAsociado: row.idTransaccion ?? '23',
        nroDocumentoHijo: row.numeroDTE ?? '123',
        nroDocumento: row.codigoGeneracion ?? '123',
        idTransaccionHija: row.idTransaccion ?? '567',
      };
      this.arrayData.push(this.selectedData);
    } else {
      this.selectedRows = this.selectedRows.filter(
        (codigoGeneracion: any) => codigoGeneracion !== row.idTransaccion
      );
      this.transacciones = this.transacciones.filter(
        (transaccion: any) => transaccion.idTransaccion !== row.idTransaccion
      );
    }

    
    
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex + 1;
    
  }

  opcion = '1';
  items: Array<any> = [];
  customers: Array<any> = [];
  compra: boolean = false;

  filteredOptions: any = {
    clientes: [],
    productos: [],
    tipoIdentificacion: [],
    formaDePago: [],
    actividad_economica: [],
    impuestos: [],
  };
  catalogos: any = {
    actividad_economica: [],
    tipoPersona: [],
    formaDePago: [],
  };
  tipoDocumentoList: any = [];
  public invalidacionTipo: any;
  dataSource = new MatTableDataSource<any>();
  transactions_array: Array<any> = [];

  cargar_datatable() {
    

    let tipoDTE = this.sharedDataService.getVariableValue('tipoDTE');
    let cliente = this.sharedDataService.getVariableValue('cliente');
    let id_doc_transaction_type = this.sharedDataService.getVariableValue(
      'id_doc_transaction_type'
    );

    
    
    this.dataSourceSaved = new MatTableDataSource<any>();

    let url = '';

    if (cliente) {
      url =
        tipoDTE == '38'
          ? '/transacciones/ventasCCF/' + cliente.nit
          : '/transacciones/ventasFCF/' + cliente.nit;
    }

    if (id_doc_transaction_type == '40') {
      

      url = '/transacciones/ventas/iva-retenido/';
    }

    if (id_doc_transaction_type == '41') {
      

      url = '/transacciones/ventas/';
    }
    

    this.opcion == '1' ? (this.compra = false) : (this.compra = true);
    this.api.doRequest(url, {}, 'get').then((res: any) => {
      
      this.items = res.data.items;
      this.customers = res.data.clientes;
      this.filteredOptions.clientes = res.data.clientes;
      this.filteredOptions.productos = res.data.items;
      this.filteredOptions.tipoIdentificacion =
        res.data.catalogos.tipoIdentificacion;
      this.filteredOptions.formaDePago = res.data.catalogos.formaDePago;
      this.filteredOptions.actividad_economica =
        res.data.catalogos.actividad_economica;
      this.filteredOptions.impuestos = res.data.catalogos.impuestos;
      this.filteredOptions.proveedors = res.data.proveedores;
      this.catalogos = res.data.catalogos;
      

      this.sharedDataService.setVariable('catalogos', res.data.catalogos);
      this.tipoDocumentoList = res.data.catalogos.tipo_rango;
      
      
      this.invalidacionTipo = res.data.catalogos.tipoAnulacion;
      
      
      
      
      
      this.dataSourceSaved = new MatTableDataSource<any>(res.data.transactions);
      this.dataSourceSaved.paginator = this.branchPaginator;

      

      this.transactions_array = res.data.transaccions;

      
      
      if (this.opcion) {
        interface TipoDocumento {
          id: number;
          name: string;
        }
      
      }
      
    });
  }

  formatTimestamp(timestamp: any) {
    
    let date = new Date(timestamp);

    
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    let day = date.getDate().toString().padStart(2, '0');
    let hours = date.getHours().toString().padStart(2, '0');
    let minutes = date.getMinutes().toString().padStart(2, '0');
    let seconds = date.getSeconds().toString().padStart(2, '0');

    
    let formattedDate = day + '/' + month + '/' + year;
    let formattedTime = hours + ':' + minutes + ':' + seconds;

    
    return formattedDate + ' ' + formattedTime;
  }

  formattedDate(timestamp: any) {
    
    let date = new Date(timestamp);

    
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    let day = date.getDate().toString().padStart(2, '0');
    let hours = date.getHours().toString().padStart(2, '0');
    let minutes = date.getMinutes().toString().padStart(2, '0');
    let seconds = date.getSeconds().toString().padStart(2, '0');

    
    let formattedDate = day + '/' + month + '/' + year;
    let formattedTime = hours + ':' + minutes + ':' + seconds;

    
    return formattedDate;
  }

  formattedTime(timestamp: any) {
    
    let date = new Date(timestamp);

    
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    let day = date.getDate().toString().padStart(2, '0');
    let hours = date.getHours().toString().padStart(2, '0');
    let minutes = date.getMinutes().toString().padStart(2, '0');
    let seconds = date.getSeconds().toString().padStart(2, '0');

    
    let formattedDate = day + '/' + month + '/' + year;
    let formattedTime = hours + ':' + minutes + ':' + seconds;

    
    return formattedTime;
  }

  agregar() {
    
    this.sharedDataService.setVariable('arrayData', this.arrayData);
    this.sharedDataService.setVariable('ventasCCF', this.transacciones);
    this.sharedDataService.setVariable('clienteCR', this.cliente);
    this.sharedDataService.setVariable('load-current-page', true);

    
    let items = [];

    this.arrayData.forEach((obj: any) => {
      obj.items.map((item: any) => {
        item.nombre2 = obj.codigoGeneracion + ' ' + item.nombre;
        items.push(item);
      });
    });

    

    
    let itemsUnicos = <any>[];

    
    this.arrayData.forEach((objeto: any) => {
      
      objeto.items.forEach((item: any) => {
        
        const existeItem = itemsUnicos.some(
          (itemUnico: any) => itemUnico.nombre === item.nombre
        );
        
        if (!existeItem) {
          itemsUnicos.push(item);
        }
      });
    });

    

    
    
    this.sharedDataService.setVariable('itemsUnicos', itemsUnicos);

 

    this.closeModal();
  }

  closeModal() {
    this.dialog.closeAll();
  }
  timestampToDate(timestamp: number): Date {
    return new Date(timestamp);
  }

  getSerieDTE(inputString: string): string {
    return inputString.substring(0, 15);
  }

  getNumeroPreimpreso(inputString: string): number {
    const lastFifteen = inputString.substring(inputString.length - 15);
    return parseInt(lastFifteen, 10);
  }

  formatDate(timestamp: any) {
    
    let date = new Date(timestamp);

    
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    let day = date.getDate().toString().padStart(2, '0');
    let hours = date.getHours().toString().padStart(2, '0');
    let minutes = date.getMinutes().toString().padStart(2, '0');
    let seconds = date.getSeconds().toString().padStart(2, '0');

    
    
    let formattedDate = year + '-' + month + '-' + day;

    let formattedTime = hours + ':' + minutes + ':' + seconds;
    
    return formattedDate;
  }

  filterColumns: string[] = [
    'customer',
    'numeroDTE',
    'type_transaction',
    'date',
    'codigoGeneracion',
    'status',
    'totalTransaccion',
  ]; 
  applyCustomFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.toUpperCase();
    this.dataSourceSaved.filterPredicate = (data: any, filter: string) => {
      return this.filterColumns.some((column) => {
        if (column === 'customer') {
          const value =
            (data.cliente &&
              data.cliente.nombre &&
              data.cliente.nombre.toLowerCase().trim()) ||
            '';
          return value.includes(filter.toLowerCase());
        } else if (column === 'date') {
          
          const formattedDate = this.util.fecha(data.createdAt);
          return formattedDate.includes(filter.toLowerCase());
        } else if (column === 'type_transaction') {
          const tipoDte = this.util.tipo_DTE(data.tipoDTE).toLowerCase();
          return tipoDte.includes(filter.toLowerCase());
        } else if (column === 'totalTransaccion') {
          
          const value = data.totalTransaccion
            ? data.totalTransaccion.toString()
            : '';
          return value.includes(filter);
        } else {
          const value =
            (data[column] && data[column].toLowerCase().trim()) || '';
          return value.includes(filter.toLowerCase());
        }
      });
    };
    this.dataSourceSaved.filter = filterValue;
  }
}
