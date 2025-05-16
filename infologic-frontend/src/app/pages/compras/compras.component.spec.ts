import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ComprasComponent } from './compras.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { DataService } from 'src/app/core/services/data.service';
import { of } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { ModuleHeaderComponent } from 'src/app/components/base/module-header/module-header.component';
import { MatDialogRef } from '@angular/material/dialog';
import { Compras } from 'src/app/core/model/compras/compras.model';

describe('ComprasComponent', () => {
  let component: ComprasComponent;
  let fixture: ComponentFixture<ComprasComponent>;
  let apiService: jasmine.SpyObj<ApiService>;
  let utilsService: jasmine.SpyObj<UtilsService>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<any>>;

  beforeEach(async () => {
    const apiSpy = jasmine.createSpyObj('ApiService', ['getPage', 'doRequest']);
    const utilsSpy = jasmine.createSpyObj('UtilsService', [
      'openModal',
      'SWALYESNO',
    ]);

    // Simula que getPage retorna una promesa resuelta
    apiSpy.getPage.and.returnValue(
      Promise.resolve({
        code: 200,
        data: {
          pages: {
            content: [], // Ajusta el contenido según lo que se espera en la prueba real
            totalElements: 10, // Ajusta el número de elementos según la situación
          },
          operacion_tipo: [],
          documento_tipo: [],
        },
      } as JsonResponse)
    );

    dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['afterClosed']);

    // Simula que doRequest retorna una promesa resuelta
    apiSpy.doRequest.and.returnValue(
      Promise.resolve({
        code: 200,
        data: {},
        message: '',
      } as JsonResponse)
    );

    await TestBed.configureTestingModule({
      declarations: [ComprasComponent],
      imports: [
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        BrowserAnimationsModule,
        ModuleHeaderComponent,
      ],
      providers: [
        { provide: ApiService, useValue: apiSpy },
        { provide: UtilsService, useValue: utilsSpy },
        { provide: DataService, useValue: { getData: () => of({}) } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ComprasComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    utilsService = TestBed.inject(UtilsService) as jasmine.SpyObj<UtilsService>;

    fixture.detectChanges();
  });

  it('should create the component', async () => {
    expect(component).toBeTruthy();
  });

  // it('should fetch data in indexCompras', async () => {
  //   component.indexCompras();

  //   // Esperar a que la promesa se resuelva
  //   await fixture.whenStable();

  //   expect(apiService.getPage).toHaveBeenCalledWith(
  //     '/compras/page',
  //     undefined,
  //     undefined,
  //     0,
  //     10
  //   );
  //   expect(component.dataSource).toBeTruthy();
  //   expect(component.totalRecords).toBe(10); // Ajusta según la simulación
  // });


});
