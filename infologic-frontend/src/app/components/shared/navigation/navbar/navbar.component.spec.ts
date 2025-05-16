import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { MenuService } from 'src/app/core/services/menu-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ResetPasswordComponent } from 'src/app/pages/autentificacion/reset-password/reset-password.component';
import { ManageUserComponent } from 'src/app/pages/user/dialogs/manage-user/manage-user.component';
import { of } from 'rxjs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let menuServiceSpy: jasmine.SpyObj<MenuService>;
  let utilsServiceSpy: jasmine.SpyObj<UtilsService>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', [
      'getAuthInfo',
      'LogOut',
      'getRequiredInvoice',
    ]);
    menuServiceSpy = jasmine.createSpyObj('MenuService', ['sendClickEvent']);
    utilsServiceSpy = jasmine.createSpyObj('UtilsService', [
      'goToPage',
      'openModal',
    ]);
    apiServiceSpy = jasmine.createSpyObj('ApiService', ['logout']);

    await TestBed.configureTestingModule({
      declarations: [NavbarComponent],
      imports: [MatToolbarModule, MatIconModule, MatMenuModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: MenuService, useValue: menuServiceSpy },
        { provide: UtilsService, useValue: utilsServiceSpy },
        { provide: ApiService, useValue: apiServiceSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize username from auth service', () => {
    authServiceSpy.userName = 'testUser';
    component = new NavbarComponent(
      menuServiceSpy,
      authServiceSpy,
      utilsServiceSpy,
      apiServiceSpy
    );

    expect(component.username).toBe('testUser');
  });

  it('should get username from auth info if userName is empty', () => {
    authServiceSpy.userName = '';
    authServiceSpy.getAuthInfo.and.returnValue('authUser');

    component.userName(); // Invocar el método para establecer el nombre de usuario

    expect(component.username).toBe('authUser');
  });

  it('should call goToPage on home method', () => {
    component.home();
    expect(utilsServiceSpy.goToPage).toHaveBeenCalledWith('dashboard', false);
  });

  it('should call sendClickEvent on toggle method', () => {
    component.toggle();
    expect(menuServiceSpy.sendClickEvent).toHaveBeenCalled();
  });

  it('should open modal for password change', () => {
    component.changePass();
    expect(utilsServiceSpy.openModal).toHaveBeenCalledWith(
      {
        title: 'Actualizacion de contraseña',
        botonAceptar: 'Aceptar',
        componentToLoad: ResetPasswordComponent,
        callerComponent: component,
        data: null,
        footer: false,
      },
      '40%',
      '50%',
      '100%',
      '100%'
    );
  });

  // it('should open modal for profile update', () => {
  //   authServiceSpy.userName = 'testUser';
  //   authServiceSpy.getRequiredInvoice.and.returnValue('invoiceData');

  //   component.updateProfile();

  //   expect(utilsServiceSpy.openModal).toHaveBeenCalledWith(
  //     {
  //       title: 'Actualización de Usuario',
  //       botonAceptar: 'Actualizar',
  //       componentToLoad: ManageUserComponent,
  //       callerComponent: component,
  //       footer: false,
  //       data: {
  //         user: {
  //           idUser: 0,
  //           usuario: 'testUser',
  //         },
  //         requerido: 'invoiceData',
  //         origin: 'login',
  //       },
  //     }
  //   );
  // });

  it('should call logout method', async () => {
    apiServiceSpy.logout.and.returnValue(
      Promise.resolve({ data: [], code: 200, message: 'simon' })
    );
    authServiceSpy.LogOut.and.callThrough(); // Permitir que se llame al método original

    await component.logout();

    expect(apiServiceSpy.logout).toHaveBeenCalled();
    expect(authServiceSpy.LogOut).toHaveBeenCalled();
  });
});
