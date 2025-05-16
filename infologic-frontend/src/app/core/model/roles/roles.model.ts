import { Permisos } from '../permisos/permisos.model';

export interface Roles {
  idRole?: number;
  nameRole?: string;
  descriptionRole?: string;
  createdAt?: string;
  updatedAt?: string;
  deletedAt?: string;
  permissionIds?: number[];
  user?: any[];
  permission?: Permisos[];
}
