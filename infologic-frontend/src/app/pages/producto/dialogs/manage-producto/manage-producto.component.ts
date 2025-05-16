import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Producto } from 'src/app/core/model/producto/producto.model';

@Component({
  selector: 'app-manage-producto',
  templateUrl: './manage-producto.component.html',
})
export class ManageProductoComponent implements OnInit {
  public productoForm: FormGroup;
  public isEditMode: boolean = false;
  private IVA_PORCENTAJE = 0.13; // 🔥 IVA del 13%

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private utils: UtilsService,
    private dialogRef: MatDialogRef<ManageProductoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { producto: Producto }
  ) {
    this.isEditMode = !!data.producto?.idProducto;

    this.productoForm = this.fb.group({
      codigoProducto: ['', [Validators.required, Validators.pattern(/^\d{1}-\d{3}$/)]], 
      clasificacion: [data.producto?.clasificacion || 'GENERAL', Validators.required], 
      nombre: [data.producto?.nombre || '', Validators.required],
      descripcion: [data.producto?.descripcion || ''],
      codigoIngreso: [data.producto?.codigoIngreso || '', [Validators.maxLength(50)]], 
      precio: [data.producto?.precio || 0, Validators.required],
      iva: [{ value: data.producto?.iva || 0, disabled: true }], 
      total: [{ value: data.producto?.total || 0, disabled: true }], 
      tipo: [{ value: 'G', disabled: true }], 
      estado: [{ value: 'I', disabled: true }],
    });
    
  }

  ngOnInit(): void {
    this.formatearCodigoProducto();
    this.calcularValores(); // 🔥 Activar cálculo de IVA y Total
  }

  formatearCodigoProducto(): void {
    this.productoForm.get('codigoProducto')?.valueChanges.subscribe((valor: string) => {
      if (!valor) {
        return;
      }
  
      // 🔥 Eliminar caracteres no numéricos excepto el guion
      valor = valor.replace(/[^0-9-]/g, '');
  
      // 🔥 Separar la parte antes y después del guion
      let partes = valor.split('-');
      
      let parteIzquierda = partes[0].substring(0, 1); // Máximo 1 número antes del guion
      let parteDerecha = partes[1] ? partes[1].substring(0, 3) : ''; // Máximo 3 números después del guion
  
      // 🔥 Construir el código con el guion automático
      let codigoFormateado = parteIzquierda;
      if (valor.length > 1 || partes.length > 1) {
        codigoFormateado += `-${parteDerecha}`;
      }
  
      this.productoForm.patchValue({ codigoProducto: codigoFormateado }, { emitEvent: false });
    });
  }
  
  
  calcularValores(): void {
    this.productoForm.get('precio')?.valueChanges.subscribe((precio: number) => {
      if (!precio || precio < 0) {
        this.productoForm.patchValue({ iva: 0, total: 0 }, { emitEvent: false });
        return;
      }

      const iva = precio * this.IVA_PORCENTAJE;
      const total = precio + iva;

      this.productoForm.patchValue({ 
        iva: iva.toFixed(2), 
        total: total.toFixed(2) 
      }, { emitEvent: false });
    });
  }

  guardar(): Promise<any> | undefined {
    if (this.productoForm.invalid) {
      return this.utils.showSWAL('Error', 'Por favor complete todos los campos obligatorios.', 'Aceptar', 'warning');
    }
  
    let productoData = this.productoForm.getRawValue();
  
    if (!productoData.codigoProducto.trim()) {
      return this.utils.showSWAL('Error', 'El código del producto no puede estar vacío.', 'Aceptar', 'warning');
    }
  
    if (!productoData.clasificacion.trim()) {
      return this.utils.showSWAL('Error', 'La clasificación del producto no puede estar vacía.', 'Aceptar', 'warning');
    }
  
    productoData.codigo_producto = productoData.codigoProducto;
    productoData.codigo_ingreso = productoData.codigoIngreso; 
    delete productoData.codigoProducto;
    delete productoData.codigoIngreso;
  
    const endpoint = this.isEditMode
      ? `/productos/${this.data.producto.idProducto}`
      : '/productos';
  
    const method = this.isEditMode ? 'put' : 'post';
  
    return this.api.doRequest(endpoint, productoData, method)
      .then((resp: any) => {
        console.log("Respuesta del backend:", resp); 
  
        if (!resp || (resp.code && resp.code !== 200 && resp.code !== 201 && resp.code !== 204)) {
          return this.utils.showSWAL('Error', 'No se pudo guardar el producto', 'Aceptar', 'warning');
        }
  
        return this.utils.showSWAL('Éxito', 'Producto guardado correctamente', 'Aceptar', 'success').then(() => {
          this.dialogRef.close({ action: 'update' });
          window.location.reload();
        });
      })
      .catch((error) => {
        console.error("Error en la solicitud:", error);
        return this.utils.showSWAL('Error', 'Ocurrió un error al guardar el producto', 'Aceptar', 'warning');
      });
  }
  
  
  

  cancelar() {
    this.dialogRef.close();
  }
}
