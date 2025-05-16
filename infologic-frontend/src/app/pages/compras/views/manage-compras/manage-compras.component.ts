import { Component, Inject } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Category } from 'src/app/core/model/category.model';
import { Compras } from 'src/app/core/model/compras/compras.model';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-manage-compras',
  templateUrl: './manage-compras.component.html',
})
export class ManageComprasComponent {
  updating: boolean = false;

  public compras: Compras;
  comprasForm: FormGroup;
  public documentosTipo: Category[] = [];
  public operacionTipo: Category[] = [];
  public operacionTipoSelected: Category = {};
  public documentosTipoSelected: Category = {};

  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ManageComprasComponent>,
    private api: ApiService,
    private _data: DataService,
    private utils: UtilsService
  ) {
    this.compras = dialogData.data.compras;
    this.operacionTipo = dialogData.data.operacion_tipo;
    this.documentosTipo = dialogData.data.documento_tipo;

    this.comprasForm = this.fb.group({
      idCompra: new FormControl(
        '',
        this.updating ? [Validators.required] : null
      ),
      codigoGeneracion: new FormControl('', [Validators.required]),
      numeroControl: new FormControl('', [Validators.required]),
      fechaEmision: new FormControl('', [Validators.required]),
      tipoOperacion: new FormControl('', [Validators.required]),
      tipoDocumento: new FormControl('', [Validators.required]),
      selloRecepcion: new FormControl('', [Validators.required]),
      idProveedor: new FormControl('', [Validators.required]),
      totalGravado: new FormControl('', [Validators.required]),
      totalExento: new FormControl('', [Validators.required]),
      totalOperacion: new FormControl('', [Validators.required]),
    });

    if (this.compras?.idCompra !== undefined) {
      this.setCompra(this.compras.idCompra);
    }
  }

  setCompra(idCompra: number | undefined) {
    this.updating = true;
    this.api
      .doRequest('/compras/' + idCompra, null, 'get')
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'No encontró la compra seleccionada',
            '',
            'Aceptar',
            'error'
          );
        }
        this.compras = resp.data.compras;

        this._data.sendMessage({
          from: 'UpdateCompraComponent',
          action: 'update',
          data: {
            compra: this.compras,
            pages: resp.data.pages,
          },
        });

        return this.comprasForm.patchValue({
          idCompra: this.compras.idCompra,
          codigoGeneracion: this.compras.codigoGeneracion,
          numeroControl: this.compras.numeroControl,
          fechaEmision: this.compras.fechaEmision,
          tipoOperacion: this.compras.tipoOperacion,
          tipoDocumento: this.compras.tipoDocumento,
          selloRecepcion: this.compras.selloRecepcion,
          idProveedor: this.compras.idProveedor,
          totalGravado: this.compras.totalGravado,
          totalExento: this.compras.totalExento,
          totalOperacion: this.compras.totalOperacion,
        });
      })
      .catch((error) => {
        console.error('update menu', error);
      });
  }

  update() {
    this.updating = this.comprasForm.controls['idCompra'].value !== '';
    this.api
      .doRequest(
        this.updating
          ? '/compras/' + this.comprasForm.controls['idCompra'].value
          : '/compras',
        this.comprasForm.value,
        this.updating ? 'put' : 'post'
      )
      .then((resp: any) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            this.updating
              ? 'No se actualizó el registro'
              : 'No se guardó la compra',
            '',
            'Aceptar',
            'error'
          );
        }

        return this.utils
          .showSWAL(
            this.updating
              ? 'Registro actualizado correctamente'
              : 'Resgistro ingresado correctamente',
            '',
            'Aceptar',
            'success'
          )
          .then(() => {
            return this.dialogRef.close({
              action: 'update',
              result: true,
            });
          });
      })
      .catch((error: any) => {
        console.error('error update', error);
      });
  }

  public compareWith(object1: any, object2: any) {
    return object1 && object2 && object1 === object2;
  }
}
