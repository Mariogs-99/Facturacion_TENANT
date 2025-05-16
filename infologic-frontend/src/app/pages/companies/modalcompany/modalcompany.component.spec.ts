import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalcompanyComponent } from './modalcompany.component';

describe('ModalcompanyComponent', () => {
  let component: ModalcompanyComponent;
  let fixture: ComponentFixture<ModalcompanyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModalcompanyComponent]
    });
    fixture = TestBed.createComponent(ModalcompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
