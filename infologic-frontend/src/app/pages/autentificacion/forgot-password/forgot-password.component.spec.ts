import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotPasswordComponent } from './forgot-password.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MaterialModule } from 'src/app/material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';

describe('ForgotPasswordComponent', () => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ForgotPasswordComponent>;
  let apiService: jasmine.SpyObj<ApiService>;
  let utilsService: jasmine.SpyObj<UtilsService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(() => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['login']);
    const utilsServiceSpy = jasmine.createSpyObj('UtilsService', ['showSWAL']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MaterialModule,
        BrowserAnimationsModule,
      ],
      declarations: [ForgotPasswordComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: UtilsService, useValue: utilsServiceSpy },
        { provide: Router, useValue: routerSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    utilsService = TestBed.inject(UtilsService) as jasmine.SpyObj<UtilsService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form correctly', () => {
    expect(component.recoverForm).toBeDefined();
    expect(component.recoverForm.controls['usuario'].valid).toBeFalse(); // Should be invalid initially
    expect(component.recoverForm.controls['usuario'].validator).toBeDefined();
  });

  it('should call disableEnter and prevent default action', () => {
    const event = new KeyboardEvent('keydown');
    spyOn(event, 'preventDefault');
    spyOn(event, 'stopPropagation');
    component.disableEnter(event);
    expect(event.preventDefault).toHaveBeenCalled();
    expect(event.stopPropagation).toHaveBeenCalled();
  });

  it('should call recuperar and navigate to login on success', async () => {
    const mockResponse = { code: 200, message: '', data: {} };
    apiService.login.and.returnValue(Promise.resolve(mockResponse)); // Mock de la respuesta
    utilsService.showSWAL.and.returnValue(Promise.resolve()); // Asegúrate de que esto devuelva una promesa resuelta

    await component.recuperar(); // Llama al método recuperar

    expect(apiService.login).toHaveBeenCalledWith(
      component.recoverForm.value,
      'recuperar-sisucc'
    );
    expect(utilsService.showSWAL).toHaveBeenCalledWith(
      'Correo enviado',
      'para completar este proceso debes revisar tu correo',
      'Aceptar',
      'success'
    );
    expect(router.navigateByUrl).toHaveBeenCalledWith('login'); // Comprueba que se llama a navigateByUrl
  });

  it('should show error alert when recuperar fails', async () => {
    const mockError = { error: { data: { error: 'Error message' } } };
    apiService.login.and.returnValue(Promise.reject(mockError));
    utilsService.showSWAL.and.returnValue(Promise.resolve());

    await component.recuperar();

    expect(apiService.login).toHaveBeenCalledWith(
      component.recoverForm.value,
      'recuperar-sisucc'
    );
    expect(utilsService.showSWAL).toHaveBeenCalledWith(
      'Ha ocurrido un problema',
      'Error message',
      'Aceptar',
      'error'
    );
  });

  it('should show error alert when recuperar fails', async () => {
    const errorResponse = {
      error: {
        data: {
          error: 'Error message', // Asegúrate de que este mensaje coincida con el que se espera
        },
      },
    };
    
    apiService.login.and.returnValue(Promise.reject(errorResponse)); // Mock de la respuesta de error
  
    await component.recuperar(); // Llama al método recuperar
  
    expect(apiService.login).toHaveBeenCalledWith(component.recoverForm.value, 'recuperar-sisucc');
    expect(utilsService.showSWAL).toHaveBeenCalledWith(
      'Ha ocurrido un problema',
      'Error message', // Asegúrate de que coincida con lo que estás usando en tu método
      'Aceptar',
      'error'
    );
  });
  
});
