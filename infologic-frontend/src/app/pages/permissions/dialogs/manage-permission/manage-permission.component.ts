import { Component, Inject } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Menu } from 'src/app/core/model/menu.model';
import { MenuItems } from 'src/app/core/model/menuItems.model';
import { Permisos } from 'src/app/core/model/permisos/permisos.model';
import { ApiService } from 'src/app/core/services/api.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { ManageMenuComponent } from 'src/app/pages/menu/views/manage-menu/manage-menu.component';

@Component({
  selector: 'app-manage-permission',
  templateUrl: './manage-permission.component.html',
})
export class ManagePermissionComponent {
  public permissionForm: FormGroup;
  public permission: Permisos;
  public menus: Menu[] = [];
  public menuItems: MenuItems[] = [];
  public updating: boolean = false;
  public menuItemsIds: number[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    public dialogRef: MatDialogRef<ManageMenuComponent>,
    private api: ApiService,
    private formBuilder: FormBuilder,
    private utils: UtilsService
  ) {
    this.permission = dialogData.data.permiso;
    this.permissionForm = this.formBuilder.group({
      idPermissions: new FormControl(
        '',
        this.updating ? [Validators.required] : null
      ),
      namePermissions: new FormControl('', [Validators.required]),
      descriptionPermissions: new FormControl('', [Validators.required]),
      menuId: new FormControl('', [Validators.required]),
      menuItemsIds: new FormControl([], [Validators.required]),
    });

    this.permission?.idPermissions != undefined
      ? this.setPermission(this.permission?.idPermissions)
      : this.setMenus();
  }

  setMenus() {
    this.api.doRequest('/menu/index', {}, 'get').then((resp: JsonResponse) => {
      if (resp.code != 200) {
        return this.utils.showSWAL(
          this.updating ? 'No se actualizo el menu' : 'No se creo el menu',
          '',
          'error'
        );
      }

      this.menus = resp.data;

      return this.setMenuItems(this.menus[0].idMenu);
    });
  }

  setPermission(idPermissions: number | undefined) {
    this.updating = true;

    this.api
      .doRequest('/permissions/' + idPermissions, null, 'get')
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'No encontro el menu seleccionado',
            '',
            'error'
          );
        }

        this.permission = resp.data.permiso;
        this.menus = resp.data.menu;

        this.setMenuItems(
          this.permission?.menuItems && this.permission?.menuItems.length > 0
            ? this.permission?.menuItems[0].idMenu
            : undefined
        );

        return this.permissionForm.patchValue({
          idPermissions: this.permission?.idPermissions,
          namePermissions: this.permission?.namePermissions,
          descriptionPermissions: this.permission?.descriptionPermissions,
          menuId: this.menus[0]?.idMenu,
          menuItemsIds:
            this.permission?.menuItems != undefined
              ? this.permission?.menuItems.map(
                  (menuItem) => menuItem?.idMenuItems
                )
              : null,
        });
      })
      .catch((error) => {});
  }

  update() {
    this.updating = this.permissionForm.controls['idPermissions'].value != '';
    this.api
      .doRequest(
        this.updating
          ? '/permissions/' +
              this.permissionForm.controls['idPermissions'].value
          : '/permissions',
        this.permissionForm.value,
        this.updating ? 'put' : 'post'
      )
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            this.updating
              ? 'No se actualizo el permiso'
              : 'No se creo el permiso',
            '',
            'error'
          );
        }

        return this.utils
          .showSWAL(
            this.updating
              ? 'Permiso actualizado correctamente'
              : 'Permiso creado correctamente',
            '',
            'success'
          )
          .then(() => {
            this.dialogRef.close({
              action: 'update',
              result: true,
            });
          });
      })
      .catch((error: any) => {});
  }

  setMenuItems(menuId: any) {
    if (menuId == undefined) {
      menuId = this.menus[0].idMenu;
    }

    this.menuItems = this.menus
      .filter((menu) => menu.menuItems)
      .flatMap((menu) => menu.menuItems || [])
      .filter((item) => item.idMenu == menuId);
  }

  public compareWith(object1: any, object2: any) {
    return object1 && object2 && object1 === object2;
  }
}
