import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageUserComponent } from './manage-user.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { of, throwError } from 'rxjs';
import { MaterialModule } from 'src/app/material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('ManageUserComponent', () => {
  let component: ManageUserComponent;
  let fixture: ComponentFixture<ManageUserComponent>;

  const mockDialogRef = {
    close: jasmine.createSpy('close'),
  };

  const mockApiService = {
    doRequest: jasmine
      .createSpy('doRequest')
      .and.returnValue(Promise.resolve({ code: 200, data: {} })),
    getPage: jasmine.createSpy('getPage').and.returnValue(
      Promise.resolve({
        code: 200,
        data: { pages: { content: [], totalElements: 0 } },
      })
    ),
  };

  const mockUtilsService = {
    showSWAL: jasmine.createSpy('showSWAL'),
  };

  const mockAuthService = {
    setLocalData: jasmine.createSpy('setLocalData'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MaterialModule,
        BrowserAnimationsModule,
        NgxMaskDirective,
        NgxMaskPipe,
      ],
      declarations: [ManageUserComponent],
      providers: [
        { provide: MatDialogRef, useValue: mockDialogRef },
        {
          provide: MAT_DIALOG_DATA,
          useValue: { data: { user: {}, origin: '', requerido: {} } },
        },
        { provide: ApiService, useValue: mockApiService },
        { provide: UtilsService, useValue: mockUtilsService },
        { provide: AuthService, useValue: mockAuthService },
        provideNgxMask(),
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  // it('should initialize form controls', () => {
  //   expect(component.userForm).toBeDefined();
  //   expect(component.userForm.controls['firstname']).toBeDefined();
  //   expect(component.userForm.controls['lastname']).toBeDefined();
  // });

  // it('should call index on initialization', async () => {
  //   // Arrange: Create a mock response for the API
  //   const mockResponse = {
  //     message: '',
  //     code: 200,
  //     data: {
  //       roles: [
  //         { idRole: 1, nameRole: 'Admin', descriptionRole: 'Administrator' },
  //       ],
  //       docTypes: [{ idCatalogo: 1, alterno: 'DNI' }],
  //       company: [{ idCompany: 1, name: 'Company A' }],
  //       branch: [{ idSucursal: 1, name: 'Branch A' }],
  //       cargoAdmin: [{ idCatalogo: 1, name: 'Admin' }],
  //     },
  //   };

  //   mockApiService.doRequest.and.returnValue(Promise.resolve(mockResponse));

  //   spyOn(component, 'index').and.callThrough();
  //   await component.ngAfterViewInit();

  //   expect(component.index).toHaveBeenCalled();

  //   await component.index();

  //   expect(mockApiService.doRequest).toHaveBeenCalledWith(
  //     '/users/list',
  //     {},
  //     'get'
  //   );

  //   expect(component.roles).toEqual(mockResponse.data.roles);
  //   expect(component.docTypes).toEqual(mockResponse.data.docTypes);
  //   expect(component.companies).toEqual(mockResponse.data.company);
  //   expect(component.branches).toEqual(mockResponse.data.branch);
  //   expect(component.ranges).toEqual(mockResponse.data.cargoAdmin);
  // });

  // it('should set user data', async () => {
  //   const userMock = {
  //     idUser: 1,
  //     firstname: 'John',
  //     lastname: 'Doe',
  //     email: 'john.doe@example.com',
  //     isActive: true,
  //     testMode: false,
  //     rolIds: [],
  //   };
  //   mockApiService.doRequest.and.returnValue(
  //     Promise.resolve({
  //       code: 200,
  //       data: {
  //         user: userMock,
  //         roles: [],
  //         docTypes: [],
  //         company: [],
  //         branch: [],
  //         cargoAdmin: [],
  //       },
  //     })
  //   );

  //   await component.setUser(1);
  //   expect(component.userForm.value.firstname).toBe(userMock.firstname);
  //   expect(component.userForm.value.lastname).toBe(userMock.lastname);
  //   expect(component.user).toEqual(userMock);
  // });

  // it('should update user', async () => {
  //   component.userForm.patchValue({
  //     idUser: 1,
  //     firstname: 'John',
  //     lastname: 'Doe',
  //     email: 'john.doe@example.com',
  //     rolIds: [1],
  //   });
  //   mockApiService.doRequest.and.returnValue(
  //     Promise.resolve({ code: 200, data: {} })
  //   );

  //   await component.update();
  //   expect(mockApiService.doRequest).toHaveBeenCalledWith(
  //     '/users/1',
  //     component.userForm.value,
  //     'put'
  //   );
  // });

  // it('should handle update error', async () => {
  //   component.userForm.patchValue({
  //     idUser: 1,
  //     firstname: 'John',
  //     lastname: 'Doe',
  //     email: 'john.doe@example.com',
  //     rolIds: [1],
  //   });
  //   mockApiService.doRequest.and.returnValue(
  //     Promise.resolve({ code: 400, data: {}, message: 'Error updating' })
  //   );

  //   await component.update();
  //   expect(mockUtilsService.showSWAL).toHaveBeenCalledWith(
  //     'No se actualizo el Usuario',
  //     'Error updating',
  //     'Aceptar',
  //     'error'
  //   );
  // });
});
