import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { LoadingService } from './core/services/loading.service';
import { BehaviorSubject } from 'rxjs';
import { RouterModule } from '@angular/router';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let loadingService: LoadingService;
  let loadingSubject: BehaviorSubject<boolean>;

  beforeEach(async () => {
    loadingSubject = new BehaviorSubject<boolean>(false);

    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [RouterModule],
      providers: [
        {
          provide: LoadingService,
          useValue: {
            loading$: loadingSubject.asObservable(),
            show: () => loadingSubject.next(true),
            hide: () => loadingSubject.next(false),
          },
        },
      ],
    }).compileComponents();

    loadingService = TestBed.inject(LoadingService);
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should have loading$ observable from LoadingService', () => {
    let isLoading: boolean | undefined;
    component.loading$.subscribe((value) => (isLoading = value));

    expect(isLoading).toBeFalse();

    loadingService.show();
    expect(isLoading).toBeTrue();
  });

  it('should call loading service in constructor', () => {
    expect(component.loadingService).toBeDefined();
  });
});
