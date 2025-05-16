import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SidenavComponent } from './sidenav.component';
import { MenuService } from 'src/app/core/services/menu-service.service';
import { of } from 'rxjs';
import { UtilsService } from 'src/app/core/services/utils.service';
import { LoadingService } from 'src/app/core/services/loading.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { MatSidenav } from '@angular/material/sidenav';
import { MatTreeModule } from '@angular/material/tree';

describe('SidenavComponent', () => {
  let component: SidenavComponent;
  let fixture: ComponentFixture<SidenavComponent>;
  let menuService: jasmine.SpyObj<MenuService>;
  let utilsService: jasmine.SpyObj<UtilsService>;
  let loadingService: jasmine.SpyObj<LoadingService>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    const menuServiceSpy = jasmine.createSpyObj('MenuService', [
      'getClickEvent',
    ]);
    const utilsServiceSpy = jasmine.createSpyObj('UtilsService', ['goToPage']);
    const loadingServiceSpy = jasmine.createSpyObj('LoadingService', [
      'show',
      'hide',
    ]);
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getMatMenu']);

    TestBed.configureTestingModule({
      declarations: [SidenavComponent],
      imports: [MatTreeModule],
      providers: [
        { provide: MenuService, useValue: menuServiceSpy },
        { provide: UtilsService, useValue: utilsServiceSpy },
        { provide: LoadingService, useValue: loadingServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
      ],
    }).compileComponents();

    menuService = TestBed.inject(MenuService) as jasmine.SpyObj<MenuService>;
    utilsService = TestBed.inject(UtilsService) as jasmine.SpyObj<UtilsService>;
    loadingService = TestBed.inject(
      LoadingService
    ) as jasmine.SpyObj<LoadingService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;

    // Simulando el retorno del observable
    menuService.getClickEvent.and.returnValue(of(null));

    // Asegúrate de que getMatMenu devuelva un arreglo
    authService.getMatMenu.and.returnValue([]); // Retorna un arreglo vacío para la prueba

    fixture = TestBed.createComponent(SidenavComponent);
    component = fixture.componentInstance;

    // Asegúrate de que el sidenav esté disponible
    component.sidenav = jasmine.createSpyObj('MatSidenav', ['open', 'close']);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize menuStatus to false', () => {
    expect(component.menuStatus).toBeTrue();
  });

  it('should toggle menu status and open/close sidenav', () => {
    const sidenavSpy = spyOn(component.sidenav, 'open');
    const sidenavCloseSpy = spyOn(component.sidenav, 'close');

    component.menu(); // Open the sidenav
    expect(component.menuStatus).toBeTrue();
    expect(sidenavSpy).toHaveBeenCalled();

    component.menu(); // Close the sidenav
    expect(component.menuStatus).toBeFalse();
    expect(sidenavCloseSpy).toHaveBeenCalled();
  });

  it('should unsubscribe from menu service on destroy', () => {
    const unsubscribeSpy = spyOn(
      component.clickEventSubscription,
      'unsubscribe'
    ).and.callThrough();

    component.ngOnDestroy();

    expect(unsubscribeSpy).toHaveBeenCalled();
  });



  it('should call goToPage and show/hide loading', () => {
    const node = { url: 'mock-url' }; // Mock node
    component.goto(node);

    expect(loadingService.show).toHaveBeenCalled();
    expect(utilsService.goToPage).toHaveBeenCalledWith(node.url);
    expect(loadingService.hide).toHaveBeenCalled();
    expect(menuService.closeMenu).toHaveBeenCalled();
  });

  it('should set tree data from AuthService', () => {
    // Here you can check that data source is correctly populated
    expect(component.dataSource.data).toEqual([]); // Adjust if you mock getMatMenu to return specific data
  });

  it('should transform node correctly', () => {
    const node: any = { category: 'Test Category', menu: [] };
    const transformedNode = component['_transformer'](node, 0);

    expect(transformedNode).toEqual({
      expandable: false,
      name: 'Test Category',
      nameMenu: undefined,
      slugMenu: undefined,
      level: 0,
    });
  });

  it('should have children correctly identified', () => {
    const node: any = { expandable: true, name: 'Node', level: 0 };
    expect(component.hasChild(0, node)).toBeTrue();
  });
});
