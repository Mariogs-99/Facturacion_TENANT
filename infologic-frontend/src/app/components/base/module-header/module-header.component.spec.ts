import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModuleHeaderComponent } from './module-header.component';
import { DataService } from 'src/app/core/services/data.service';

describe('ModuleHeaderComponent', () => {
  let component: ModuleHeaderComponent;
  let fixture: ComponentFixture<ModuleHeaderComponent>;
  let mockDataService: jasmine.SpyObj<DataService>;

  beforeEach(async () => {
    mockDataService = jasmine.createSpyObj('DataService', ['sendMessage']);

    await TestBed.configureTestingModule({
      imports: [ModuleHeaderComponent], // Aquí se debe importar el componente standalone
      providers: [{ provide: DataService, useValue: mockDataService }],
    }).compileComponents();

    fixture = TestBed.createComponent(ModuleHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Esto es necesario para inicializar correctamente el componente
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should set default values for inputs', () => {
    expect(component.title).toBe('sin nombre');
    expect(component.subtitle).toBe('sin nombre');
    expect(component.component).toBe('');
    expect(component.action).toBe('');
    expect(component.button).toBe(false);
    expect(component.buttonLabel).toBe('sin nombre');
    expect(component.buttonColor).toBe('primary');
    expect(component.buttonAction).toBe('');
    expect(component.buttonIcon).toBe(null);
  });

  describe('sendMessage', () => {
    it('should call sendMessage on DataService with correct parameters', () => {
      component.component = 'TestComponent';
      component.action = 'TestAction';

      component.sendMessage();

      expect(mockDataService.sendMessage).toHaveBeenCalledWith({
        from: 'ModuleHeaderComponent',
        component: 'TestComponent',
        action: 'TestAction',
      });
    });
  });

  describe('cambiarValorButton', () => {
    it('should change the value of button', () => {
      component.cambiarValorButton(true);
      expect(component.button).toBe(true);

      component.cambiarValorButton(false);
      expect(component.button).toBe(false);
    });
  });
});
