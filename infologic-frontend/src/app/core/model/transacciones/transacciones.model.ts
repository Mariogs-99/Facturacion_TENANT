export class Cliente {
  id: string = '';
  id_branch: string = '';
  name: string = '';
  comercial_name: string = '';
  doc_num_id: string = '';
  doc_num: string = '';
  phone: string = '';
  others: string = '';
  giro: string = '';
  email: string = '';
  fecha: string = '';
  NIT: string = '';
  NRC: string = '';
  monto_maximo_credit: number = 0;
  customer_state: number = 1;
  is_final_consumer: number = 0;
  is_natural_person: number = 0;
  is_juridico: number = 0;
  is_extranjero: number = 0;
  is_gran_contribuyente: number = 0;
  lead_customer: number = 0;
  editable!: boolean;
}

export class Transaction {
  id: string = '';
  id_branch: string = '';
  id_user: string = '';
  id_user_name: string = '';
  id_customer: string = '';
  num_transaction: string = '';
  id_payment_type: string = '';
  payment_type: string = '';
  id_doc_transaction_type: string = '35';
  id_transaction_type: string = '1676';
  items: string = '';
  customers: string = '';
  id_payment_cond: string = '457';
  id_credit_time: string = '474';
  credit_time: number = 0;
  cliente: string = '';
  state: number = 3;
  notas: string = '';
  id_payment_status: string = '';
  impuestos: string = '';
  descuentos: string = '';
  type_descuentos: string = '';
  deuda: number = 0;
  monto_maximo_credit: number = 0;
  number_dte: string = '';
  generation_code: string = '';
  transaction_date: string = '';
  transaction_time: string = '';
  valid_stamp: string = '';
  addNote: boolean = false;
  id_padre: string = '';
  transaction_padre: string = '';
  comprobantes: { numeroComprobante: string }[] = [];
}

export class Item {
  name: string = '';
  description: string = '';
  cantidad: string = '';
  precio: string = '';
}

export class Detalles {
  item: string = '';
  name: string = '';
  nombre: string = '';
  item_id: string = '';
  item_desc: string = '';
  cantidad: number = 0;
  precio: number = 0;
  iva: number = 0;
  precioiva: number = 0;
  precioiva2: number = 0;
  subtotal: number = 0;
  codigo: number = 0;
  tipo: string = '';
  isgravada: boolean = true;
  isexenta: boolean = false;
  issujeta: boolean = false;
  existencia_producto: number = 0;
  editable: string = 'N';
  codigo_producto: string = '';
  codigo_ingreso: string = '';
  clasificacion: string = '';
  numeroDocumento: string = '';
  renta1: boolean = false;
  renta13: boolean = false;

}

export class DataTransaction {
  customer: string = '';
  type_transaction: string = '';
  id_type_transaction: string = '';
  pay_type: string = '';
  total: string = '';
  date: string = '';
  id: string = '';
  id_padre: string = '';
  transaction_padre: string = '';
  pay_condition: string = '';
  deuda: number = 0;
  generation_code = '';
  customerdata: any;
  days_pass?: any;
  invalid_stamp?: any;
  state: any;
  simple_sale?: any;
  id_user: string = '';
  sale: boolean = true;
}

export class CatalogoModel {
  ID_CATALOGO: string = '';
  ID_MH: string = '';
  VALOR: string = '';
  ALTERNO: string = '';
  ALTERNO_A: string = '';
  ALTERNO_B: string = '';
  GRUPO: string = '';
  SUB_GRUPO: string = '';
  GRUPO_PADRE: string = '';
  CAT_PADRE: string = '';
  ID_MH_PADRE: string = '';
  ID_PADRE: string = '';
  ESTADO: string = '';
  DESCRIPCION: string = '';
  NOTAS: string = '';
  USU_CREA: string = '';
  FEC_CREA: string = '';
  USU_MODIFICA: string = '';
  FEC_MODIFICA: string = '';
  TRASH: string = '';
}
