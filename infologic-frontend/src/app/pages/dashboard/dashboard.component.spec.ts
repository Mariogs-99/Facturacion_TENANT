import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { ApiService } from 'src/app/core/services/api.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const apiSpy = jasmine.createSpyObj('ApiService', ['doRequest']);
    const routerSpyMock = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [DashboardComponent],
      imports: [MatCardModule, MatTabsModule],
      providers: [
        { provide: ApiService, useValue: apiSpy },
        { provide: Router, useValue: routerSpyMock }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] // Agregar esto
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    apiServiceSpy = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to specific page', () => {
    component.redirigirAPaginaEspecifica();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/transacciones/ventas']);
  });

  it('should redirect to reports page', () => {
    component.redirigirAPaginaReportes();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/transacciones/reportes']);
  });

  it('should initialize KPI data', async () => {
    const mockKPIResponse: any = [ // Cambia aquí para usar any
      { id: 'kpi1', name: 'KPI 1', value: 10, target: 20 },
      { id: 'kpi2', name: 'KPI 2', value: 5, target: 15 },
      { id: 'kpi3', name: 'KPI 3', value: 2, target: 10 },
      { id: 'kpi4', name: 'KPI 4', value: 1, target: 5 },
      { id: 'kpi5', name: 'KPI 5', value: 0, target: 2 },
    ];

    apiServiceSpy.doRequest.and.returnValue(Promise.resolve(mockKPIResponse));
    
    await component.ngOnInit();

    expect(component.kpiData.length).toBe(5);
    // Asegúrate de que estos valores sean calculados correctamente en el componente
    expect(component.totalEmitidos).toBe(20);
    expect(component.totalInvalidados).toBe(10);
    expect(component.totalContingencia).toBe(5);
    expect(component.totalRechazados).toBe(2);
    expect(component.totalFECanceladas).toBe(1);
    expect(component.totalCCFECancelados).toBe(0);
  });

  it('should create the chart for KPIs', async () => {
    const mockKPIResponse: any = [ // Cambia aquí para usar any
      { id: 'kpi1', name: 'KPI 1', value: 10, target: 20 },
      { id: 'kpi2', name: 'KPI 2', value: 5, target: 15 },
    ];

    apiServiceSpy.doRequest.and.returnValue(Promise.resolve(mockKPIResponse));
    
    await component.ngOnInit();
    component.chartTest();

    expect(component.chart).toBeDefined();
    expect(component.chart.data.labels!.length).toBe(3); // 2 KPIs + "DTE Emitidos"
  });

  it('should load tiles with counter data', async () => {
    const mockKPIResponse: any = [ // Cambia aquí para usar any
      { id: 'kpi1', name: 'KPI 1', value: 10, target: 20 },
      { id: 'kpi2', name: 'KPI 2', value: 5, target: 15 },
      { id: 'kpi3', name: 'KPI 3', value: 2, target: 10 },
      { id: 'kpi4', name: 'KPI 4', value: 1, target: 5 },
      { id: 'kpi5', name: 'KPI 5', value: 0, target: 2 },
    ];

    apiServiceSpy.doRequest.and.returnValue(Promise.resolve(mockKPIResponse));
    
    await component.ngOnInit();
    component.loadTiles();

    expect(component.dashtotalEmitidos.value).toBe(10);
    expect(component.dashtotalInvalidados.value).toBe(1); // 2 / 10
    expect(component.dashtotalRechazados.value).toBe(0.2); // 2 / 10
  });

  it('should call the API for sucursales on init', async () => {
    const mockSucursalResponse: any = [{ id: 1, name: 'Sucursal 1' }];
    
    apiServiceSpy.doRequest.and.returnValue(Promise.resolve(mockSucursalResponse));
    
    await component.ngOnInit();

    expect(component.sucursales.length).toBe(1);
    expect(component.sucursales[0].name).toBe('Sucursal 1');
  });

  // it('should update the chart for sucursal', async () => {
  //   const mockKPIResponse: any = [ // Cambia aquí para usar any
  //     { id: 'kpi1', name: 'KPI 1', value: 10 },
  //     { id: 'kpi2', name: 'KPI 2', value: 5 },
  //   ];

  //   apiServiceSpy.doRequest.and.returnValue(Promise.resolve(mockKPIResponse));
  //   component.chartSucursal(1);
    
  //   expect(component.kpiDataSucursal.length).toBe(2);
  //   expect(component.kpiDataSucursal[0].name).toBe('KPI 1');
  // });
});

