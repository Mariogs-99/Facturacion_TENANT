import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  private dataStore: Record<string, BehaviorSubject<any>> = {};

  setVariable(key: string, value: any): void {
    if (!this.dataStore[key]) {
      this.dataStore[key] = new BehaviorSubject<any>(value);
    } else {
      this.dataStore[key].next(value);
    }
  }

  getVariableValue(key: string): any {
    if (!this.dataStore[key]) {
      this.dataStore[key] = new BehaviorSubject<any>(null);
    }
    return this.dataStore[key].getValue();
  }

  getVariable(key: string): Observable<any> {
    if (!this.dataStore[key]) {
      this.dataStore[key] = new BehaviorSubject<any>(null);
    }
    return this.dataStore[key].asObservable();
  }

  resetVariable(key: string): void {
    if (this.dataStore[key]) {
      this.dataStore[key].next(null);
    }
  }
}