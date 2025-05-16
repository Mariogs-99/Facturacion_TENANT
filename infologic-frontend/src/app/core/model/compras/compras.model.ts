export interface Compras {
  idCompra?: number;
  codigoGeneracion?: string;
  numeroControl?: string;
  fechaEmision?: string;
  tipoOperacion?: string;
  tipoDocumento?: string;
  selloRecepcion?: string;
  idProveedor?: number;
  totalGravado?: string;
  totalExento?: string;
  totalOperacion?: string | number;
}
