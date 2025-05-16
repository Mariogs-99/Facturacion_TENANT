import { Component, Inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Category } from 'src/app/core/model/category.model';
import { JsonResponse } from 'src/app/core/model/JsonResponse.model';
import { Menu } from 'src/app/core/model/menu.model';
import { ApiService } from 'src/app/core/services/api.service';
import { DataService } from 'src/app/core/services/data.service';
import { UtilsService } from 'src/app/core/services/utils.service';

@Component({
  selector: 'app-manage-menu',
  templateUrl: './manage-menu.component.html',
})
export class ManageMenuComponent implements OnInit {
  public menuForm: FormGroup;
  public menu: Menu;
  public selectedCategory: any;

  public categories: Category[] = [];
  updating: boolean = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    public dialogRef: MatDialogRef<ManageMenuComponent>,
    private api: ApiService,
    private formBuilder: FormBuilder,
    private _data: DataService,
    private utils: UtilsService
  ) {
    this.menu = dialogData.data.menu;
    this.categories = dialogData.data.categories;

    this.menuForm = this.formBuilder.group({
      idMenu: new FormControl('', this.updating ? [Validators.required] : null),
      nameMenu: new FormControl('', [Validators.required]),
      descriptionMenu: new FormControl('', [Validators.required]),
      slugMenu: new FormControl('', [Validators.required]),
      idCategory: new FormControl('', [Validators.required]),
      createItemsMenu: new FormControl(false),
      apiName: new FormControl(''),
    });

    this.menuForm.controls['createItemsMenu'].valueChanges.subscribe(
      (value) => {
        if (value) {
          this.menuForm.controls['apiName'].setValidators([
            Validators.required,
          ]);
        } else {
          this.menuForm.controls['apiName'].setValidators(null);
        }
        this.menuForm.controls['apiName'].updateValueAndValidity();
      }
    );
  }
  ngOnInit(): void {
    if (this.menu?.idMenu != undefined) {
      this.setMenu(this.menu?.idMenu);
    }
  }

  setMenu(idMenu: number | undefined) {
    this.updating = true;
    this.api
      .doRequest('/menu/' + idMenu, null, 'get')
      .then((resp: JsonResponse) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            'No encontro el menu seleccionado',
            '',
            'Aceptar',
            'error'
          );
        }
        this.menu = resp.data.menu;

        this._data.sendMessage({
          from: 'UpdateMenuComponent',
          action: 'update',
          data: {
            menu: this.menu,
            pages: resp.data.pages,
          },
        });

        return this.menuForm.patchValue({
          idMenu: this.menu.idMenu,
          nameMenu: this.menu.nameMenu,
          descriptionMenu: this.menu.descriptionMenu,
          slugMenu: this.menu.slugMenu,
          idCategory: this.menu.idCategory,
        });
      })
      .catch((error) => {
        console.error('update menu', error);
      });
  }

  update() {
    this.updating = this.menuForm.controls['idMenu'].value != '';
    this.api
      .doRequest(
        this.updating
          ? '/menu/' + this.menuForm.controls['idMenu'].value
          : '/menu',
        this.menuForm.value,
        this.updating ? 'put' : 'post'
      )
      .then((resp: any) => {
        if (resp.code != 200) {
          return this.utils.showSWAL(
            this.updating ? 'No se actualizo el menu' : 'No se creo el menu',
            '',
            'Aceptar',
            'error'
          );
        }

        return this.utils
          .showSWAL(
            this.updating
              ? 'Menú actualizado correctamente'
              : 'Menú creado correctamente',
            '',
            'Aceptar',
            'success'
          )
          .then(() => {
            this.dialogRef.close({
              action: 'update',
              result: true,
            });
          });
      })
      .catch((error: any) => {
        console.error('error update', error);
      });
  }

  test() {
    this._data.sendMessage({
      from: 'UpdateMenuComponent',
      component: 'UpdateMenuComponent',
      status: false,
    });
  }

  public compareWith(object1: any, object2: any) {
    return object1 && object2 && object1 === object2;
  }
}
