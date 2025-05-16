export interface Reporte {
  reporteName: string;
  descripcion: string;
  status: string;
  csv: string;
  esParaTodos: string;
  deptoReporte: string;
  esPorSucursal: string;
}


export interface Sucursal {
  id: string;
  descripcion: string;
  nombreSucursal: string;
  numeroSucursales: number;

}

export interface PuntoDeVenta {
  codigoSucursal: string;
  nombre: string;
  misional: string;

}

