import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContenidoModalComponent } from './contenido-modal.component';

describe('ContenidoModalComponent', () => {
  let component: ContenidoModalComponent;
  let fixture: ComponentFixture<ContenidoModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ContenidoModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContenidoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
