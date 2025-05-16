import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TableHeaderComponent } from './table-header.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatGridListModule } from '@angular/material/grid-list';
import { DataService } from '../../../core/services/data.service';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('TableHeaderComponent', () => {
  let component: TableHeaderComponent;
  let fixture: ComponentFixture<TableHeaderComponent>;
  let dataService: jasmine.SpyObj<DataService>;

  beforeEach(async () => {
    const dataServiceSpy = jasmine.createSpyObj('DataService', ['getData', 'sendMessage']);

    await TestBed.configureTestingModule({
      imports: [
        TableHeaderComponent, // Importamos el componente standalone
        ReactiveFormsModule,
        FormsModule,
        MatIconModule,
        MatFormFieldModule,
        MatSelectModule,
        MatDatepickerModule,
        MatInputModule,
        MatButtonModule,
        MatDialogModule,
        MatPaginatorModule,
        MatTableModule,
        MatGridListModule,
      ],
      providers: [
        { provide: DataService, useValue: dataServiceSpy },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(TableHeaderComponent);
    component = fixture.componentInstance;
    dataService = TestBed.inject(DataService) as jasmine.SpyObj<DataService>;

    // Configuramos el método getData para devolver un observable
    dataService.getData.and.returnValue(of({ from: component.tableName, events: 'sort', input: 'testColumn' }));

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.title).toBe('sin nombre');
    expect(component.column).toBe('sin nombre');
    expect(component.tableName).toBe('');
    expect(component.rowHeight).toBe('7:1');
    expect(component.sort).toBe(false);
    expect(component.search).toBe(false);
    expect(component.alignment).toBe('text-center');
    expect(component.rows).toBe(1);
    expect(component.type).toBe('text');
    expect(component.data).toEqual([]);
  });

  it('should call getData on ngOnInit', () => {
    component.ngOnInit();

    expect(dataService.getData).toHaveBeenCalled();
  });

  it('should update sortDirection in response to sort event', () => {
    component.ngOnInit();

    expect(component.sortDirection).toBe('none');

    // Simulamos un cambio en el input
    component.column = 'testColumn';
    dataService.getData.and.returnValue(of({ from: component.tableName, events: 'sort', input: 'anotherColumn' }));
    component.ngOnInit();

    expect(component.sortDirection).toBe('none');
  });

  it('should sort data correctly', () => {
    component.sortData('testColumn');
    expect(component.sortDirection).toBe('asc');

    component.sortData('testColumn');
    expect(component.sortDirection).toBe('desc');

    component.sortData('testColumn');
    expect(component.sortDirection).toBe('none');
  });

  it('should apply filter correctly', () => {
    component.inputFilter = 'testFilter';
    component.applyFilterByColumn('testFilter');

    expect(dataService.sendMessage).toHaveBeenCalledWith({
      from: component.tableName,
      value: '',
      input: 'testFilter',
      events: 'filter',
    });
  });

  it('should clear filter correctly', () => {
    const event = new Event('clear');

    component.clearFilter('testFilter', event);

    expect(component.inputFilter).toBe('');
    expect(dataService.sendMessage).toHaveBeenCalledWith({
      from: component.tableName,
      value: '',
      input: 'testFilter',
      events: 'filter',
    });
  });
});
