import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuDialogComponent } from './menu-dialog.component';

describe('MenuDialogComponent', () => {
  let component: MenuDialogComponent;
  let fixture: ComponentFixture<MenuDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MenuDialogComponent]
    });
    fixture = TestBed.createComponent(MenuDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
