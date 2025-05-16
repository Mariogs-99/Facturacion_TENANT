export interface Producto {
    idProducto: number;
    clasificacion: string;
    codigoProducto: string;
    nombre: string;
    descripcion?: string;
    codigoIngreso?: string;
    precio: number;
    iva?: number;
    tipo?: string;
    total?: number;
    estado?: string;
    editable?: string;
    createdAt?: Date;
    updatedAt?: Date;
    deletedAt?: Date | null;
  }
  