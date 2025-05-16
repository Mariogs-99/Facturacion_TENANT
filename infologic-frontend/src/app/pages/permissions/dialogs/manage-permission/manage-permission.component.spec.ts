import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagePermissionComponent } from './manage-permission.component';

describe('ManagePermissionComponent', () => {
  let component: ManagePermissionComponent;
  let fixture: ComponentFixture<ManagePermissionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManagePermissionComponent]
    });
    fixture = TestBed.createComponent(ManagePermissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
