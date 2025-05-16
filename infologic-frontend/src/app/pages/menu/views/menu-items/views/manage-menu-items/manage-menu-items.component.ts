import { Component, OnDestroy } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { MenuItems } from 'src/app/core/model/menuItems.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-manage-menu-items',
  templateUrl: './manage-menu-items.component.html',
})
export class ManageMenuItemsComponent implements OnDestroy {
  private subscribe!: Subscription;
  public menuItemsForms: FormGroup;
  public update: boolean = false;
  public menuItemId!: MenuItems;
  public menuId!: number | null;
  public httpMethods: string[] = ['GET', 'POST', 'PUT', 'DELETE'];

  constructor(
    private _data: DataService,
    private api: ApiService,
    private formBuilder: FormBuilder,
    private utils: UtilsService,
    public dialogRef: MatDialogRef<ManageMenuItemsComponent>
  ) {
    this.subscribe = this._data.getData().subscribe((resp: any) => {
      if (resp.from == 'UpdateMenuComponent') {
        this.menuId = resp.data.menu.idMenu;
      }

      if (resp.from == 'MenuItemsComponent') {
        if (resp.action == 'update') {
          this.menuId = resp.data.idMenu;
          this.update = true;
          this.setMenuItem(resp.data.idMenuItems);
        }

        if (resp.from == 'MenuItemsComponent' && resp.action == 'open') {
          this.update = false;
          this.menuItemsForms.reset();
          this.menuItemsForms.markAsUntouched();
        }
      }
    });

    this.menuItemsForms = this.formBuilder.group({
      idMenuItems: new FormControl(
        '',
        this.update ? [Validators.required] : null
      ),
      idMenu: new FormControl(
        this.menuId,
        this.update ? [Validators.required] : null
      ),
      nameMenuItems: new FormControl('', [Validators.required]),
      uriMenuItems: new FormControl('', [Validators.required]),
      descriptionMenuItems: new FormControl('', [Validators.required]),
      type: new FormControl('', [Validators.required]),
      imageMenuItems: new FormControl(''),
    });
  }
  ngOnDestroy(): void {
    this.subscribe.unsubscribe();
    this.menuItemsForms.reset();
    this.menuItemsForms.markAsUntouched();
  }

  setMenuItem(idMenuItems: number | undefined) {
    this.api
      .doRequest('/menu-items/' + idMenuItems, null, 'get')
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'No encontro el menu seleccionado',
            '',
            'Aceptar',
            'error'
          );
        }

        return this.menuItemsForms.patchValue({
          idMenuItems: resp.data.idMenuItems,
          idMenu: this.menuId,
          nameMenuItems: resp.data.nameMenuItems,
          uriMenuItems: resp.data.uriMenuItems,
          descriptionMenuItems: resp.data.descriptionMenuItems,
          type: resp.data.type,
          imageMenuItems: resp.data.imageMenuItems,
        });
      });
  }

  upsert() {
    if (!this.update) {
      this.menuItemsForms.patchValue({
        idMenu: this.menuId,
      });
    }

    this.api
      .doRequest(
        this.update
          ? '/menu-items/' + this.menuItemsForms.controls['idMenuItems'].value
          : '/menu-items',
        this.menuItemsForms.value,
        this.update ? 'put' : 'post'
      )
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            this.update
              ? 'No se actualizo el item de menu'
              : 'No se creo el item de menu ',
            '',
            'error'
          );
        }

        return this.utils
          .showSWAL(
            this.update
              ? 'Se actualizo el item de menu'
              : 'Se creo el item de menu',
            '',
            'Aceptar',
            'success'
          )
          .then(() => {
            this.menuItemsForms.reset();
            this.menuItemsForms.markAsUntouched();
            this._data.sendMessage({
              from: 'ManageMenuItemsComponent',
              action: this.update ? 'update' : 'create',
              data: this.menuId,
            });
          });
      });
  }

  cancel() {
    this.menuItemsForms.reset();
    this.menuItemsForms.markAsUntouched();
    this._data.sendMessage({
      from: 'ManageMenuItemsComponent',
      action: 'cancel',
    });
  }

  compareStrings(s1: string, s2: string) {
    return s1 === s2;
  }
}
