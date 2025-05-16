import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAnulacionComponent } from './modal-anulacion.component';

describe('ModalAnulacionComponent', () => {
  let component: ModalAnulacionComponent;
  let fixture: ComponentFixture<ModalAnulacionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModalAnulacionComponent]
    });
    fixture = TestBed.createComponent(ModalAnulacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
