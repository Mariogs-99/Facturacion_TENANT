import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InstitucionComponent } from './institucion.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatAccordion } from '@angular/material/expansion';
import { AuthService } from 'src/app/core/services/auth.service';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { DataService } from 'src/app/core/services/data.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { of } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ModuleHeaderComponent } from 'src/app/components/base/module-header/module-header.component';
import { MaterialModule } from 'src/app/material.module';

describe('InstitucionComponent', () => {
  let component: InstitucionComponent;
  let fixture: ComponentFixture<InstitucionComponent>;
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockApiService: jasmine.SpyObj<ApiService>;
  let mockUtilsService: jasmine.SpyObj<UtilsService>;
  let mockDialog: jasmine.SpyObj<MatDialog>;
  let mockDataService: jasmine.SpyObj<DataService>;
  let mockSharedDataService: jasmine.SpyObj<SharedDataService>;

  beforeEach(async () => {
    mockAuthService = jasmine.createSpyObj('AuthService', ['getPermissions']);
    mockApiService = jasmine.createSpyObj('ApiService', ['doRequest']);
    mockUtilsService = jasmine.createSpyObj('UtilsService', ['showSWAL']);
    mockDialog = jasmine.createSpyObj('MatDialog', ['closeAll']);
    mockDataService = jasmine.createSpyObj('DataService', ['getData']);
    mockSharedDataService = jasmine.createSpyObj('SharedDataService', [
      'setVariable',
    ]);

    // Corrección: Ajustar los retornos de los mocks a los tipos esperados
    mockAuthService.getPermissions.and.returnValue(['READ', 'WRITE']);
    mockDataService.getData.and.returnValue(
      of({ component: 'InstitucionComponent', action: 'open' })
    );
    mockSharedDataService.setVariable.and.returnValue(undefined);

    // Asegurarse de que `doRequest` devuelva una `Promise` correctamente.
    mockApiService.doRequest.and.returnValue(
      Promise.resolve({
        message: '',
        code: 200,
        data: {
          company: { nameCompany: 'Test Company' },
          catalogos: {
            municipios: [],
            giros: [],
            recintos: [],
            regimens: [],
          },
        },
      })
    );

    await TestBed.configureTestingModule({
      declarations: [InstitucionComponent, MatAccordion],
      imports: [MaterialModule, ModuleHeaderComponent, BrowserAnimationsModule],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: ApiService, useValue: mockApiService },
        { provide: UtilsService, useValue: mockUtilsService },
        { provide: MatDialog, useValue: mockDialog },
        { provide: DataService, useValue: mockDataService },
        { provide: SharedDataService, useValue: mockSharedDataService },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InstitucionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('should initialize the form on component creation', () => {
  //   expect(component.institucionForm).toBeDefined();
  //   expect(component.institucionForm.controls['nameCompany']).toBeDefined();
  // });

  // // it('should toggle disable status on subscription event', () => {
  // //   expect(component.disable).toBeFalse();
  // //   expect(component.isupdating).toBeTrue();
  // // });

  // // it('should call inicializartabla on ngOnInit', () => {
  // //   spyOn(component, 'inicializartabla');
  // //   component.ngOnInit();
  // //   expect(component.inicializartabla).toHaveBeenCalled();
  // // });

  // it('should set filteredOptions when filtervaluesInstitucion is called', () => {
  //   component.municipios = [
  //     { id: 1, name: 'Municipio 1' },
  //     { id: 2, name: 'Municipio 2' },
  //   ];
  //   component.filtervaluesInstitucion('Municipio 1');
  //   expect(component.filteredOptions).toEqual([{ id: 1, name: 'Municipio 1' }]);
  // });

  // it('should call the API to fetch data in inicializartabla', async () => {
  //   mockApiService.doRequest.and.returnValue(
  //     Promise.resolve({
  //       message: '',
  //       code: 200,
  //       data: {
  //         company: { nameCompany: 'Test Company' },
  //         catalogos: {
  //           municipios: [],
  //           giros: [],
  //           recintos: [],
  //           regimens: [],
  //         },
  //       },
  //     })
  //   );

  //   await component.inicializartabla();
  //   expect(mockApiService.doRequest).toHaveBeenCalledWith(
  //     '/company/find/1',
  //     component.companies,
  //     'get'
  //   );
  //   expect(component.companies.nameCompany).toEqual('Test Company');
  // });

  // it('should validate required fields and show warning on guardar', () => {
  //   spyOn(component, 'setCompanies');
  //   component.institucionForm.controls['nameCompany'].setValue('');
  //   component.guardar();
  //   expect(mockUtilsService.showSWAL).toHaveBeenCalledWith(
  //     'Ingrese el Nombre de la Compañia',
  //     'Debe Escribir un nombre válido',
  //     'OK',
  //     'warning'
  //   );
  // });

  // it('should set companies correctly on setCompanies', () => {
  //   component.institucionForm.setValue({
  //     nameCompany: 'Test Company',
  //     emailCompany: 'test@example.com',
  //     descriptionCompany: 'Description',
  //     nit: '123456789',
  //     nrc: '12345',
  //     addressCompany: '123 Street',
  //     phoneCompany: '1234567890',
  //     idGiroCompany: '1',
  //     nombreGiroCompany: 'Giro',
  //     idRecinto: '1',
  //     idRegimen: '1',
  //     nombreRecintoCompany: 'Recinto',
  //     nombreRegimenCompany: 'Regimen',
  //     socialReasonCompany: 'Social Reason',
  //     idMuniCompany: '1',
  //     nombreMunicipioCompany: 'Municipio',
  //     idCompany: '1',
  //     file: '',
  //     mhUser: 'mhUser',
  //     mhPass: 'mhPass',
  //     clavePrimariaCert: 'claveCert',
  //     active: true,
  //   });

  //   component.setCompanies();
  //   expect(component.companies.nameCompany).toEqual('Test Company');
  // });

  // it('should close modal and reset form on closeModal', () => {
  //   component.closeModal();
  //   expect(mockDialog.closeAll).toHaveBeenCalled();
  //   expect(component.institucionForm.pristine).toBeTrue();
  // });
});
