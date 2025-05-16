import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  public menu = new Subject<any>();
  public menus: boolean = false;

  constructor() {}

  public sendClickEvent(): void {
    this.menu.next(false);
  }

  public closeMenu(): void {
    this.menu.next(true);
  }

  getClickEvent(): Observable<any> {
    return this.menu.asObservable();
  }
}
