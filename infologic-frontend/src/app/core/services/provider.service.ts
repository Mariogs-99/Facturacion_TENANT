import { EventEmitter, Injectable } from '@angular/core';
import { SharedDataService } from './shared-data-service.service';

@Injectable({
  providedIn: 'root',
})
export class ProviderService {
  public institucionValue = 0;
  public uhriValue = 0;
  year!: number;
  month!: number;
  selectedIntitucionValueChange = new EventEmitter<number>();

  constructor(public sharedDataService: SharedDataService) {
    this.institucionValue = this.getInstitucion();
    this.uhriValue = this.getUhri();
  }

  updateSelectedInstitucion(value: number, godMode?: boolean) {
    if (godMode) {
      this.institucionValue = value;
      this.selectedIntitucionValueChange.emit(value);
      this.sharedDataService.setVariable('INST-id', JSON.stringify(value));
      this.uhriValue = 0;
      this.sharedDataService.setVariable('UHRI-id', null);
    } else {
      this.institucionValue = value;
      this.selectedIntitucionValueChange.emit(value);
      this.sharedDataService.setVariable('INST-id', JSON.stringify(value));
      this.sharedDataService.setVariable('UHRI-id', null);
    }
  }

  updateSelectedUhri(value: number, godMode: boolean) {
    if (godMode) {
      this.uhriValue = value;
      this.sharedDataService.setVariable('UHRI-id', value);
    } else {
      this.sharedDataService.setVariable('UHRI-id', value);
      this.uhriValue = value;
    }
  }

  getInstitucion(): number {
    let institucion_id = this.sharedDataService.getVariableValue('INST-id');
    if (institucion_id) {
      return parseInt(institucion_id);
    }
    return 0;
  }

  getUhri() {
    let urhi_id = this.sharedDataService.getVariableValue('UHRI-id');
    if (urhi_id) {
      return parseInt(urhi_id);
    }
    return 0;
  }

  updateYear(value: number) {
    this.year = value;
  }

  updateMonth(value: number) {
    this.month = value;
  }
  getYear() {
    return this.year;
  }
  getMonth() {
    return this.month;
  }
}
