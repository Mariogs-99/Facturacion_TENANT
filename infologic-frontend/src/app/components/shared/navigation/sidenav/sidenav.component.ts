import { Component, OnDestroy, ViewChild } from '@angular/core';
import { FlatTreeControl } from '@angular/cdk/tree';
import {
  MatTreeFlatDataSource,
  MatTreeFlattener,
} from '@angular/material/tree';
import { MenuService } from 'src/app/core/services/menu-service.service';
import { MatSidenav } from '@angular/material/sidenav';
import { Subscription } from 'rxjs';
import { UtilsService } from '../../../../core/services/utils.service';
import { LoadingService } from '../../../../../../src/app/core/services/loading.service';
import { AuthService } from 'src/app/core/services/auth.service';

interface CategoryNode {
  category: string;
  menu?: MenuNode[];
}

interface MenuNode extends CategoryNode {
  nameMenu?: string;
  slugMenu?: string;
}

interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  url?: string;
  level: number;
  nameMenu?: string;
  slugMenu?: string;
}

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css'],
})
export class SidenavComponent implements OnDestroy {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  selectedOption: HTMLElement | null = null;

  public menuStatus: boolean;
  clickEventSubscription!: Subscription;
  public treeData: CategoryNode[] = [];

  //! Marcado como readonly
  private readonly _transformer = (node: CategoryNode | MenuNode, level: number) => {
    return {
      expandable: !!node.menu && node.menu.length > 0,
      name: node.category,
      nameMenu: 'nameMenu' in node ? node.nameMenu : undefined,
      slugMenu: 'slugMenu' in node ? node.slugMenu : undefined,
      level: level,
    };
  };

  treeControl = new FlatTreeControl<ExampleFlatNode>(
    (node) => node.level,
    (node) => node.expandable
  );

  treeFlattener = new MatTreeFlattener<CategoryNode, ExampleFlatNode>(
    this._transformer,
    (node) => node.level,
    (node) => node.expandable,
    (node) => node.menu
  );

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  constructor(
    //!Establecidos como readonly
    public _menuService: MenuService,
    private readonly util: UtilsService,
    private readonly loadingService: LoadingService,
    private readonly auth: AuthService
  ) {
    this.menuStatus = false;
    this.clickEventSubscription = this._menuService
      .getClickEvent()
      .subscribe((t) => {
        this.menu();
      });

    this.dataSource.data = this.auth.getMatMenu();
  }

  hasChild = (_: number, node: ExampleFlatNode) => node.expandable;

  ngOnDestroy(): void {
    this.clickEventSubscription.unsubscribe();
  }

  menu() {
    this.menuStatus = !this.menuStatus;
    this.menuStatus ? this.sidenav.open() : this.sidenav.close();
  }

  goto(node: any) {
    this.loadingService.show();

    this.util.goToPage(node);
    this.loadingService.hide();
    this._menuService.closeMenu();
  }

  setSelectedClass(element: any) {
    if (this.selectedOption) {
      this.selectedOption.classList.remove('selected-option');
    }

    this.selectedOption = element.target as HTMLElement;
    this.selectedOption.classList.add('selected-option');
  }
}
