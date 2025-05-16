import { MenuItems } from './menuItems.model';

export interface Menu {
  idMenu?: number;
  idCategory?: number;
  nameMenu?: string;
  descriptionMenu?: string;
  slugMenu?: string;
  imageMenu?: string;
  menuItems: MenuItems | undefined;
  createdAt?: any;
  updatedAt?: any;
  deletedAt?: any;
}
