import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModalPdfComponent } from './modal-pdf.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { of } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';  // Asegúrate de tener la ruta correcta

describe('ModalPdfComponent', () => {
  let component: ModalPdfComponent;
  let fixture: ComponentFixture<ModalPdfComponent>;
  let mockApiService: jasmine.SpyObj<ApiService>;
  let mockUtilsService: jasmine.SpyObj<UtilsService>;
  let mockSharedDataService: jasmine.SpyObj<SharedDataService>;
  const mockDialog = jasmine.createSpyObj('MatDialog', ['closeAll']);

  beforeEach(async () => {
    mockApiService = jasmine.createSpyObj('ApiService', ['doRequest2']);
    mockUtilsService = jasmine.createSpyObj('UtilsService', ['showSWAL']);
    mockSharedDataService = jasmine.createSpyObj('SharedDataService', [
      'getVariableValue',
    ]);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule],
      declarations: [ModalPdfComponent],
      providers: [
        { provide: ApiService, useValue: mockApiService },
        { provide: UtilsService, useValue: mockUtilsService },
        { provide: SharedDataService, useValue: mockSharedDataService },
        { provide: MatDialog, useValue: mockDialog },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalPdfComponent);
    component = fixture.componentInstance;

    // Configuración inicial
    mockSharedDataService.getVariableValue.and.returnValue({
      codigoGeneracion: 'testCode',
    });
    component.mostrarElemento = { codigoGeneracion: 'testCode' };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should send email when form is valid', async () => {
    component.emailForm.setValue({ emails: 'test@example.com' });
    const mockResponse: JsonResponse = {
      code: 200,
      data: {},
      message: '',
    };
    mockApiService.doRequest2.and.returnValue(
      Promise.resolve(mockResponse)
    );

    await component.enviar_correo();

    expect(mockApiService.doRequest2).toHaveBeenCalledWith(
      jasmine.any(String),
      jasmine.any(Object),
      'post'
    );
    expect(mockUtilsService.showSWAL).toHaveBeenCalledWith(
      'Correo Enviado.',
      'El Documento Tributario Electrónico fue enviado Correctamente a la dirección indicada',
      'OK',
      'success'
    );
  });

  it('should show error when email is invalid', async () => {
    component.emailForm.setValue({ emails: '' });

    await component.enviar_correo();

    expect(mockUtilsService.showSWAL).toHaveBeenCalledWith(
      'ERROR',
      'Por favor, Digite Correos Electrónicos Válidos',
      'OK',
      'error'
    );
  });

  it('should send WhatsApp message when form is valid', async () => {
    component.whatsappForm.setValue({ phone: '123456789' });
    const mockResponse: JsonResponse = {
      code: 200,
      data: {},
      message: '',
    };
    mockApiService.doRequest2.and.returnValue(
      Promise.resolve(mockResponse)
    );

    await component.enviar_whatsapp();

    expect(mockApiService.doRequest2).toHaveBeenCalledWith(
      jasmine.any(String),
      jasmine.any(Object),
      'get'
    );
    expect(mockUtilsService.showSWAL).toHaveBeenCalledWith(
      'Mensaje Enviado.',
      'El Documento Tributario Electrónico fue enviado Correctamente al teléfono indicado',
      'OK',
      'success'
    );
  });

  it('should show error when WhatsApp phone is invalid', async () => {
    component.whatsappForm.setValue({ phone: '' });

    await component.enviar_whatsapp();

    expect(mockUtilsService.showSWAL).toHaveBeenCalledWith(
      'ERROR',
      'Por favor, Digite Teléfono Válido',
      'OK',
      'error'
    );
  });

  it('should zoom in and zoom out correctly', () => {
    component.zoomIn();
    expect(component.zoom).toBe(1.1);

    component.zoomOut();
    expect(component.zoom).toBe(1.0);
  });
});
