import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-manage-cliente',
  templateUrl: './manage-cliente.component.html',
})
export class ManageClienteComponent implements OnInit {
  public clienteForm: FormGroup;
  public isEditMode: boolean = false;
  public departamentos: any[] = [];
  
  public municipios: any[] = [];
  public giros: any[] = [];
  public filteredGiros: any[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private api: ApiService,
    private utils: UtilsService,
    public dialogRef: MatDialogRef<ManageClienteComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.isEditMode = !!data.cliente?.id;

    this.clienteForm = this.formBuilder.group({
      nombre: [data.cliente?.nombre || '', Validators.required],
      nombreComercial: [data.cliente?.nombreComercial || ''],
      tipoDocumento: [data.cliente?.tipoDocumento || '', Validators.required],
      numeroDocumento: [data.cliente?.numeroDocumento || '', Validators.required],
      nrc: [data.cliente?.nrc || '', [Validators.maxLength(5)]],
      telefono: [data.cliente?.telefono || '', Validators.required],
      email: [data.cliente?.email || '', [Validators.required, Validators.email]],
      pais: [data.cliente?.pais || '', Validators.required],
      departamento: [data.cliente?.departamento || '', Validators.required],
      municipio: [data.cliente?.municipio || '', Validators.required],
      direccion: [data.cliente?.direccion || ''],
      actividadEconomica: [data.cliente?.actividadEconomica || ''],
      esConsumidorFinal: [data.cliente?.esConsumidorFinal ?? false],
      esExtranjero: [data.cliente?.esExtranjero ?? false],
      esGobierno: [data.cliente?.esGobierno ?? false],
      esGranContribuyente: [data.cliente?.esGranContribuyente ?? false]
    });

    this.clienteForm.get('tipoDocumento')?.valueChanges.subscribe(() => {
      this.applyDocumentRules();
    });

    this.clienteForm.get('numeroDocumento')?.valueChanges.subscribe(() => {
      this.formatNumeroDocumento();
    });

    if (this.clienteForm.value.departamento) {
      this.updateMunicipios(this.clienteForm.value.departamento);
    }
  }

  ngOnInit(): void {
    this.loadGiros();
    this.applyDocumentRules();
    this.loadDepartamentos();
  }

  //!......................DEPARTAMENTOS API..........................

  loadDepartamentos() {
    this.api.doRequest('/rcatalogos/departamentosmunicipios', {}, 'get').then((res: any) => {
      console.log("Respuesta API Departamentos:", res);

      if (res.code === 200) {
        this.departamentos = res.data.map((depto: any) => ({
          codigo: depto.codigo.toString(), // Asegurar que sea string
          nombre: depto.nombre,
          municipios: depto.municipios.map((muni: any) => ({
            codigo: muni.codigo.toString(),
            nombre: muni.nombre
          }))
        }));

        console.log("Departamentos correctamente formateados:", this.departamentos);
      }
    }).catch(error => {
      console.error("Error al cargar departamentos:", error);
    });
  }
  
  updateMunicipios(departamentoCodigo: string) {
    console.log("Departamento seleccionado:", departamentoCodigo);

    // Limpiar municipios al cambiar de departamento
    this.municipios = [];
    this.clienteForm.controls['municipio'].setValue('');

    const deptoSeleccionado = this.departamentos.find(d => d.codigo === departamentoCodigo);
    
    if (deptoSeleccionado) {
      this.municipios = [...deptoSeleccionado.municipios];
      console.log("Municipios disponibles:", this.municipios);
    } else {
      console.warn("No se encontraron municipios para el departamento:", departamentoCodigo);
    }
  }
  
  
  
  
  

    //!......................GIROS API...............................

  loadGiros() {
    this.api.doRequest('/rcatalogos/giro', {}, 'get').then((res: any) => {
      if (res.code === 200) {
        this.giros = res.data.map((giro: any) => ({
          id: giro.idCatalogo,
          name: giro.valor
        }));
        this.filteredGiros = [...this.giros];
      }
    });
  }

  filterGiros(event: any) {
    const value = event.target.value.toLowerCase();
    this.filteredGiros = this.giros.filter(giro => giro.name.toLowerCase().includes(value));
  }

  selectGiro(event: any) {
    const selectedGiro = this.giros.find(g => g.name === event.option.value);
    if (selectedGiro) {
      this.clienteForm.controls['actividadEconomica'].setValue(selectedGiro.id);
    }
  }

  applyDocumentRules() {
    const tipo = this.clienteForm.get('tipoDocumento')?.value;
  
    if (tipo === 'NIT') {
      // Copiar automáticamente el número de documento al NIT
      const numeroDocumento = this.clienteForm.get('numeroDocumento')?.value;
      this.clienteForm.get('nit')?.setValue(numeroDocumento);
      
      // Deshabilitar el campo NIT para que no lo editen manualmente
      this.clienteForm.get('nit')?.disable();
  
      // Limpiar validadores (ya no es requerido aparte)
      this.clienteForm.get('nit')?.clearValidators();
      this.clienteForm.get('nit')?.updateValueAndValidity();
    } else {
      // Habilitar el campo NIT para edición si el tipo es DUI o Pasaporte
      this.clienteForm.get('nit')?.enable();
  
      // Aplicar validador requerido solo en estos casos
      this.clienteForm.get('nit')?.setValidators([Validators.required]);
      this.clienteForm.get('nit')?.updateValueAndValidity();
    }
  }
  

  formatNumeroDocumento() {
    let tipo = this.clienteForm.get('tipoDocumento')?.value;
    let numeroDocumento = this.clienteForm.get('numeroDocumento')?.value || '';
  
    if (tipo === 'DUI') {
      // Limitar y formatear DUI (00000000-0)
      numeroDocumento = numeroDocumento.replace(/\D/g, '').slice(0, 9);
      if (numeroDocumento.length > 8) {
        numeroDocumento = numeroDocumento.slice(0, 8) + '-' + numeroDocumento.slice(8);
      }
    } else if (tipo === 'NIT') {
      // Limitar y formatear NIT (0000-000000-000-0)
      numeroDocumento = numeroDocumento.replace(/\D/g, '').slice(0, 17);
      if (numeroDocumento.length > 4) {
        numeroDocumento = numeroDocumento.slice(0, 4) + '-' + numeroDocumento.slice(4);
      }
      if (numeroDocumento.length > 11) {
        numeroDocumento = numeroDocumento.slice(0, 11) + '-' + numeroDocumento.slice(11);
      }
      if (numeroDocumento.length > 15) {
        numeroDocumento = numeroDocumento.slice(0, 15) + '-' + numeroDocumento.slice(15);
      }
      this.clienteForm.get('nit')?.setValue(numeroDocumento);
    }
  
    this.clienteForm.get('numeroDocumento')?.setValue(numeroDocumento, { emitEvent: false });
  }
  
  formatNIT() {
    let nit = this.clienteForm.get('nit')?.value || '';
    nit = nit.replace(/\D/g, '').slice(0, 17);
  
    if (nit.length > 4) {
      nit = nit.slice(0, 4) + '-' + nit.slice(4);
    }
    if (nit.length > 11) {
      nit = nit.slice(0, 11) + '-' + nit.slice(11);
    }
    if (nit.length > 15) {
      nit = nit.slice(0, 15) + '-' + nit.slice(15);
    }
  
    this.clienteForm.get('nit')?.setValue(nit, { emitEvent: false });
  }
  

  guardar(): Promise<any> | undefined {
    if (this.clienteForm.invalid) {
      return this.utils.showSWAL('Error', 'Por favor complete todos los campos obligatorios.', 'Aceptar', 'warning');
    }
  
    let clienteData = this.clienteForm.getRawValue();

    if (clienteData.tipoDocumento === 'NIT') {
      clienteData.nit = clienteData.numeroDocumento.replace(/\D/g, '');
    }
  
    // 🔹 Convertir booleanos en 1 o 0
    clienteData.esConsumidorFinal = clienteData.esConsumidorFinal ? 1 : 0;
    clienteData.esExtranjero = clienteData.esExtranjero ? 1 : 0;
    clienteData.esGobierno = clienteData.esGobierno ? 1 : 0;
    clienteData.esGranContribuyente = clienteData.esGranContribuyente ? 1 : 0;
  
    console.log("Datos enviados al backend:", clienteData); // Debugging
  
    const endpoint = this.isEditMode
      ? `/clientes/${this.data.cliente.id}`
      : '/clientes';
    const method = this.isEditMode ? 'put' : 'post';
  
    return this.api.doRequest(endpoint, clienteData, method)
      .then((resp: any) => {
        if (!resp || (resp.code && resp.code !== 200 && resp.code !== 201 && resp.code !== 204)) {
          return this.utils.showSWAL('Error', 'No se pudo guardar el cliente', 'Aceptar', 'warning');
        }
  
        return this.utils.showSWAL('Éxito', 'Cliente guardado correctamente', 'Aceptar', 'success').then(() => {
          this.dialogRef.close({ action: 'update' });
          window.location.reload();
        });
      })
      .catch((error) => {
        console.error('Error en la solicitud:', error);
        return this.utils.showSWAL('Error', 'Ocurrió un error al guardar el cliente', 'Aceptar', 'warning');
      });
  }
  

  cancelar() {
    this.dialogRef.close();
  }
}
