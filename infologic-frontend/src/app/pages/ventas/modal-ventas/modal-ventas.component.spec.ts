import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalVentasComponent } from './modal-ventas.component';

describe('ModalVentasComponent', () => {
  let component: ModalVentasComponent;
  let fixture: ComponentFixture<ModalVentasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModalVentasComponent]
    });
    fixture = TestBed.createComponent(ModalVentasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
