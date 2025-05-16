import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalbranchComponent } from './modalbranch.component';

describe('ModalbranchComponent', () => {
  let component: ModalbranchComponent;
  let fixture: ComponentFixture<ModalbranchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModalbranchComponent]
    });
    fixture = TestBed.createComponent(ModalbranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
