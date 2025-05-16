import { Component, Inject, OnDestroy, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { DataService } from 'src/app/core/services/data.service';
import { ProviderService } from 'src/app/core/services/provider.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-modal-clientes',
  templateUrl: './modal-clientes.component.html',
  styleUrls: ['../ventas.component.css'],
})
export class ModalClientesComponent implements OnDestroy {
  subscription: Subscription;

  foods = [
    { value: 'steak-0', name: 'Steak' },
    { value: 'pizza-1', name: 'Pizza' },
    { value: 'tacos-2', name: 'Tacos' },
  ];

  clickedRows = new Set<any>();
  selectedRowIndex = -1;

  displayedColumns: string[] = ['NIT', 'idpersona', 'nombre', 'decargo'];
  dataForm: FormGroup;
  dataSource!: MatTableDataSource<any>;
  currentPageNo: number = 1;
  totalRecords: number = 100;
  paginationSize: number = 5;
  disableButtonTabla = true;
  sortBy: string = '';
  filterByColumn: string = '';
  Filtercolumn: string = '';
  inputFilter: string = '';
  paginationSizeOptions: number[] = [5, 10, 25, 50, 100, 200];
  selectedItem: any;
  currentSort: Sort = { active: 'id', direction: 'asc' };
  @ViewChild(MatSort)
  sort!: MatSort;
  filter: any = {};
  private filters: any = {
    NIT: '',
    idpersona: '',
    nombre: '',
    decargo: '',
  };

  apiurl!: string;
  constructor(
    public utils: UtilsService,
    public provider: ProviderService,
    private sharedDataService: SharedDataService,
    private formBuider: FormBuilder,
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public inputData: any,
    private _data: DataService
  ) {
    this.dataForm = this.formBuider.group({
      marcacionFilter: new FormControl(''),
      nombre: new FormControl(''),
    });

    /**
     * subscribirse al filtro
     */

    this.subscription = this._data.getData().subscribe((filter: any) => {
      this.filter = this._data
        .headerTableProLocal(filter, this.filters, 'filtroBusquedaPersonas')
        .then((resp: any) => {
          this.filter = resp;
        });
    });
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex + 1;
  }
  inputChange(event: Event) {
    this.inputFilter = (event.target as HTMLInputElement).value;
  }
  applyFilterByColumn(column: string) {
    this.Filtercolumn = column;
    this.currentPageNo = 1;
    this.paginationSize = 5;
    this.filterByColumn = this.inputFilter;
  }
  sortData(column: string) {
    const isDesc =
      this.currentSort.active === column &&
      this.currentSort.direction === 'desc';
    this.currentSort = { active: column, direction: isDesc ? 'asc' : 'desc' };
    this.sortBy = `${this.currentSort.active}:${this.currentSort.direction}`;
    if (this.currentSort.direction === 'asc') {
      this.sort.start = this.currentSort.direction;
      this.sort.direction = this.currentSort.direction;
      this.sort.active = this.currentSort.active;
    }
    if (this.currentSort.direction === 'desc') {
      this.sort.start = this.currentSort.direction;
      this.sort.direction = this.currentSort.direction;
      this.sort.active = this.currentSort.active;
    }
  }

  selectItem(id: number) {
    this.selectedRowIndex = id;
    let index: number = this.dataSource.data.findIndex((item) => {
      return item.id === id;
    });
    if (index >= 0) {
      this.selectedItem = this.dataSource.data[index];
    }
  }
  keyDown(event: KeyboardEvent) {
    if (event.code === 'ArrowDown') {
      let index: number = this.dataSource.data.findIndex((item) => {
        return item.id === this.selectedItem?.id;
      });
      if (index >= 0) {
        index++;

        if (index >= this.dataSource.data.length) {
          index = 0;
        }
        this.selectedItem = this.dataSource.data[index];
      }
    } else if (event.code === 'ArrowUp') {
      let index: number = this.dataSource.data.findIndex((item) => {
        return item.id === this.selectedItem?.id;
      });
      if (index >= 0) {
        index--;

        if (index < 0) {
          index = this.dataSource.data.length - 1;
        }
        this.selectedItem = this.dataSource.data[index];
      }
    }
  }

  selectItemAndClose(id: number | undefined) {
    if (id !== undefined) {
      this._data.sendMessage({
        from: 'ModalClientesComponent',
        data: this.selectedItem,
      });
      this.selectItem(id);
      this.sharedDataService.setVariable('Persona-busqueda', this.selectedItem);

      if (this.inputData.data.mensaje.data) {
        this._data.sendMessage({
          from: 'Persona-busqueda',
          data: this.selectedItem,
        });
      }
      this.dialogRef.close(this.selectedItem);
    }
  }
}
