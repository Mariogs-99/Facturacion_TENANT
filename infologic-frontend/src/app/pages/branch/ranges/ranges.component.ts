import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { Rango } from '../../../core/model/range.model';
import { ApiService } from '../../../core/services/api.service';
import { UtilsService } from '../../../core/services/utils.service';

import Swal from 'sweetalert2';

@Component({
  selector: 'app-ranges',
  templateUrl: './ranges.component.html',
})
export class RangesComponent implements OnInit {
  @Input() item: any;

  public tipoRango: any[] = [];
  public rangos: Rango[] = [];
  public rango: Rango = {};
  public isEditing: boolean = false;
  public update: boolean = false;
  public filterType: any;
  public image: string = 'assets/images/emptystate/no-task.png';
  public displayedRangesColumns: string[] = [
    'descripcion',
    'tipo',
    'ranges',
    '-',
  ];

  public dataSource!: MatTableDataSource<any>;
  public rangosBranchForm!: FormGroup;

  @Output() rangosArr = new EventEmitter<any>();
  @ViewChild(MatTable) table!: MatTable<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  branchData: any;

  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public utils: UtilsService,
    private formBuider: FormBuilder,
    private sharedDataService: SharedDataService
  ) {
    this.dataSource = new MatTableDataSource();

    this.rangosBranchForm = this.formBuider.group({
      descripcion: new FormControl('', Validators.required),
      inicioRango: new FormControl('', [Validators.required]),
      finalRango: new FormControl('', [Validators.required]),
      idRango: new FormControl('', Validators.required),
    });
  }

  catalogos: any;

  ngOnInit(): void {
    this.catalogos = this.sharedDataService.getVariableValue('catalogos');
    this.tipoRango = this.catalogos.tipo_rango ?? [];
    this.update = this.sharedDataService.getVariableValue('editarBranch');
    this.branchData = this.sharedDataService.getVariableValue('branch');

    if (this.update) {
      this.dataSource = new MatTableDataSource(this.branchData.rango ?? []);
    }

    if (this.item != undefined) {
      this.branchData = JSON.parse(JSON.stringify(this.item));
      this.dataSource = new MatTableDataSource(this.branchData.rangos.ranges);
    }
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  compareObjects(o1: any, o2: any): boolean {
    if (typeof o1 == 'object') {
      return parseInt(o1.id) === o2;
    }
    return parseInt(o1) === parseInt(o2);
  }

  test(tipo_dte: any) {
    this.filterType = this.tipoRango.filter(
      (item: any) => item.id === tipo_dte
    );
    this.rangosBranchForm.controls['inicioRango'].setValue(
      'DTE-' + this.filterType[0].id_mh + '-'
    );
    this.rangosBranchForm.controls['finalRango'].setValue(
      'DTE-' + this.filterType[0].id_mh + '-'
    );
  }

  limpiarformulario() {
    this.isEditing = false;
    this.rangosBranchForm.reset();
  }

  actualizarRange() {
    if (this.rangosBranchForm.valid) {
      this.rango = {
        descripcion: this.rangosBranchForm.value['descripcion'],
        idRango: this.rangosBranchForm.value['idRango'],
        inicioRango: this.rangosBranchForm.value['inicioRango'],
        finalRango: this.rangosBranchForm.value['finalRango'],
      };
    } else {
      return this.utils.showSWAL(
        'ERROR',
        'Por favor, complete todos los campos correctamente',
        'OK',
        'error'
      );
    }
    this.rango.actualValor = this.rango.inicioRango;
    this.rango.idTipoRango = this.rangosBranchForm.value['idRango'];
    this.rango.nombre = this.catalogos.tipo_rango.filter((value: any) => {
      if (value.id == this.rangosBranchForm.value['idRango']) {
        return value.name;
      }
    })[0].name;

    if (this.update) {
      this.rango.idSucursal = this.branchData.idSucursal;
      this.api.doRequest('/rango', this.rango, 'post').then((res: any) => {
        if (res.code != 200) {
          return this.utils.showSWAL(
            'ERROR AL ACTUALIZAR',
            res.data,
            'OK',
            'error'
          );
        }

        return Swal.fire({
          title: 'Se actualizo con exito',
          html: '',
          icon: 'success',
          confirmButtonText: 'aceptar',
        }).then(() => {
          this.dataSource.data.push(res.data);

          this.isEditing = false;
        });
      });
    }

    /**agregar rangos
     * enviar los datos al padre
     */

    this.isEditing = false;
    this.rangos.push(this.rango);
    this.dataSource = new MatTableDataSource<any>(this.rangos);
    this.dataSource.paginator = this.paginator;
    this.rangosArr.emit(this.rangos);

    return this.sharedDataService.setVariable('rangos', this.rangos);
  }

  eliminar(element: any, i: any) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#444C5E',
      confirmButtonText: 'Sí, eliminar',
    }).then((result) => {
      if (result.isConfirmed) {
        if (this.update) {
          this.api
            .doRequest('/rango/' + element.idRango, this.branchData, 'delete')
            .then((res: any) => {
              if (res.code == 200) {
                this.utils.showSWAL(
                  'RANGO ELIMINADO',
                  'El Rango fue eliminado con éxito',
                  'OK',
                  'success'
                );
                this.dataSource.data.splice(i, 1);
                this.dataSource = new MatTableDataSource<any>(
                  this.dataSource.data
                );
              }

              if (res.code != 200) {
                this.utils.showSWAL(
                  'ERROR',
                  'El Rango no fue eliminada \n' + res.data,
                  'OK',
                  'error'
                );
              }
            });
        }
        if (i >= 0 && i < this.rangos.length) {
          this.rangos.splice(i, 1);
          this.dataSource = new MatTableDataSource<any>(this.rangos);
        }
      }
    });
  }

  closeModal() {
    this.rangosBranchForm.reset();
    this.dialog.closeAll();
  }

  onKeyPressNumeros(event: KeyboardEvent) {
    if (!event.key.match(/\d/)) {
      event.preventDefault();
    }
  }
}
