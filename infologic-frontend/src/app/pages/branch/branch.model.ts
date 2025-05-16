import { Rango } from '../../core/model/range.model';

export class BranchModel {
  nombre?: string = '';
  direccion?: string = '';
  misional?: string = '';
  nombreMunicipioBranch?: string = '';
  telefono?: string = '';
  email?: string = '';
  idDeptoBranch?: number = 0;
  idMuniBranch?: number = 0;
  idCompany?: number = 0;
  idSucursal?: number = 0;
  nombreCompany?: string = '';
  config_pdf?: any;
  config?: any;
  codigoSucursal?: any;
  rango?: Rango;
  idTipoEstablecimiento?: number = 0;
}

export class Branch {
  nombre?: string;
  direccion?: string;
  nombreMunicipioBranch?: string;
  telefono?: string;
  email?: string;
  misional?: string;
  idDeptoBranch?: number;
  idMuniBranch?: number;
  idCompany?: number;
  idSucursal?: number;
  nombreCompany?: string;
  config_pdf?: any;
  config?: any;
  codigoSucursal?: any;
  rango?: Rango;
  idTipoEstablecimiento?: number;
}
