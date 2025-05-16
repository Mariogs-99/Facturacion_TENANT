import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { ModalheaderComponent } from './modalheader.component';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('ModalheaderComponent', () => {
  let component: ModalheaderComponent;
  let fixture: ComponentFixture<ModalheaderComponent>;
  let mockDialog: jasmine.SpyObj<MatDialog>;

  beforeEach(async () => {
    // Creamos un mock de MatDialog para espiar las funciones
    mockDialog = jasmine.createSpyObj('MatDialog', ['closeAll']);

    await TestBed.configureTestingModule({
      declarations: [ModalheaderComponent],
      imports: [
        MatIconModule,
        MatToolbarModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: MatDialog, useValue: mockDialog } // Inyectamos el mock de MatDialog
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalheaderComponent);
    component = fixture.componentInstance;
    // Simulamos la data del modal
    component.data = { title: 'Test Modal Title' }; 
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should display the correct title', () => {
    // Corregimos la clase del título
    const titleElement: HTMLElement = fixture.nativeElement.querySelector('.modal-tittle');
    expect(titleElement.textContent).toContain('Test Modal Title');
  });

  it('should close the dialog when close button is clicked', () => {
    // Simulamos el clic en el botón de cerrar
    const closeButton = fixture.nativeElement.querySelector('button[mat-icon-button]');
    closeButton.click();
    expect(mockDialog.closeAll).toHaveBeenCalled();
  });
});
