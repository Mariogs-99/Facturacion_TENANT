import { Component, Inject, ViewChild } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Roles } from 'src/app/core/model/roles/roles.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Permisos } from 'src/app/core/model/permisos/permisos.model';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { ManageMenuComponent } from 'src/app/pages/menu/views/manage-menu/manage-menu.component';

@Component({
  selector: 'app-manage-roles',
  templateUrl: './manage-roles.component.html',
})
export class ManageRolesComponent {
  public rolesForm: FormGroup;
  public rol: Roles = {};
  public permissions: Permisos[] = [];

  public updating: boolean = false;
  public selection = new SelectionModel<number | undefined>(true, []);

  dataSource = new MatTableDataSource<Permisos>();
  displayedColumns: string[] = [
    'select',
    'namePermissions',
    'descriptionPermissions',
  ];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  currentPageNo: any = 1;
  totalRecords: number = 0;
  paginationSize: number = 5;
  paginationSizeOptions: number[] = [5, 10, 25, 50, 100];

  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    public dialogRef: MatDialogRef<ManageMenuComponent>,
    private api: ApiService,
    private formBuilder: FormBuilder,
    private utils: UtilsService,
    private _data: DataService
  ) {
    this.rol = dialogData.data.permiso;
    this.rolesForm = this.formBuilder.group({
      idRole: new FormControl('', this.updating ? [Validators.required] : null),
      nameRole: new FormControl('', [Validators.required]),
      descriptionRole: new FormControl('', [Validators.required]),
      permissionIds: new FormControl([], [Validators.required]),
    });
    this.index();

    if (this.rol?.idRole != undefined) {
      this.setRol(this.rol?.idRole);
    }
  }

  setRol(idRole: number | undefined) {
    this.updating = true;

    this.api
      .doRequest('/roles/' + idRole, null, 'get')
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'No encontro el menu seleccionado',
            '',
            'Aceptar',
            'error'
          );
        }

        this.rol = resp.data.rol;

        this.rolesForm.patchValue({
          idRole: this.rol.idRole,
          nameRole: this.rol.nameRole,
          descriptionRole: this.rol.descriptionRole,
          permissionIds:
            this.rol.permission != undefined && this.rol.permission.length > 0
              ? this.rol.permission.map((item) => item.idPermissions)
              : null,
        });

        return this.checkCheck();
      })
      .catch((error) => {});
  }

  update() {
    this.updating = this.rolesForm.controls['idRole'].value != '';

    this.api
      .doRequest(
        this.updating
          ? '/roles/' + this.rolesForm.controls['idRole'].value
          : '/roles',
        this.rolesForm.value,
        this.updating ? 'put' : 'post'
      )
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            this.updating ? 'No se actualizo el Rol' : 'No se creo el Rol',
            '',
            'Aceptar',
            'error'
          );
        }

        return this.utils
          .showSWAL(
            this.updating
              ? 'Rol actualizado correctamente'
              : 'Rol creado correctamente',
            '',
            'Aceptar',
            'success'
          )
          .then(() => {
            this.dialogRef.close({
              action: 'update',
              result: true,
            });
          });
      })
      .catch((error: any) => {});
  }

  index(page: number = 0, size: number = 5) {
    this.api
      .getPage('/permissions/page', undefined, undefined, page, size)
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'Ha ocurrido un error',
            '',
            'Aceptar',
            'warning'
          );
        }

        this.permissions = resp.data.pages.content;
        this.dataSource = new MatTableDataSource<Permisos>(
          resp.data.pages.content
        );
        this.totalRecords = resp.data.pages.totalElements;
        this.dataSource.sort = this.sort;
        return this.checkCheck();
      });
  }

  changecheck(checked: boolean, row: any) {
    if (checked) {
      this.selection.select(row.idPermissions);
    } else {
      this.selection.deselect(row.idPermissions);
    }
    this.rolesForm.controls['permissionIds'].setValue(this.selection.selected);
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;

    return numSelected === numRows;
  }

  toggleAllRows() {
    if (this.isAllSelected() || this.selection.selected.length > 0) {
      this.selection.clear();
      this.rolesForm.controls['permissionIds'].setValue([]);
    } else {
      this.selection.select(
        ...this.dataSource.data.map((item) => item.idPermissions)
      );

      this.rolesForm.controls['permissionIds'].setValue(
        this.selection.selected
      );
    }
  }

  masterToggle() {
    if (this.selection.isEmpty()) {
      this.selection.select(
        ...this.dataSource.data.map((item) => item.idPermissions)
      );
    } else {
      this.selection.clear();
    }
  }

  checkCheck() {
    this.dataSource.data.forEach((row) => {
      this.rolesForm.controls['permissionIds'].value.forEach(
        (select_: number) => {
          if (row.idPermissions === select_) {
            this.selection.select(row.idPermissions);
          }
        }
      );
    });
  }

  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;

    this.index(this.currentPageNo, this.paginationSize);
  }
}
