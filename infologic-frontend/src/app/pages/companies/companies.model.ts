export class CompaniesModel {
  nameCompany?: string = '';
  descriptionCompany?: string = '';
  addressCompany?: string = '';
  phoneCompany?: string = '';
  emailCompany?: string = '';
  idMuniCompany?: number = 0;
  idDeptoCompany?: number = 0;
  idActividadMH?: number = 0;
  idGiroCompany?: number = 0;
  idRecinto?: number = 0;
  idRegimen?: number = 0;
  idCompany?: number = 0;
  mhUser?: string = '';
  mhPass?: string = '';
  clavePrimariaCert?: string = '';
  active?: boolean = false;
  nombreMunicipioCompany?: string = '';
  nombreDeptoCompany?: string = '';
  nombreGiroCompany?: string = '';
  nombreRegimenCompany?: string = '';
  nombreRecintoCompany?: string = '';
  nit?: string = '';
  nrc?: string = '';
  socialReasonCompany?: string = '';
  file?: string = '';
  branches?: any;
  old_image?: string = '';
}

export class Company {
  nameCompany?: string;
  descriptionCompany?: string;
  addressCompany?: string;
  phoneCompany?: string;
  emailCompany?: string;
  idMuniCompany?: number;
  idDeptoCompany?: number;
  idActividadMh?: number;
  idGiroCompany?: number;
  idRecinto?: number;
  idRegimen?: number;
  idCompany?: number;
  mhUser?: string;
  mhPass?: string;
  clavePrimariaCert?: string;
  active?: boolean = false;
  nombreMunicipioCompany?: string;
  nombreDeptoCompany?: string;
  nombreGiroCompany?: string;
  nombreRegimenCompany?: string;
  nombreRecintoCompany?: string;
  nit?: string;
  nrc?: string;
  socialReasonCompany?: string;
  file?: string;
  branches?: any;
  old_image?: string;
}
