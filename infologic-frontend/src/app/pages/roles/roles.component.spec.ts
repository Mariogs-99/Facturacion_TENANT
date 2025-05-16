import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RolesComponent } from './roles.component';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { of } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { ModuleHeaderComponent } from 'src/app/components/base/module-header/module-header.component';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('RolesComponent', () => {
  let component: RolesComponent;
  let fixture: ComponentFixture<RolesComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let dataServiceSpy: jasmine.SpyObj<DataService>;
  let utilsServiceSpy: jasmine.SpyObj<UtilsService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    apiServiceSpy = jasmine.createSpyObj('ApiService', [
      'getPage',
      'doRequest',
    ]);
    dataServiceSpy = jasmine.createSpyObj('DataService', ['getData']);
    utilsServiceSpy = jasmine.createSpyObj('UtilsService', [
      'showSWAL',
      'openModal',
      'SWALYESNO',
    ]);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getPermissions']);

    // Mocking necessary services and their methods
    authServiceSpy.getPermissions.and.returnValue([
      'Index',
      'Actualizar',
      'Eliminar',
    ]);
    dataServiceSpy.getData.and.returnValue(
      of({ component: 'RolesComponent', action: 'open' })
    );
    const mockResponse: JsonResponse = {
      code: 200,
      message: '',
      data: {
        pages: {
          content: [
            { idRole: 1, nameRole: 'Admin', descriptionRole: 'Administrator' },
          ],
          totalElements: 1,
        },
      },
    };
    apiServiceSpy.getPage.and.returnValue(Promise.resolve(mockResponse));

    await TestBed.configureTestingModule({
      declarations: [RolesComponent],
      imports: [
        MatPaginatorModule,
        MatSortModule,
        MatTableModule,
        BrowserAnimationsModule,
        ModuleHeaderComponent,
      ],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: DataService, useValue: dataServiceSpy },
        { provide: UtilsService, useValue: utilsServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RolesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Initialize the component
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with permissions and load roles', async () => {
    await component.indexRol();
    expect(apiServiceSpy.getPage).toHaveBeenCalled();
    expect(component.dataSource.data).toEqual([
      {
        idPermissions: 1,
        namePermissions: 'Admin',
        descriptionPermissions: 'Administrator',
      },
    ]);
    expect(component.totalRecordsRoles).toBe(1);
  });

  it('should handle error when loading roles', async () => {
    apiServiceSpy.getPage.and.returnValue(
      Promise.resolve({ code: 500, message: '', data: {} })
    );
    await component.indexRol();
    expect(utilsServiceSpy.showSWAL).toHaveBeenCalledWith(
      'Ha ocurrido un error',
      '',
      'Aceptar',
      'warning'
    );
  });



  it('should show a warning when trying to delete a role', async () => {
    await component.deleteRoles({ idRole: 1, nameRole: 'Admin' }, 0);
    expect(utilsServiceSpy.SWALYESNO).toHaveBeenCalled();
  });
});
