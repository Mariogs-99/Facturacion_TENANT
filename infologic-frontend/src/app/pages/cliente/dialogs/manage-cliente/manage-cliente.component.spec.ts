import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageClienteComponent } from './manage-cliente.component';

describe('ManageClienteComponent', () => {
  let component: ManageClienteComponent;
  let fixture: ComponentFixture<ManageClienteComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageClienteComponent]
    });
    fixture = TestBed.createComponent(ManageClienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
