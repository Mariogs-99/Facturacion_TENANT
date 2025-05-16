import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PermissionsComponent } from './permissions.component';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { DataService } from 'src/app/core/services/data.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { of } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Permisos } from 'src/app/core/model/permisos/permisos.model';
import { PageEvent } from '@angular/material/paginator';

describe('PermissionsComponent', () => {
  let component: PermissionsComponent;
  let fixture: ComponentFixture<PermissionsComponent>;
  let apiServiceMock: jasmine.SpyObj<ApiService>;
  let utilsServiceMock: jasmine.SpyObj<UtilsService>;
  let dataServiceMock: jasmine.SpyObj<DataService>;
  let authServiceMock: jasmine.SpyObj<AuthService>;

  const mockJsonResponse: JsonResponse = {
    code: 200,
    data: {
      pages: {
        content: [
          {
            idPermissions: 1,
            namePermissions: 'Permission 1',
            descriptionPermissions: 'Description 1',
          },
        ],
        totalElements: 1,
      },
    },
    message: 'Success',
  };

  beforeEach(async () => {
    apiServiceMock = jasmine.createSpyObj('ApiService', [
      'getPage',
      'doRequest',
    ]);
    utilsServiceMock = jasmine.createSpyObj('UtilsService', [
      'showSWAL',
      'openModal',
    ]);
    dataServiceMock = jasmine.createSpyObj('DataService', ['getData']);
    authServiceMock = jasmine.createSpyObj('AuthService', ['getPermissions']);

    authServiceMock.getPermissions.and.returnValue([
      'Index',
      'Actualizar',
      'Eliminar',
    ]);

    apiServiceMock.getPage.and.returnValue(Promise.resolve(mockJsonResponse));
    // utilsServiceMock.showSWAL.and.returnValue(Promise.resolve(true));
    dataServiceMock.getData.and.returnValue(
      of({ component: 'PermissionsComponent', action: 'open' })
    );

    await TestBed.configureTestingModule({
      declarations: [PermissionsComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceMock },
        { provide: UtilsService, useValue: utilsServiceMock },
        { provide: DataService, useValue: dataServiceMock },
        { provide: AuthService, useValue: authServiceMock },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize permissions on ngOnInit', async () => {
    await component.ngOnInit();
    expect(apiServiceMock.getPage).toHaveBeenCalledWith(
      '/permissions/page',
      undefined,
      undefined,
      0,
      10
    );
    expect(component.dataSource.data.length).toBe(1);
    expect(component.totalRecordsPermission).toBe(1);
  });

  it('should call indexPermissions on subscribe', async () => {
    await component.ngOnInit();
    expect(apiServiceMock.getPage).toHaveBeenCalledTimes(1);
  });

  it('should open modal on update', () => {
    const mockPermission: Permisos = {
      idPermissions: 1,
      namePermissions: 'Permission 1',
      descriptionPermissions: 'Description 1',
    };
    component.update(mockPermission, null, true);
    expect(utilsServiceMock.openModal).toHaveBeenCalled();
  });

  it('should delete permission and call indexPermissions', async () => {
    const mockPermission: Permisos = {
      idPermissions: 1,
      namePermissions: 'Permission 1',
      descriptionPermissions: 'Description 1',
    };
    apiServiceMock.doRequest.and.returnValue(
      Promise.resolve({ data: {}, message: '', code: 200 })
    );

    spyOn(component, 'indexPermissions').and.callThrough(); // Spy on the indexPermissions method
    await component.delete(mockPermission, null);
    expect(utilsServiceMock.SWALYESNO).toHaveBeenCalled();
    expect(apiServiceMock.doRequest).toHaveBeenCalledWith(
      '/permissions/1',
      {},
      'delete'
    );
    expect(component.indexPermissions).toHaveBeenCalled(); // Check if indexPermissions is called
  });

  it('should handle pagination', async () => {
    const mockPageEvent = { pageIndex: 1, pageSize: 10 } as PageEvent;
    await component.paginatePermission(mockPageEvent);
    expect(component.paginationSizePermission).toBe(10);
    expect(component.currentPageNoPermission).toBe(1);
    expect(apiServiceMock.getPage).toHaveBeenCalledWith(
      '/permissions/page',
      undefined,
      undefined,
      1,
      10
    );
  });

  afterEach(() => {
    component.ngOnDestroy();
  });
});
