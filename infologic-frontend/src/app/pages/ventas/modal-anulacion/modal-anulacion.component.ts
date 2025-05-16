import { Component, ElementRef, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ApiService } from 'src/app/core/services/api.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { MatSelect } from '@angular/material/select';
import { AuthService } from 'src/app/core/services/auth.service';
import { environment } from 'src/app/core/environments/environment';
import { ValidacionesService } from 'src/app/core/services/validaciones.service';

@Component({
  selector: 'app-modal-anulacion',
  templateUrl: './modal-anulacion.component.html',
})
export class ModalAnulacionComponent {
  @ViewChild('motivoSelect') motivoSelect!: MatSelect;
  @ViewChild('descripcionTextarea') descripcionTextarea!: ElementRef;

  public registerForm2!: FormGroup;
  public nombre: string="";
  public dui: string="";


  private apiUrl = environment.apiTransmisorUrl+'api/v1'; 
  private apiUrlBack = environment.apiServerUrl+'api/v1'; 


  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public util: UtilsService,
    public validaciones: ValidacionesService,
    public auth: AuthService,
    private formBuider: FormBuilder,
    private sharedDataService: SharedDataService 
  ) {


    this.dui = this.auth.getDui();
    this.nombre = this.auth.getNombre();




  }

  catalogos: any;
  anularElemento: any;
  ngOnInit(): void {

    

    this.registerForm2 = this.formBuider.group({
      solicitante: new FormControl('', [
        Validators.required,
        Validators.maxLength(250),
      ]),

      DOC_solicitante: new FormControl('', [Validators.required]),

      responsable: new FormControl(this.nombre, [
        Validators.required,
        Validators.maxLength(250),
      ]),

      DOC_responsable: new FormControl(this.dui, [Validators.required]),

      motivoSelect: new FormControl('', [Validators.required]),

      descripcionTextarea: new FormControl('', [Validators.required]),
    });


    this.catalogos = this.sharedDataService.getVariableValue('catalogos');
    
    this.anularElemento =
      this.sharedDataService.getVariableValue('anularElemento');

    this.invalidacionTipo = this.catalogos.tipoAnulacion;
  }

  public invalidacionTipo: any;

  closeModal() {
    this.dialog.closeAll();
  }

  capitalizeWords(value: string): string {
    if (!value) return ''; 
    return value
      .split(' ')
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  confirmarAnulacion() {
    
    const formValues = this.registerForm2.getRawValue();

    let payload = {
      tipoAnulacion: formValues.motivoSelect,
      tipoInvalidacion: 2,
      motivoInvalidacion: formValues.descripcionTextarea,
      nombreResponsable: formValues.responsable,
      tipoDocResponsable: '36',
      numDocResponsable: formValues.DOC_responsable,
      nombreSolicita: formValues.solicitante,
      tipoDocSolicita: '36',
      numDocSolicita: formValues.DOC_solicitante,
      codigoGeneracionr: null, 
    };

    

    
    if (this.validarAnulacionDET(payload)) {
      
      this.api
        .doRequest2(
          this.apiUrl + '/dte/anularDte/' +
          
            this.anularElemento.codigoGeneracion,
          payload,
          'post'
        )
        .then((res: any) => {
          if (res.code != 200) {
            return this.util.showSWAL(
              'Error no se invalido el documento',
              res.data.mensaje,
              'error'
            );
          }

          this.closeModal();
          return this.util
            .showSWAL(
              'Invalidación exitosa',
              'El DTE fue invalidado con éxito',
              'Aceptar'
            )
            .then((result) => {
              if (result.isConfirmed) {
                this.reloadPage();
              }
            });
        });
    }
  }

  reloadPage() {
    window.location.reload();
  }

  validarAnulacionDET(payload: any) {
    
    if (payload.motivoAnulacion == undefined && payload.tipoAnulacion == '') {
      this.util.showSWAL(
        'Todos los campos son requeridos',
        'Por favor llenar todos los campos',
        'Aceptar',
        'warning'
      );
      return false;
    }

    if (payload.tipoAnulacion == undefined) {
      this.util.showSWAL(
        'Motivo es requerido',
        'Por favor seleccione el motivo',
        'Aceptar',
        'warning'
      );
      return false;
    }

    if (payload.motivoAnulacion == '') {
      this.util.showSWAL(
        'Descripción es requerido',
        'Por favor llene la descripción del motivo',
        'Aceptar',
        'warning'
      );
      return false;
    }



    if (!payload.motivoInvalidacion || payload.motivoInvalidacion.length < 5) {
      this.util.showSWAL(
        'Mínimo de caracteres',
        'El Motivo debe poseer un mínimo de 5 caracteres',
        'Aceptar',
        'warning'
      );
      return false;
    }
    return true;
  }
}
