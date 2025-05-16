import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ResetPasswordComponent } from './reset-password.component';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { PasswordValidatorService } from 'src/app/core/services/password-validator.service';
import {
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogModule,
} from '@angular/material/dialog';
import { UtilsService } from 'src/app/core/services/utils.service'; // Importar el servicio UtilsService
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;

  const dialogRefMock = {
    close: jasmine.createSpy('close'),
  };

  const mockDialogData = {
    data: {
      from: 'root',
      log: 'no-log',
      user: 'testuser',
    },
  };

  const passwordValidatorServiceMock = {
    passwordMatchValidator: jasmine
      .createSpy('passwordMatchValidator')
      .and.callFake((password: string, confirmPassword: string) => {
        return (formGroup: any) => {
          const passwordControl = formGroup.controls[password];
          const confirmPasswordControl = formGroup.controls[confirmPassword];

          if (passwordControl.value !== confirmPasswordControl.value) {
            confirmPasswordControl.setErrors({ mismatch: true });
          } else {
            confirmPasswordControl.setErrors(null);
          }
        };
      }),
  };

  const utilsServiceMock = {
    SWALYESNO: jasmine
      .createSpy('SWALYESNO')
      .and.returnValue(Promise.resolve(true)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        HttpClientModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
      ], // Asegúrate de importar MatDialogModule
      declarations: [ResetPasswordComponent],
      providers: [
        FormBuilder,
        {
          provide: PasswordValidatorService,
          useValue: passwordValidatorServiceMock,
        },
        { provide: MatDialogRef, useValue: dialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: mockDialogData },
        { provide: HttpClient },
        { provide: UtilsService, useValue: utilsServiceMock }, // Proveer UtilsService mock
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });
  it('form should be invalid when empty', () => {
    expect(component.resetForm.invalid).toBeTrue();
  });

  it('clave field validity', () => {
    const claveControl = component.resetForm.get('clave');
    claveControl?.setValue('');
    expect(claveControl?.errors?.['required']).toBeTruthy();

    claveControl?.setValue('short');
    expect(claveControl?.errors?.['minlength']).toBeTruthy();

    claveControl?.setValue('ThisIsAValidPassword');
    expect(claveControl?.errors).toBeNull();
  });

  it('nuevaClave field validity', () => {
    const nuevaClaveControl = component.resetForm.get('nuevaClave');
    nuevaClaveControl?.setValue('');
    expect(nuevaClaveControl?.errors?.['required']).toBeTruthy();

    nuevaClaveControl?.setValue('short');
    expect(nuevaClaveControl?.errors?.['minlength']).toBeTruthy();

    nuevaClaveControl?.setValue('thisIsWeak');
    component.resetForm.get('claveConfirmada')?.setValue('differentPassword');
    expect(component.resetForm.get('claveConfirmada')?.errors).toEqual({
      mismatch: true,
    }); // Test password mismatch

    nuevaClaveControl!.setValue('ThisIsAValidPassword');
    component.resetForm
      .get('claveConfirmada')
      ?.setValue('ThisIsAValidPassword'); // Set matching password
    expect(component.resetForm.get('claveConfirmada')?.errors).toBeNull();
  });
  it('claveConfirmada field validity', () => {
    const claveConfirmadaControl = component.resetForm.get('claveConfirmada');
    claveConfirmadaControl?.setValue('');
    expect(claveConfirmadaControl?.errors?.['required']).toBeTruthy();

    // Set a mismatched value to trigger the password mismatch error
    claveConfirmadaControl?.setValue('ThisDoesntMatch');
    component.resetForm.get('nuevaClave')?.setValue('ThisIsAValidPassword');
    // Check for passwordMismatch error instead of mismatch
    expect(claveConfirmadaControl?.errors).toEqual({ passwordMismatch: true });

    // Set matching passwords to clear errors
    claveConfirmadaControl?.setValue('ThisIsAValidPassword');
    component.resetForm.get('nuevaClave')?.setValue('ThisIsAValidPassword');
    expect(claveConfirmadaControl?.errors).toBeNull();
  });
});
