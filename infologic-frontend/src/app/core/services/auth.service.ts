import { Injectable } from '@angular/core';
import { UtilsService } from './utils.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public token: any;
  public localData: any;
  public userName: string = '';
  public isLoggedIn = false;
  public menu: any;
  public permisos: string[] = [];
  public departamento: string = '';
  public nombre: string = '';
  public dui: string = '';

  constructor(
    private util: UtilsService,
    public dialog: MatDialog,
    private router: Router
  ) {
    this.getToken();
  }

  public getAuthInfo(index: string): string {
    return this.localData[index];
  }

  public getLocalData(origen: string, item: string) {
    const localData = JSON.parse(localStorage.getItem(origen) as string);
    return localData[item];
  }

  public setLocalData(origen: string, item: string, value: any) {
    const localData = JSON.parse(localStorage.getItem(origen) as string);
    localData[item] = value;
    localStorage.setItem(origen, JSON.stringify(localData));
  }

  public getMatMenu() {
    this.menu = JSON.parse(localStorage.getItem('menu') ?? '{}');

    // Verificar si la categoría "Gestión" existe en el menú
    if (!this.menu["Gestión"]) {
        this.menu["Gestión"] = [];
    }

    // Verificar si "Productos" ya está en la categoría "Gestión"
    const productosExiste = this.menu["Gestión"].some((item: any) => item.nameMenu === "Productos");

    if (!productosExiste) {
        this.menu["Gestión"].push({ nameMenu: "Productos", slugMenu: "config/productos" });
    }

    // 🔹 Agregar Clientes a la categoría "Gestión"
    const clientesExiste = this.menu["Gestión"].some((item: any) => item.nameMenu === "Clientes");
    if (!clientesExiste) {
        this.menu["Gestión"].push({ nameMenu: "Clientes", slugMenu: "config/clientes" });
    }

    // Guardar en localStorage para que persista
    localStorage.setItem('menu', JSON.stringify(this.menu));

    let arrtest: any = [];
    Object.entries(this.menu).forEach((key: any) => {
        let test = { category: key[0], menu: [...key[1]] };
        arrtest.push(test);
    });

    return [...arrtest];
  }

  public getToken() {
    this.localData = JSON.parse(localStorage.getItem('cnr-info') as string);

    if (this.localData != null) {
      this.token = this.getAuthInfo('token');
      this.departamento = this.getAuthInfo('departamento');
      this.nombre = this.getAuthInfo('nombre');
      return this.token;
    }

    return this.LogOut();
  }

  public LogOut() {
    this.dialog.closeAll();
    this.token = null;
    localStorage.clear();
    return this.util.goToPage('/login', false);
  }

  public isLoggedInFn() {
    return this.token != undefined;
  }

  public getPermissions(): string[] {
    this.permisos = [];

    try {
      const menu = this.getMatMenu();
      const currentUrl = this.router.url;

      for (const menuGroup of menu) {
        for (const menuItem of menuGroup.menu) {
          if (currentUrl === '/' + menuItem.slugMenu) {
            for (const item of menuItem.menuItems) {
              this.permisos.push(item.nameMenuItems);
            }
          }
        }
      }

      return this.permisos;
    } catch (error) {
      console.error('getting permissions... ERROR', error);
    }

    return ['error'];
  }

  getRequiredInvoice() {
    return this.getLocalData('cnr-info', 'requerido_facturacion');
  }

  public getDepartamento() {
    return this.departamento;
  }

  public getNombre() {
    return this.nombre;
  }

  public getDui() {
    this.dui = this.getAuthInfo('dui');
    return this.dui;
  }
}
