import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { of, throwError } from 'rxjs';
import { ResetPasswordComponent } from '../reset-password/reset-password.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let utilsServiceSpy: jasmine.SpyObj<UtilsService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', [
      'getRequiredInvoice',
    ]);
    apiServiceSpy = jasmine.createSpyObj('ApiService', ['login']);
    utilsServiceSpy = jasmine.createSpyObj('UtilsService', [
      'showSWAL',
      'openModal',
      'SWALYESNO',
    ]);
    routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl', 'navigate']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [MatFormFieldModule, MatInputModule, BrowserAnimationsModule, MatIconModule],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: UtilsService, useValue: utilsServiceSpy },
        { provide: Router, useValue: routerSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize login form', () => {
    expect(component.loginForm).toBeTruthy();
    expect(component.loginForm.controls['usuario']).toBeDefined();
    expect(component.loginForm.controls['clave']).toBeDefined();
  });

  it('should disable enter key', () => {
    const event = new KeyboardEvent('keydown', { key: 'Enter' });
    component.disableEnter(event);
    expect(event.defaultPrevented).toBeFalse();
  });


  it('should handle login error with code 401', async () => {
    const errorResponse = {
      error: {
        code: 401,
        data: 'Unauthorized access',
      },
    };
    apiServiceSpy.login.and.returnValue(Promise.reject(errorResponse));

    await component.login();

    expect(utilsServiceSpy.showSWAL).toHaveBeenCalledWith(
      'Acceso Inválido',
      'Unauthorized access',
      'Aceptar',
      'error'
    );
  });

  it('should handle general error on login', async () => {
    const errorResponse = {
      error: {
        data: {
          error: {
            codigo: 4,
          },
        },
      },
    };
    apiServiceSpy.login.and.returnValue(Promise.reject(errorResponse));

    await component.login();

    expect(utilsServiceSpy.showSWAL).toHaveBeenCalledWith(
      'Ha ocurrido un problema',
      'Debes cambiar tu contraseña para continuar',
      'Aceptar',
      'error'
    );
  });

  it('should call changePassword method', () => {
    spyOn(utilsServiceSpy, 'openModal');
    const user = { idUser: 1, username: 'testUser' };
    component.changePassword(user);

    expect(utilsServiceSpy.openModal).toHaveBeenCalledWith(
      {
        title: 'Actualizacion de contraseña',
        botonAceptar: 'Aceptar',
        componentToLoad: ResetPasswordComponent,
        callerComponent: component,
        data: {
          user: user,
          from: 'root',
          log: 'no-log',
        },
        footer: false,
      },
      '40%',
      '50%',
      '100%',
      '100%'
    );
  });
});
