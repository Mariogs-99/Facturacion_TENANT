import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageMenuComponent } from './manage-menu.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import Swal from 'sweetalert2';
import { of, throwError } from 'rxjs';
import { MaterialModule } from 'src/app/material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('ManageMenuComponent', () => {
  let component: ManageMenuComponent;
  let fixture: ComponentFixture<ManageMenuComponent>;
  let apiService: jasmine.SpyObj<ApiService>;
  let dataService: jasmine.SpyObj<DataService>;

  const mockDialogRef = {
    close: jasmine.createSpy('close'),
  };

  const mockDialogData = {
    data: {
      menu: null,
      categories: [],
    },
  };

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['doRequest']);
    const dataServiceSpy = jasmine.createSpyObj('DataService', ['sendMessage']);

    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MaterialModule,
        HttpClientTestingModule,
        BrowserAnimationsModule,
      ],
      declarations: [ManageMenuComponent],
      providers: [
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: MAT_DIALOG_DATA, useValue: mockDialogData },
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: DataService, useValue: dataServiceSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ManageMenuComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    dataService = TestBed.inject(DataService) as jasmine.SpyObj<DataService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form correctly', () => {
    expect(component.menuForm).toBeDefined();
    expect(component.menuForm.controls['nameMenu'].valid).toBeFalse(); // Should be invalid initially
    expect(component.menuForm.controls['nameMenu'].validator).toBeDefined();
  });

  it('should send message with test method', () => {
    component.test();

    expect(dataService.sendMessage).toHaveBeenCalledWith({
      from: 'UpdateMenuComponent',
      component: 'UpdateMenuComponent',
      status: false,
    });
  });
});
