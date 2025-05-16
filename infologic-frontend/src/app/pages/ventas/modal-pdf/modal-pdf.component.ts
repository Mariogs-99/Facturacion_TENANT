import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { environment } from 'src/app/core/environments/environment';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-modal-pdf',
  templateUrl: './modal-pdf.component.html',
  styleUrls: ['./modal-pdf.component.css'],
})
export class ModalPdfComponent {
  private apiUrl = environment.apiTransmisorUrl + 'api/v1';
  private apiUrlBack = environment.apiServerUrl + 'api/v1';
  zoom = 1.0;
  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public util: UtilsService,
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private sanitizer: DomSanitizer,

    private sharedDataService: SharedDataService
  ) {
    this.emailForm = this.formBuilder.group({
      emails: ['', [Validators.required, this.multiEmailValidator]],
    });

    this.whatsappForm = this.formBuilder.group({
      phone: ['', [Validators.required]],
    });
  }

  mostrarElemento: any;

  multiEmailValidator(control: AbstractControl): ValidationErrors | null {
    const emails: string[] = control.value
      .split(/[,;]/)
      .map((email: string) => email.trim());
    const emailPattern: RegExp = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/;
    const invalidEmails: string[] = emails.filter(
      (email: string) => !emailPattern.test(email)
    );
    return invalidEmails.length > 0 ? { invalidEmails: true } : null;
  }
  ngOnInit(): void {
    this.envio_correo = false;
    this.envio_whatsapp = false;
    this.mostrarElemento =
      this.sharedDataService.getVariableValue('mostrarElemento');

    this.loadPdf();
  }

  public emailForm: FormGroup;
  public whatsappForm: FormGroup;
  pdfSRC: SafeResourceUrl | null = null;
  qr_generado: boolean = false;
  transaction_id: any;

  envio_correo = false;
  envio_whatsapp = false;
  div_correo() {
    this.envio_correo = !this.envio_correo;
    this.envio_whatsapp = false;
  }
  div_whatsapp() {
    this.envio_correo = false;
    this.envio_whatsapp = true;
  }
  closeModal() {
    this.envio_correo = false;
    this.envio_whatsapp = false;
    this.dialog.closeAll();
  }
  enviar_correo() {
    if (this.emailForm.valid && this.emailForm.value.emails) {
      const emails: string[] = this.emailForm.value.emails
        .split(/[,;]/)
        .map((email: string) => email.trim());
      const body = {
        codigoGeneracion: this.mostrarElemento.codigoGeneracion,
        correo: emails,
      };

      this.api
        .doRequest2(this.apiUrl + '/dte/enviarVarios', body, 'post')
        .then((res: any) => {
          if (res.code == 200) {
            this.util.showSWAL(
              'Correo Enviado.',
              'El Documento Tributario Electrónico fue enviado Correctamente a la dirección indicada',
              'OK',
              'success'
            );
            this.closeModal();
          }
        });
    } else {
      this.util.showSWAL(
        'ERROR',
        'Por favor, Digite Correos Electrónicos Válidos',
        'OK',
        'error'
      );
    }
  }

  async descargar() {
    try {
      this.http
        .get(
          this.apiUrl + '/dte/reporte/' + this.mostrarElemento.codigoGeneracion,

          { responseType: 'arraybuffer' }
        )
        .subscribe(
          (response: ArrayBuffer) => {
            const blob = new Blob([response], { type: 'application/pdf' });

            const url = window.URL.createObjectURL(blob);

            const fileName = this.mostrarElemento.codigoGeneracion + '.pdf';
            const link = document.createElement('a');
            link.href = url;
            link.download = fileName;

            link.click();

            window.URL.revokeObjectURL(url);
          },
          (error) => {
            console.error('Error al obtener el PDF:', error);
          }
        );
    } catch (error) {
      console.error('Error loading PDF', error);
    }
  }

  enviar_whatsapp() {
    if (this.whatsappForm.valid && this.whatsappForm.value.phone) {
      const body = {
        codigoGeneracion: this.mostrarElemento.codigoGeneracion,
        telefono: this.whatsappForm.value.phone,
      };

      this.api
        .doRequest2(
          this.apiUrl +
          '/dte/send-message/' +
          this.mostrarElemento.codigoGeneracion +
          '/503' +
          this.whatsappForm.value.phone,
          body,
          'get'
        )
        .then((res: any) => {
          if (res.code == 200) {
            this.util.showSWAL(
              'Mensaje Enviado.',
              'El Documento Tributario Electrónico fue enviado Correctamente al teléfono indicado',
              'OK',
              'success'
            );
            this.closeModal();
          }
        });
    } else {
      this.util.showSWAL(
        'ERROR',
        'Por favor, Digite Teléfono Válido',
        'OK',
        'error'
      );
    }
  }

  zoomIn() {
    this.zoom += 0.1;
    console.log("zoom in", this.zoom)
  }

  zoomOut() {
    if (this.zoom > 0.2) {
      this.zoom -= 0.1;
    }
    console.log("zoom out", this.zoom)

  }

  async loadPdf() {
    try {
      this.http
        .get(
          this.apiUrl +
          '/dte/reporte/pdf/' +
          this.mostrarElemento.codigoGeneracion,

          { responseType: 'text' }
        )
        .subscribe(
          (response: string) => {
            this.pdfSRC = 'data:application/pdf;base64,' + response;
          },
          (error) => {
            console.error('Error al obtener el PDF:', error);
          }
        );
    } catch (error) {
      console.error('Error loading PDF', error);
    }
  }
}
