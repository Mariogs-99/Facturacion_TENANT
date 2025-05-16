import { Component } from '@angular/core';
import { LoadingService } from './core/services/loading.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['../assets/styles/material-dashboard.css', './app.component.css'],
})
export class AppComponent {
  loading$ = this.loadingService.loading$;

  constructor(public loadingService: LoadingService) {}
}
