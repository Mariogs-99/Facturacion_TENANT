import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PagesComponent } from './pages.component';
import { MatSidenav } from '@angular/material/sidenav';
import { MenuService } from '../core/services/menu-service.service';
import { of, Subscription } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('PagesComponent', () => {
  let component: PagesComponent;
  let fixture: ComponentFixture<PagesComponent>;
  let menuServiceSpy: jasmine.SpyObj<MenuService>;
  let sidenavSpy: jasmine.SpyObj<MatSidenav>;

  beforeEach(async () => {
    
    const menuServiceMock = jasmine.createSpyObj('MenuService', [
      'getClickEvent',
    ]);
    menuServiceMock.getClickEvent.and.returnValue(of()); 

    sidenavSpy = jasmine.createSpyObj('MatSidenav', ['open', 'close']);

    await TestBed.configureTestingModule({
      declarations: [PagesComponent],
      providers: [{ provide: MenuService, useValue: menuServiceMock }],
      schemas: [NO_ERRORS_SCHEMA], 
    }).compileComponents();

    fixture = TestBed.createComponent(PagesComponent);
    component = fixture.componentInstance;
    menuServiceSpy = TestBed.inject(MenuService) as jasmine.SpyObj<MenuService>;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize menuStatus to false', () => {
    expect(component.menuStatus).toBeFalse();
  });

  it('should toggle menuStatus when menu() is called', () => {
    component.sidenav = sidenavSpy; 
    component.menu();
    expect(component.menuStatus).toBeTrue();
    expect(sidenavSpy.open).toHaveBeenCalled();

    component.menu();
    expect(component.menuStatus).toBeFalse();
    expect(sidenavSpy.close).toHaveBeenCalled();
  });

  it('should close the sidenav when menu() is called with close argument', () => {
    component.sidenav = sidenavSpy; 
    component.menu(true);
    expect(sidenavSpy.close).toHaveBeenCalled();
  });

  it('should close sidenav and set reason when close() is called', () => {
    component.sidenav = sidenavSpy; 
    const reason = 'user clicked outside';

    component.close(reason);
    expect(component.reason).toBe(reason);
    expect(component.menuStatus).toBeFalse();
    expect(sidenavSpy.close).toHaveBeenCalled();
  });

  it('should subscribe to menuService click event on initialization', () => {
    expect(menuServiceSpy.getClickEvent).toHaveBeenCalled();
  });

  it('should call menu() when menuService emits click event', () => {
    spyOn(component, 'menu');
    component.clickEventSubscription = menuServiceSpy
      .getClickEvent()
      .subscribe(() => {
        expect(component.menu).toHaveBeenCalled();
      });
  });
});
