import { Component } from '@angular/core';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { MenuService } from 'src/app/core/services/menu-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ResetPasswordComponent } from 'src/app/pages/autentificacion/reset-password/reset-password.component';
import { ManageUserComponent } from 'src/app/pages/user/dialogs/manage-user/manage-user.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  public username: string = '';
  public menu: boolean = false;

  constructor(
    //! Marcados como readonly
    private readonly _menuService: MenuService,
    public auth: AuthService,
    private readonly util: UtilsService,
    private readonly api: ApiService
  ) {
    this.userName();
  }

  public home() {
    this.util.goToPage('dashboard', false);
  }

  public toggle() {
    this._menuService.sendClickEvent();
  }

  public userName() {
    this.username =
      this.auth.userName != ''
        ? this.auth.userName
        : this.auth.getAuthInfo('username');
  }

  public changePass() {
    this.util.openModal(
      {
        title: 'Actualizacion de contraseña',
        botonAceptar: 'Aceptar',
        componentToLoad: ResetPasswordComponent,
        callerComponent: this,
        data: null,
        footer: false,
      },
      '40%',
      '50%',
      '100%',
      '100%'
    );
  }

  public updateProfile() {
    this.util.openModal({
      title: 'Actualización de Usuario',
      botonAceptar: 'Actualizar',
      componentToLoad: ManageUserComponent,
      callerComponent: this,
      footer: false,
      data: {
        user: {
          idUser: 0,
          usuario: this.username,
        },
        requerido: this.auth.getRequiredInvoice(),
        origin: 'login',
      },
    });
  }

  logout() {
    this.api.logout().then((resp) => {
      this.auth.LogOut();
    });
  }
}
