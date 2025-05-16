export class Item {
  idItem: number;
  tipoItem: number;
  nroDocumento: string;
  codigoProducto: string | null;
  clasificacion: string | null;
  codigoIngreso: string | null;
  editable: string;
  unidadMedida: number;
  nombre: string;
  descripcion: string;
  total: number;
  cantidad: number;
  precioUnitario: number;
  ivaItem: number;
  montoDescuento: number;
  ventaNosujeta: number;
  ventaExenta: number;
  ventaGravada: number;
  ventaNogravada: number;
  nombre2: string;

  constructor(data: any = {}) {
    this.idItem = this.assignValueItem(data.idItem, 0);
    this.tipoItem = this.assignValueItem(data.tipoItem, 0);
    this.nroDocumento = this.assignValueItem(data.nroDocumento, '');
    this.codigoProducto = this.assignValueItem(data.codigoProducto, null);
    this.clasificacion = this.assignValueItem(data.clasificacion, null);
    this.codigoIngreso = this.assignValueItem(data.codigoIngreso, null);
    this.editable = this.assignValueItem(data.editable, 'N');
    this.unidadMedida = this.assignValueItem(data.unidadMedida, 0);
    this.nombre = this.assignValueItem(data.nombre, '');
    this.descripcion = this.assignValueItem(data.descripcion, '');
    this.total = this.assignValueItem(data.total, 0);
    this.cantidad = this.assignValueItem(data.cantidad, 0);
    this.precioUnitario = this.assignValueItem(data.precioUnitario, 0);
    this.ivaItem = this.assignValueItem(data.ivaItem, 0);
    this.montoDescuento = this.assignValueItem(data.montoDescuento, 0);
    this.ventaNosujeta = this.assignValueItem(data.ventaNosujeta, 0);
    this.ventaExenta = this.assignValueItem(data.ventaExenta, 0);
    this.ventaGravada = this.assignValueItem(data.ventaGravada, 0);
    this.ventaNogravada = this.assignValueItem(data.ventaNogravada, 0);
    this.nombre2 = this.assignValueItem(data.nombre2, '');
  }
  assignValueItem(value: any, defaultValue: any) {
    return value !== undefined ? value : defaultValue;
  }
}

export class CuerpoDto {
  monto: number;
  ivaRetenido: number;
  fecha: string;
  fechaEmision: string;
  nombre: string;
  descripcion: string;
  items: Item[];
  tipoDte: string;
  codigoGeneracion: string;
  serieDocumento: string;
  nroPreimpreso: number;
  tipoGeneracion: string;
  idDocumentoAsociado: number;
  nroDocumentoHijo: string;
  nroDocumento: string;
  idTransaccionHija: number;
  renta1: boolean = false;
  renta13: boolean = false;

  constructor(data: any = {}) {
    this.monto = this.assignValue(data.monto, 0);
    this.ivaRetenido = this.assignValue(data.ivaRetenido, 0);
    this.fecha = this.assignValue(data.fecha, '');
    this.fechaEmision = this.assignValue(data.fechaEmision, '');
    this.nombre = this.assignValue(data.nombre, '');
    this.descripcion = this.assignValue(data.descripcion, '');
    this.items = this.assignValue(data.items, []).map(
      (item: any) => new Item(item)
    );
    this.tipoDte = this.assignValue(data.tipoDte, '');
    this.codigoGeneracion = this.assignValue(data.codigoGeneracion, '');
    this.serieDocumento = this.assignValue(data.serieDocumento, '');
    this.nroPreimpreso = this.assignValue(data.nroPreimpreso, 0);
    this.tipoGeneracion = this.assignValue(data.tipoGeneracion, '');
    this.idDocumentoAsociado = this.assignValue(data.idDocumentoAsociado, 0);
    this.nroDocumentoHijo = this.assignValue(data.nroDocumentoHijo, '');
    this.nroDocumento = this.assignValue(data.nroDocumento, '');
    this.idTransaccionHija = this.assignValue(data.idTransaccionHija, 0);
    this.renta1 = this.assignValue(data.nroDocumento, false);
    this.renta13 = this.assignValue(data.renta13, false);
  }

  assignValue(value: any, defaultValue: any) {
    return value !== undefined ? value : defaultValue;
  }
}
