import { Category } from "../../core/model/category.model";

export interface Rango {
  idRango?: number,
  nombre?: string,
  descripcion?: string,
  inicioRango?: string,
  finalRango?: string,
  actualValor?: string,
  idTipoRango?: any,
  idSucursal?: number,
    catalogo?: Category
}
