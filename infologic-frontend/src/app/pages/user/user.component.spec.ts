import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { of, Subscription } from 'rxjs';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { DataService } from 'src/app/core/services/data.service';
import { FormBuilder } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserComponent } from './user.component';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { User } from 'src/app/core/model/users/user.model';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('UserComponent', () => {
  let component: UserComponent;
  let fixture: ComponentFixture<UserComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let utilsServiceSpy: jasmine.SpyObj<UtilsService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let dataServiceSpy: jasmine.SpyObj<DataService>;

  beforeEach(async () => {
    const apiSpy = jasmine.createSpyObj('ApiService', ['getPage', 'doRequest']);
    const utilsSpy = jasmine.createSpyObj('UtilsService', [ 'openModal']);
    const authSpy = jasmine.createSpyObj('AuthService', ['getPermissions']);
    const dataSpy = jasmine.createSpyObj('DataService', ['getData']);
    
    await TestBed.configureTestingModule({
      declarations: [UserComponent],
      providers: [
        FormBuilder,
        { provide: ApiService, useValue: apiSpy },
        { provide: UtilsService, useValue: utilsSpy },
        { provide: AuthService, useValue: authSpy },
        { provide: DataService, useValue: dataSpy },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(UserComponent);
    component = fixture.componentInstance;
    apiServiceSpy = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    utilsServiceSpy = TestBed.inject(UtilsService) as jasmine.SpyObj<UtilsService>;
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    dataServiceSpy = TestBed.inject(DataService) as jasmine.SpyObj<DataService>;

    authServiceSpy.getPermissions.and.returnValue(['Index', 'Actualizar', 'Eliminar']);
    dataServiceSpy.getData.and.returnValue(of({}));
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize permissions and displayed columns', () => {
    component.ngOnInit();
    expect(component.permissionsArrUser).toContain('Actualizar');
    expect(component.displayedColumns).toContain('actions');
  });

  it('should call indexUser on initialization', () => {
    spyOn(component, 'indexUser');
    component.ngOnInit();
    expect(component.indexUser).toHaveBeenCalled();
  });

  it('should fetch users from API and populate the dataSource', async () => {
    const mockResponse: JsonResponse = {
      code: 200,
      data: {
        pages: {
          content: [{ email: 'test@example.com', usuario: 'testuser' }],
          totalElements: 1,
        },
      },
      message: 'Success'
    };
    apiServiceSpy.getPage.and.returnValue(Promise.resolve(mockResponse));

    await component.indexUser();
    expect(apiServiceSpy.getPage).toHaveBeenCalled();
    expect(component.dataSource.data.length).toBe(1);
    expect(component.totalRecords).toBe(1);
  });

  it('should call update when a user is updated', () => {
    const mockUser: User = { idUser: 1, email: 'test@example.com' } as User;
    spyOn(utilsServiceSpy, 'openModal').and.returnValue({
      afterClosed: () => of({ action: 'update', result: true }),
    } as any);

    component.update(mockUser, 0);
    expect(utilsServiceSpy.openModal).toHaveBeenCalled();
  });

  it('should call delete and remove a user', async () => {
    const mockUser: User = { idUser: 1, firstname: 'Test' } as User;
    spyOn(utilsServiceSpy, 'SWALYESNO').and.returnValue(Promise.resolve(true));
    const mockResponse: JsonResponse = { code: 200, data: {}, message: 'Deleted' };
    apiServiceSpy.doRequest.and.returnValue(Promise.resolve(mockResponse));

    await component.delete(mockUser, 0);
    expect(apiServiceSpy.doRequest).toHaveBeenCalledWith('/users/1', {}, 'delete');
  });

  it('should paginate data on page event', () => {
    spyOn(component, 'indexUser');
    const pageEvent: PageEvent = { pageIndex: 1, pageSize: 25, length: 50 };
    component.paginate(pageEvent);
    expect(component.indexUser).toHaveBeenCalledWith(1, 25);
  });

  it('should change user status when changeChk is called', async () => {
    const mockUser: User = { idUser: 1 } as User;
    spyOn(utilsServiceSpy, 'SWALYESNO').and.returnValue(Promise.resolve(true));
    const mockResponse: JsonResponse = { code: 200, data: {}, message: 'Status Updated' };
    apiServiceSpy.doRequest.and.returnValue(Promise.resolve(mockResponse));

    const event = { checked: true } as any;
    await component.changeChk(event, 'isActive', mockUser);
    expect(apiServiceSpy.doRequest).toHaveBeenCalled();
  });
});
