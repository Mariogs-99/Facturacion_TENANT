import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Subscription } from 'rxjs';
import { MenuService } from '../core/services/menu-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './pages.component.html',
  styleUrls: [
    './pages.component.css',
    '../../assets/styles/material-dashboard.css',
  ],
})
export class PagesComponent implements OnInit {
  showFiller = false;
  @ViewChild('sidenav') sidenav!: MatSidenav;
  public menuStatus: boolean;
  clickEventSubscription!: Subscription;
  reason: string = '';

  constructor(public _menuService: MenuService) {
    this.menuStatus = false;

    this.clickEventSubscription = this._menuService
      .getClickEvent()
      .subscribe((t) => {
        this.menu();
      });
  }

  ngOnInit(): void {
    this.reason = '';
  }

  menu(close?: boolean) {
    if (close) {
      this.sidenav.close();
    } else {
      this.menuStatus = !this.menuStatus;
      this.menuStatus ? this.sidenav.open() : this.sidenav.close();
    }
  }

  close(reason: string) {
    this.reason = reason;

    this.menuStatus = false;
    this.sidenav.close();
  }
}
