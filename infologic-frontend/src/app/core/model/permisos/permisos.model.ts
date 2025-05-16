import { MenuItems } from '../menuItems.model';

export interface Permisos {
  idPermissions?: number;
  namePermissions?: string;
  descriptionPermissions?: string;
  createdAt?: any;
  updatedAt?: any;
  menuItems?: MenuItems[] | null;
  menuItemsIds?: number[]
}
