import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DataService } from 'src/app/core/services/data.service';

@Component({
  selector: 'app-module-header',
  templateUrl: './module-header.component.html',
  styleUrls: ['../../../../assets/styles/material-dashboard.css'],
  standalone: true,
  imports: [MatCardModule, MatIconModule, MatButtonModule],
})
export class ModuleHeaderComponent {
  @Input() title: string = 'sin nombre';
  @Input() subtitle: string = 'sin nombre';

  @Input() component: string = '';
  @Input() action: string = '';
  @Input() button: boolean = false;
  @Input() buttonLabel: string = 'sin nombre';
  @Input() buttonColor: string = 'primary';
  @Input() buttonAction: string = '';
  @Input() buttonIcon: any = null;

  //!Establecido como readonly
  constructor(private readonly _data: DataService) {}



  sendMessage() {
    this._data.sendMessage({
      from: 'ModuleHeaderComponent',
      component: this.component,
      action: this.action,
    });
  }
  
  cambiarValorButton(valor: boolean) {
    this.button = valor;
  }

}
