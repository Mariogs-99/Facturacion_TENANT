import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageProductoComponent } from './manage-producto.component';

describe('ManageProductoComponent', () => {
  let component: ManageProductoComponent;
  let fixture: ComponentFixture<ManageProductoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageProductoComponent]
    });
    fixture = TestBed.createComponent(ManageProductoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
