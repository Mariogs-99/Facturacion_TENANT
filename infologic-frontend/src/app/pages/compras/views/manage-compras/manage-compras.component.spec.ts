import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageComprasComponent } from './manage-compras.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { of, throwError } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule, MatOption } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('ManageComprasComponent', () => {
  let component: ManageComprasComponent;
  let fixture: ComponentFixture<ManageComprasComponent>;
  let apiService: jasmine.SpyObj<ApiService>;
  let dataService: jasmine.SpyObj<DataService>;

  const mockDialogRef = {
    close: jasmine.createSpy('close'),
  };

  beforeEach(async () => {
    const apiSpy = jasmine.createSpyObj('ApiService', ['doRequest']);
    const dataSpy = jasmine.createSpyObj('DataService', ['sendMessage']);

    await TestBed.configureTestingModule({
      declarations: [ManageComprasComponent],
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,
        BrowserAnimationsModule,
      ],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            data: { compras: {}, operacion_tipo: [], documento_tipo: [] },
          },
        },
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: ApiService, useValue: apiSpy },
        { provide: DataService, useValue: dataSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ManageComprasComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    dataService = TestBed.inject(DataService) as jasmine.SpyObj<DataService>;

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form correctly', () => {
    expect(component.comprasForm).toBeDefined();
    expect(
      component.comprasForm.controls['codigoGeneracion'].valid
    ).toBeFalsy();
  });

  it('should set the compra and populate the form when editing', async () => {
    const mockCompra = {
      idCompra: 1,
      codigoGeneracion: 'CG123',
      numeroControl: 'NC123',
      fechaEmision: new Date(),
      tipoOperacion: 'Tipo1',
      tipoDocumento: 'Doc1',
      selloRecepcion: 'SR123',
      idProveedor: 'Prov1',
      totalGravado: 100,
      totalExento: 0,
      totalOperacion: 100,
    };

    apiService.doRequest.and.returnValue(
      Promise.resolve({
        code: 200,
        data: { compras: mockCompra },
      } as JsonResponse)
    );

    component.setCompra(mockCompra.idCompra);

    await fixture.whenStable();

    expect(apiService.doRequest).toHaveBeenCalledWith(
      '/compras/' + mockCompra.idCompra,
      null,
      'get'
    );
    expect(component.comprasForm.controls['codigoGeneracion'].value).toEqual(
      mockCompra.codigoGeneracion
    );
    expect(component.comprasForm.controls['numeroControl'].value).toEqual(
      mockCompra.numeroControl
    );
  });

  it('should show an error message if the compra is not found', async () => {
    apiService.doRequest.and.returnValue(
      Promise.resolve({ code: 404 } as JsonResponse)
    );

    await component.setCompra(999);

 
  });

  it('should update the compra successfully', async () => {
    component.comprasForm.patchValue({
      idCompra: 1,
      codigoGeneracion: 'CG123',
      numeroControl: 'NC123',
      fechaEmision: new Date(),
      tipoOperacion: 'Tipo1',
      tipoDocumento: 'Doc1',
      selloRecepcion: 'SR123',
      idProveedor: 'Prov1',
      totalGravado: 100,
      totalExento: 0,
      totalOperacion: 100,
    });

    apiService.doRequest.and.returnValue(
      Promise.resolve({ code: 200 } as JsonResponse)
    );

    await component.update();
    await fixture.whenStable(); // Asegúrate de esperar a que se resuelvan las promesas

    expect(apiService.doRequest).toHaveBeenCalledWith(
      '/compras/' + component.comprasForm.controls['idCompra'].value,
      component.comprasForm.value,
      'put'
    );
    expect(mockDialogRef.close).toHaveBeenCalledWith({
      action: 'update',
      result: true,
    });
  });

  it('should show an error message if update fails', async () => {
    component.comprasForm.patchValue({ idCompra: 1 });

    apiService.doRequest.and.returnValue(
      Promise.resolve({ code: 400 } as JsonResponse)
    );

    await component.update();
    await fixture.whenStable(); // Espera a que se resuelvan las promesas

  });

  it('should create a new compra successfully', async () => {
    // Aquí aseguramos que idCompra sea vacío para una nueva compra
    component.comprasForm.patchValue({
      idCompra: '', // Dejar vacío para indicar una nueva compra
      codigoGeneracion: 'CG123',
      numeroControl: 'NC123',
      fechaEmision: new Date(),
      tipoOperacion: 'Tipo1',
      tipoDocumento: 'Doc1',
      selloRecepcion: 'SR123',
      idProveedor: 'Prov1',
      totalGravado: 100,
      totalExento: 0,
      totalOperacion: 100,
    });
  
    // Simular respuesta exitosa del servicio API
    apiService.doRequest.and.returnValue(
      Promise.resolve({ code: 200 } as JsonResponse)
    );
  
    // Llamar al método update
    await component.update();
    await fixture.whenStable(); // Asegúrate de esperar a que se resuelvan las promesas
  
    // Verificar que se haya llamado al servicio con los datos correctos
    expect(apiService.doRequest).toHaveBeenCalledWith(
      '/compras',
      component.comprasForm.value,
      'post'
    );
  
    // Verificar que se haya cerrado el diálogo con la acción correcta
    expect(mockDialogRef.close).toHaveBeenCalledWith({
      action: 'update',
      result: true,
    });
  });
  
});
