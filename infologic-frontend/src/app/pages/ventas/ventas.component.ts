import { LiveAnnouncer } from '@angular/cdk/a11y';
import * as moment2 from 'moment-timezone';

import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatAccordion } from '@angular/material/expansion';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSelect } from '@angular/material/select';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import * as moment from 'moment';
import { Subscription, catchError, fromEvent, map, merge, of, startWith, switchMap, timestamp } from 'rxjs';
import { CuerpoDto } from 'src/app/core/model/transacciones/cuerpodto.model';
import { ApiService } from 'src/app/core/services/api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { DataService } from 'src/app/core/services/data.service';
import { SharedDataService } from 'src/app/core/services/shared-data-service.service';
import { UtilsService } from 'src/app/core/services/utils.service';
import { environment } from '../../core/environments/environment';
import {
  Cliente,
  DataTransaction as dataTransaction,
  Detalles as detalles,
  Item as item,
  Transaction as transaction
} from '../../core/model/transacciones/transacciones.model';
import { ManageUserComponent } from '../user/dialogs/manage-user/manage-user.component';
import { ModalAnulacionComponent } from './modal-anulacion/modal-anulacion.component';
import { ModalClientesComponent } from './modal-clientes/modal-clientes.component';
import { ModalPdfComponent } from './modal-pdf/modal-pdf.component';
import { ModalVentasComponent } from './modal-ventas/modal-ventas.component';
import { state } from '@angular/animations';
import { formatDate } from '@angular/common';
import { callback } from 'chart.js/dist/helpers/helpers.core';

@Component({
  selector: 'app-ventas',
  templateUrl: './ventas.component.html',
  styleUrls: ['./ventas.component.css'],
})
export class VentasComponent implements OnInit, OnDestroy, AfterViewInit {



  private apiUrl = environment.apiTransmisorUrl + 'api/v1';
  private apiUrlBack = environment.apiServerUrl + 'api/v1';

  subscription!: Subscription;

  public registerForm: FormGroup;
  public registerForm2: FormGroup;
  public emailForm: FormGroup;
  public municipios: Array<any> = [];
  public municipiosArr: Array<any> = [];
  public departamentosArr: Array<any> = [];
  public idsDocumentosAsociados: Array<any> = [];


  currentPageNo: any = 1;
  totalRecords: number = 0;
  paginationSize: number = 10;
  paginationSizeOptions: number[] = [10, 25, 50, 100];
  filter: any = { filterBy: '', sortBy: '' };
  ventasFilter: any = { createdAt: '', cliente: '', tipoDTE: '', numeroDTE: '', codigoGeneracion: '', status: '', totalTransaccion: '' };



  transaction_id: any;
  qr_generado: boolean = false;
  compra: boolean = false;
  public pdfSRC: string = '';
  public src: string = '';
  aplicar_descuento: boolean = false;
  x100_descuento_aplicar: number = 0;
  card: any;
  proveedores: any;
  ventasGravadasNetas = 0.0;
  es_ConsumidorFinal = true;
  impuestos_array: Array<any> = [];
  itemsG: Array<any> = [];
  public permissions: any;
  public isFactura: Boolean = true;
  public isCreditoFiscal: Boolean = false;
  sorteNOAdData: Array<any> = [];
  sortedData: Array<any> = [];
  customer = new Cliente();
  item = new detalles();
  new_item = new item();
  user: Array<any> = [];
  filtered_prods: Array<any> = [];
  items: Array<any> = [];
  items_iniciales: Array<any> = [];

  customers: Array<any> = [];
  details: Array<any> = [];
  noteType: number = 0;
  public invalidacionTipo: any;
  public check_circle: string = 'check_circle';
  inputChange: any = '';
  transaction = new transaction();
  tipoDocumentoList: any = [];
  usuarioActual: any;
  cliente: any;
  totalIva = 0.0;

  option: string = 'customer';
  catalogos: any = {
    actividad_economica: [],
    tipoPersona: [],
    formaDePago: [],
  };
  categorias: any = {
    valor: [],
  };

  filteredOptions: any = {
    clientes: [],
    productos: [],
    tipoIdentificacion: [],
    formaDePago: [],
    actividad_economica: [],
    impuestos: [],
    municipios: [],
  };

  calculos: any = {
    SUMAS: 0.0,
    Gravadas: 0.0,
    Sujetas: 0.0,
    Exentas: 0.0,
    RENTA: 0.0,
    DESCUENTO: 0.0,
    IMPUESTOS: 0.0,
    SUBTOTAL: 0.0,
    IVA: 0.0,
    PERCEPCION: 0.0,
    total: 0.0,
  };

  descuentos: any = {
    porcentaje: '',
    cantidad: 0,
  };

  descuentos_array: Array<any> = [];

  updating: boolean = false;
  updatingNotes: boolean = false;

  isBigTaxPayerSelected: boolean = false;
  isLeadCustomer: boolean = false;
  selected: Array<any> = [];

  view = 'index';
  dataSource = new MatTableDataSource<any>();
  dataTransaction = new MatTableDataSource<any>();
  dataNOATransaction = new MatTableDataSource<any>();
  transactions_array: Array<any> = [];
  transactions: Array<any> = [];
  NOAtransactions: Array<any> = [];
  isNaturalPersonSelected: boolean = false;
  isLegalPersonSelected: boolean = false;
  isConsumidorFinalSelected: boolean = true;
  isForeignCustomerSelected: boolean = false;
  searchProduct: any;
  subscribe: Subscription;
  options: string = '';

  clienteDisable = true;

  IVARetx100: number = 0.01;
  RentaRetx100: number = 0.1;

  @ViewChild('pdfModal') pdfModal!: TemplateRef<any>;
  @ViewChild('aproveModal') aproveModal!: TemplateRef<any>;
  @ViewChild('branchPaginator') branchPaginator!: MatPaginator;


  @ViewChild(MatSort, { static: true }) sort!: MatSort;
  public departamento: string = '';

  public permissionsArr: String[] = [];
  comprobanteForm!: FormGroup;
  indexUrl: string = '';
  municipiosArrFiltered: any[] = [];
  departamentoSelected: any;
  municipioSelected: any;
  constructor(
    private api: ApiService,
    public dialog: MatDialog,
    public util: UtilsService,
    private formBuider: FormBuilder,
    private data: DataService,
    private _liveAnnouncer: LiveAnnouncer,
    private sharedDataService: SharedDataService,
    private _data: DataService,
    private route: ActivatedRoute,
    public _auth: AuthService,
    private fb: FormBuilder
  ) {

    this.departamento = _auth.getDepartamento();

    this.permissionsArr = _auth.getPermissions();

    this.accordion = new MatAccordion();

    this.registerForm = this.formBuider.group({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(250),
      ]),
      comercial_name: new FormControl('', [Validators.maxLength(255)]),
      phone: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
      others: new FormControl(''),
      giro_name: new FormControl(''),
      giro_id: new FormControl(''),
      email: new FormControl('', [
        Validators.required,
        Validators.email,
        Validators.pattern(
          /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        ),
      ]),
      type_doc_id: new FormControl('', [Validators.maxLength(25)]),
      type_doc: new FormControl(''),
      is_juridico: new FormControl(false),
      is_final_consumer: new FormControl(true),
      is_extranjero: new FormControl(false),
      is_gran_contribuyente: new FormControl(false),
      lead_customer: new FormControl(false),
      doc_num_id: new FormControl(''),
      doc_num: new FormControl(''),
      address: new FormControl(''),
      colonia: new FormControl(''),
      calle: new FormControl(''),
      numeroCasa: new FormControl(''),
      apartamentoLocal: new FormControl(''),
      NRC: new FormControl(''),
      NIT: new FormControl(''),
      DUI: new FormControl(''),
      departamento: new FormControl({ value: '', disabled: false }),
      municipio: new FormControl({ value: '', disabled: false }),
      id: new FormControl(''),
      is_natural_person: new FormControl(false),
      subject_type: new FormControl('1695'),

    });

    this.registerForm2 = this.formBuider.group({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(250),
      ]),

      phone: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),

      email: new FormControl('', [
        Validators.required,
        Validators.email,
        Validators.pattern(
          /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        ),
      ]),
      NRC: new FormControl(''),
      address: new FormControl(''),
      colonia: new FormControl(''),
      apartamentoLocal: new FormControl(''),
      calle: new FormControl(''),
      numeroCasa: new FormControl(''),
      nombreMunicipio: new FormControl(''),
      idDepto: new FormControl(''),

      idMunicipio: new FormControl('', [Validators.required]),
      giro_name: new FormControl(''),
      giro_id: new FormControl(''),











      NIT: new FormControl('', [Validators.required]),

    });

    this.emailForm = this.formBuider.group({
      email: new FormControl('', [
        Validators.email,
        Validators.pattern(
          /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        ),
      ]),
    });

    this.subscribe = this._data.getData().subscribe((resp) => {


      if (resp.component == 'VentasComponent' && resp.action == 'open') {

        this.openVenta();
      }


      if (resp.from === 'VentascomponentTable') {
        this._data
          .headerTableProLocal(resp, this.ventasFilter, 'VentascomponentTable')
          .then((response) => {
            if (resp.events === 'filter') {
              this.filter.filterBy = response.filterBy;
            }

            if (resp.value === 'none') {
              this.filter.sortBy = '';
            } else if (response.sortBy !== '') {
              this.filter.sortBy = response.sortBy;
            }

            this.loadCurrentPageVentas(0, this.paginationSize);
          });
      }
    });
  }
  ngAfterViewInit(): void {



    this.dataSource.sort = this.sort;




    this.loadCurrentPageVentas();
  }


  editorConfig: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    height: '225',
    minHeight: '400',
    maxHeight: 'auto',
    width: 'auto',
    minWidth: '0',
    translate: 'no',
    enableToolbar: true,
    showToolbar: true,
    placeholder: 'Ingresar Nota de pedido...',
    defaultParagraphSeparator: '',
    defaultFontName: '',
    defaultFontSize: '',
    fonts: [
      { class: 'arial', name: 'Arial' },
      { class: 'times-new-roman', name: 'Times New Roman' },
      { class: 'calibri', name: 'Calibri' },
      { class: 'comic-sans-ms', name: 'Comic Sans MS' },
    ],
    customClasses: [
      {
        name: 'quote',
        class: 'quote',
      },
      {
        name: 'redText',
        class: 'redText',
      },
      {
        name: 'titleText',
        class: 'titleText',
        tag: 'h1',
      },
    ],
    uploadUrl: 'v1/image',
    toolbarHiddenButtons: [
      [





        'strikeThrough',
        'subscript',
        'superscript',






        'insertUnorderedList',
        'insertOrderedList',
        'heading',

      ],
      [



        'customClasses',
        'link',
        'unlink',
        'insertImage',
        'insertVideo',
        'insertHorizontalRule',
        'removeFormat',
        'toggleEditorMode',
      ],
    ],
  };

  @ViewChild(MatAccordion) accordion: MatAccordion;
  @ViewChild('NOAPAGINATOR') NOApaginator!: MatPaginator;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild('modalAnulacion') modalAnulacion!: TemplateRef<any>;
  @ViewChild(MatTable) table!: MatTable<any>;
  @ViewChild('motivoSelect') motivoSelect!: MatSelect;
  @ViewChild('descripcionTextarea') descripcionTextarea!: ElementRef;

  clientes = [];
  ngOnInit(): void {

    this.clientes = [];
    this.aplicarIVARet = false;
    this.comprobanteValueVacio = true;
    this.CR_manual = false;

    this.dataSource.sort = this.sort;

    this.registerForm.get('is_extranjero')?.valueChanges.subscribe((value) => {
      if (!value) {
        this.registerForm.get('type_doc')?.reset();
        this.registerForm.get('DUI')?.reset();
        this.registerForm.get('NIT')?.reset();
        this.registerForm.get('NRC')?.reset();
      }
    });
    this.comprobanteForm = this.fb.group({
      comprobante: [''],
    });

    this.registerForm.get('giro_name')?.valueChanges.subscribe((name) => {




      let value4 = this.catalogos.actividad_economica.filter((item: any) => {


        if (item.name.includes(name)) {
          return item;
        }
      })[0];
      if (value4 != undefined) {
        this.registerForm.controls['giro_id'].setValue(value4.id);
      }
    });

    this.registerForm.get('type_doc')?.valueChanges.subscribe((name) => {
      let value3 = this.catalogos.tipoIdentificacion.filter((item: any) => {
        if (item.name.includes(name)) {
          return item;
        }
      })[0];
      if (value3 != undefined) {
        this.registerForm.controls['type_doc_id'].setValue(value3.id);
      }
    });

    this.registerForm.get('type_doc')?.valueChanges.subscribe((name) => {
      let value3 = this.catalogos.tipoIdentificacion.filter((item: any) => {


        if (item.name.includes(name)) {
          return item;
        }
      })[0];
      if (value3 != undefined) {
        this.registerForm.controls['type_doc_id'].setValue(value3.id);
      }
    });

    this.opcion =
      this.route.snapshot.paramMap.get('option') != null
        ? (this.route.snapshot.paramMap.get('option') as string)
        : '1';
    this.opcion = '1';

    this.indexUrl =
      this.opcion == '1' || this.opcion == '3'
        ? '/transacciones/index'
        : 'transaction/index/1677';
    this.opcion == '1' ? (this.compra = false) : (this.compra = true);

    /**
     * CONSULTANDO DATOS REQUERIDOS PARA FACTURACION
     */


    if (
      this._auth.getRequiredInvoice() &&
      Object.keys(this._auth.getRequiredInvoice()).length > 0
    ) {
      this.util
        .SWALYESNO(
          'Debes actualizar tu perfil!',
          'Quieres actualizar ahora?',
          'Actualizar ahora',
          'Recuerdame más tarde'
        )
        .then((e) => {
          if (e) {
            const modalData = this.util.openModal({
              title: 'Actualización de Usuario',
              botonAceptar: 'Actualizar',
              componentToLoad: ManageUserComponent,
              callerComponent: this,
              footer: false,
              data: {
                user: {
                  idUser: 0,
                  usuario: this._auth.getLocalData('cnr-info', 'username'),
                },
                requerido: this._auth.getRequiredInvoice(),
                origin: 'ventas',
              },
            });
          }
        });
    }
  }
  ngOnDestroy(): void {


    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  arrayItems = [];
  anularElemento = {};

  ngDoCheck(): void {
    const load_page =
      this.sharedDataService.getVariableValue('load-current-page');

    if (load_page == true) {
      this.loadCurrentPage();
      this.sharedDataService.setVariable('load-current-page', false);
      if (this.transaction.id_doc_transaction_type == '38'|| this.transaction.id_doc_transaction_type == '39') {
        this.filteredOptions.productos = this.completarInformacion(
          this.obtenerItemsUnicos(this.vtasCCF),
          this.items
        );

      }
    }
  }

  vtasCCF = [];
  cuerpoDocCrDTO = {};
  loadCurrentPage(page: number = 0, size: number = 10) {


    this.transacciones = [];
    const load_page =
      this.sharedDataService.getVariableValue('load-current-page');
    this.vtasCCF = this.sharedDataService.getVariableValue('arrayData');
    const vtasCCF2 = this.sharedDataService.getVariableValue('vtasCCF');

    this.transacciones = this.vtasCCF;





    let totalTransacciones = 0.0;



    this.cuerpoDocCrDTO = this.transacciones.map((transaccion, index) => {
      return {
        numeroItem: index + 1,
        tipoDte: transaccion.tipoDte,
        tipoDoc: 1,
        numDocumento: transaccion.nroDocumento,
        fechaEmision: transaccion.fechaEmision,
        montoSujeto: (transaccion.monto - transaccion.ivaRetenido),
        ivaRetenido: transaccion.ivaRetenido,
        descripcion: transaccion.items[0].descripcion,
        tipoRetencion: 'C4',
      };
    });

    if (
      this.transaction.id_doc_transaction_type == '41' ||
      this.transaction.id_doc_transaction_type == '40'
    ) {
      this.calculos.total = this.calculos.SUBTOTAL = this.transacciones.reduce(
        (total, transaction) => total + transaction.monto - transaction.ivaRetenido,
        0
      );

      this.calculos.PERCEPCION = this.transacciones.reduce(
        (ivaRetenido, transaction) => ivaRetenido + transaction.ivaRetenido,
        0
      );

      this.calculos.Gravadas = this.transacciones.reduce(
        (total, transaction) => total + transaction.monto - transaction.ivaRetenido,
        0
      );
    }


  }

  loadCurrentPageVentas(page: number = 0, size: number = 10) {
    this.api.getPage(
      this.indexUrl,
      this.filter.filterBy,
      this.filter.sortBy,
      page,
      size)
      .then((res: any) => {
        this.items = res.data.items;
        this.items_iniciales = res.data.items;
        this.customers = res.data.clientes;
        this.clientes = res.data.clientes;
        this.filteredOptions.clientes = res.data.clientes;
        this.filteredOptions.productos = res.data.items;
        this.filteredOptions.tipoIdentificacion =
          res.data.catalogos.tipoIdentificacion;
        this.filteredOptions.formaDePago = res.data.catalogos.formaDePago;
        this.filteredOptions.actividad_economica =
          res.data.catalogos.actividad_economica;
        this.filteredOptions.impuestos = res.data.catalogos.impuestos;
        this.filteredOptions.proveedors = res.data.proveedores;
        this.filteredOptions.municipios = res.data.catalogos.municipios;
        this.proveedores = res.data.proveedores;
        this.catalogos = res.data.catalogos;
        this.municipios = this.filteredOptions.municipios;
        this.sharedDataService.setVariable('catalogos', res.data.catalogos);
        this.tipoDocumentoList = res.data.catalogos.tipo_rango;
        this.invalidacionTipo = res.data.catalogos.tipoAnulacion;
        this.categorias = res.data.main;
        this.usuarioActual = res.data.users;

        this.municipiosArr = res.data.catalogos.municipios ?? [];
        this.departamentosArr = res.data.catalogos.departamentos ?? [];
        this.idsDocumentosAsociados = res.data.idsDocumentosAsociados ?? [];
        console.log('idsDocumentosAsociados : ', this.idsDocumentosAsociados);



        this.dataSource = new MatTableDataSource<any>(res.data.transactions.content);
        this.totalRecords = res.data.transactions.totalElements;
        this.dataSource.sort = this.sort;




        this.transactions_array = res.data.transaccions;

        if (this.opcion) {
          interface TipoDocumento {
            id: number;
            name: string;
          }
        }

        interface Transaction {
          SIMPLE_SALE: string;
          TEST_MODE: string;

        }


        interface FilteredTransactions {
          filteredTransactions1: Transaction[];
          filteredTransactions2: Transaction[];
          filteredTransactions3: Transaction[];
          filteredTransactions4: Transaction[];
        }

        function getKey(
          transaction: Transaction,
          testModeComparison: '==' | '!='
        ): keyof FilteredTransactions {
          let testModeMatch =
            testModeComparison == '=='
              ? transaction.TEST_MODE == '1'
              : transaction.TEST_MODE != '1';
          return `filteredTransactions${(transaction.SIMPLE_SALE == '1' ? 1 : 2) + (testModeMatch ? 0 : 2)
            }` as keyof FilteredTransactions;
        }

        this.setNOADatatransaction(res.data.noaprobadas);
      });
  }

  public opcion: string = '1';
  campos: Array<detalles> = [new detalles()];
  transacciones: Array<any> = [];
  campo: Array<item> = [new item()];
  camposcat: Array<detalles> = [new detalles()];

  docAsociado: Array<CuerpoDto> = [new CuerpoDto()];

  public displayedColumns: string[] = [
    'fechaTransaccion',
    'customer',
    'type_transaction',
    'numeroDTE',
    'codigoGeneracion',
    'state',
    'total',
    'actions',
  ];

  editar(i: number, agregarNote: number = 0) {



    this.noteType = agregarNote;
    this.updatingNotes = false;

    if (agregarNote) {
      this.transaction.addNote = true;
    } else {
      this.transaction.addNote = false;
    }

    this.closeModal();



    this.impuestos_array = [];
    this.selected = [];
    let transa = this.transactions_array.filter((item: any) => {
      return item.id == i;
    })[0];




    this.transaction.id_doc_transaction_type = transa.ID_DOC_TYPE;
    this.seleccionFacturaCredito(this.transaction.id_doc_transaction_type);

    if (transa.VALID_STAMP) {
      this.transaction.number_dte = transa.NUMBER_DTE;
    }

    this.transaction.id_payment_cond = transa.ID_PAYMENT_COND;
    this.transaction.notas = transa.NOTES;
    this.validationcredit = false;
    this.transaction.id_credit_time = transa.ID_CREDIT_TIME;
    this.transaction.id_user = transa.ID_USER;
    this.transaction.id = transa.ID_TRANSACTIONS;
    this.transaction.id_padre = transa.id_padre;
    this.transaction.transaction_padre = transa.transaccion_padre;
    this.transaction.id_user_name = transa.customers.NAME_CUSTOMER;
    this.transaction.num_transaction = transa.NUM_TRANSACTIONS;
    this.inputChange = transa.customers.NAME_CUSTOMER;
    this.transaction.deuda = this.getTotalamount(transa.ID_CUSTUMER);
    this.transaction.id_transaction_type = this.opcion == '2' ? '1677' : '1676';
    this.transaction.number_dte = transa.NUMBER_DTE;
    this.transaction.generation_code = transa.GENERATION_CODE;
    this.transaction.transaction_date = transa.TRANSACTION_DATE;
    this.transaction.transaction_time = transa.TRANSACTION_TIME;
    this.transaction.valid_stamp = transa.VALID_STAMP;
    this.transaction.state = transa.STATE;

    if (transa.withPerception) {
      this.isBigTaxPayerSelected = true;
      this.isLeadCustomer = true;
    } else {
      this.isBigTaxPayerSelected = false;
      this.isLeadCustomer = false;
    }

    if (agregarNote) {
      this.transaction.id_doc_transaction_type = agregarNote == 1 ? '38' : '39';
    } else {
      this.transaction.id_doc_transaction_type = transa.ID_DOC_TYPE;

      if (transa.id_padre) {
        this.noteType = transa.ID_DOC_TYPE == '38' ? 1 : 2;
        this.transaction.id_doc_transaction_type =
          this.noteType == 1 ? '38' : '39';
        this.updatingNotes = true;
      }
    }

    transa.items_transactions.forEach((res: any) => {
      let it: any = this.items.filter((item: any) => {
        if (parseInt(res.ID_ITEMS) == parseInt(item.id)) {
          return item;
        }
      })[0];

      if (it && res.QUANTITY_ITEMS > 0) {
        if (it.nombre != '') {



          let data: detalles = {
            item: it.name,
            name: it.nombre,
            nombre: it.nombre,
            item_id: it.id,
            item_desc: it.descripcion,
            iva: it.iva,
            cantidad: res.QUANTITY_ITEMS,
            precio: this.isCreditoFiscal
              ? it.precio
              : it.precio + it.precio * 0.13,
            precioiva: it.precio * 0.13,
            precioiva2: it.precio / 1.13,
            subtotal:
              (this.isCreditoFiscal ? it.precio : it.total) *
              res.QUANTITY_ITEMS,
            codigo: it.code,
            tipo: it.tipo,
            isgravada: it.tipo == 'G' ? true : false,
            isexenta: it.tipo == 'E' ? true : false,
            issujeta: it.tipo == 'NS' ? true : false,
            existencia_producto: 0,
            editable: it.editable,
            codigo_producto: it.codigo_producto,
            codigo_ingreso: it.codigo_ingreso,
            clasificacion: it.clasificacion,
            numeroDocumento: it.numeroDocumento,
            renta1: false,
            renta13: false,
          };
          let seleccion = '1';
          seleccion =
            res.VTA_GRAVADA > 0 ? '1' : res.VTA_EXENTA > 0 ? '2' : '3';

          this.selected.push(seleccion);


          this.campos.push(data);
        }
      }

    });

    this.calculoTotales();
    this.updating = true;
    this.view = 'forms';
  }

  eliminar(i: number) { }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  setview(view: string) {
    this.noteType = 0;
    this.view = view;
    this.isBigTaxPayerSelected = false;
    this.isLeadCustomer = false;
    this.selected = ['1'];
    this.transaction = new transaction();
    this.transactions = [];
    this.campos = [new detalles()];


    this.updating = false;

    this.noteType = 0;
    this.inputChange = '';
    this.calculos.SUMAS = 0.0;
    this.calculos.Sujetas = 0.0;
    this.calculos.Gravadas = 0.0;
    this.calculos.Exentas = 0.0;
    this.calculos.DESCUENTO = 0.0;
    this.calculos.SUBTOTAL = 0.0;
    this.calculos.IVA = 0.0;
    this.calculos.PERCEPCION = 0.0;
    this.calculos.total = 0.0;
  }

  mascara: string = '99999999-9';
  simplemaskfunct(event: any) {
    if (event.length > 9) {
      this.mascara = '9999-999999-999-9';
    }
    if (event.length < 8) {
      this.mascara = '99999999-9';
    }
  }

  comprobanteValueVacio = true;
  validarComprobante() {
    const comprobanteValue = this.comprobanteForm.get('comprobante')?.value;
    this.comprobanteValueVacio = !comprobanteValue;

    if (comprobanteValue) {
      this.util.showSWAL(
        'Validar Comprobante de pago',
        'Antes de Continuar debe Validar y Agregar el comprobante de Pago',
        'Aceptar',
        'error'
      );
      return;
    } else {

    }
  }

  validationcredit: boolean = true;
  campos_temp: Array<detalles> = [new detalles()];

  manageTransaction(opt: number = 1) {

    this.filteredOptions.productos = this.items;




    if (
      !this.transaction.payment_type &&
      (this.transaction.id_doc_transaction_type == '35' ||
        this.transaction.id_doc_transaction_type == '36' ||
        this.transaction.id_doc_transaction_type == '44')
    ) {
      return this.util.showSWAL(
        'Faltan Datos',
        '<b>Debe Indicar una Forma de Pago</b>',
        'Aceptar',
        'error'
      );

    
    }








    this.validarComprobante();

    if (!this.comprobanteValueVacio) {
      return;
    }




    this.campos_temp = this.campos;










    if (
      !this.validarCantidades(this.campos) &&
      this.transaction.id_doc_transaction_type != '44' &&
      this.transaction.id_doc_transaction_type != '41' &&
      this.transaction.id_doc_transaction_type != '40'
    ) {
      return this.util.showSWAL(
        'Faltan Datos',
        '<b>Productos :</b> Las Cantidades deben ser al menos 1 elemento y máximo 2000',
        'Aceptar',
        'error'
      );

  
    }





    if (
      this.cliente.actividad_nombre == '' &&
      ![39, 40, 41].includes(+this.transaction.id_doc_transaction_type)
    ) {
      return this.util.showSWAL(
        'Faltan Datos',
        'Seleccione una actividad Económica ó Giro para el Cliente.',
        'error',
        'error'
      );
    }

    if (
      this.CR_manual == true &&
      this.transaction.id_payment_type == '' &&
      !(
        this.transaction.id_doc_transaction_type == '38' ||
        this.transaction.id_doc_transaction_type == '39' ||
        this.transaction.id_doc_transaction_type == '40' ||
        this.transaction.id_doc_transaction_type == '41'
      )
    ) {
      return this.util.showSWAL(
        'Faltan Datos',
        'Tipo de pago no puede ir vacios',
        'error',
        'error'
      );
    }

    if (
      this.transaction.id_doc_transaction_type == '' ||
      (this.transaction.id_customer == '' &&
        this.transaction.id_doc_transaction_type != '41' &&
        this.CR_manual != false)
    ) {
      if (this.transaction.id_customer == '') {
        return this.util.showSWAL(
          'Faltan Datos',
          'Nombre del Cliente no puede ir vacio',
          'error',
          'error'
        );
      }

      return this.util.showSWAL(
        'Faltan Datos',
        'Revise y vuelva a llenar los campos del Formulario',
        'error',
        'error'
      );
    }

    /* if (this.isCreditoFiscal) {
      this.campos.forEach((res, index) => {
        this.campos[index].precio = this.campos[index].precio;
      });
    } else {
      this.campos.forEach((res, index) => {
        this.campos[index].precio = this.campos[index].precioiva;
      });
    }
    this.campos.forEach((res, index) => {
      res.tipo = res.isexenta
        ? 'E'
        : res.isgravada
          ? 'G'
          : res.issujeta
            ? 'NS'
            : '';
    }); */


    if (!this.isCreditoFiscal) {
      this.campos.forEach((res) => {
        res.precio = res.precioiva;
      });
    }
    
    this.campos.forEach((res) => {
      if (res.isexenta) {
        res.tipo = 'E';
      } else if (res.isgravada) {
        res.tipo = 'G';
      } else if (res.issujeta) {
        res.tipo = 'NS';
      } else {
        res.tipo = '';
      }
    });
    
    

    this.campos_temp = this.campos;












    this.transaction.items = JSON.stringify(this.campos);

    let data = {
      customer_state: 1,
      DUI: this.registerForm.controls['DUI'].value,
      NIT: this.registerForm.controls['NIT'].value,
      NRC: this.registerForm.controls['NRC'].value,
      comercial_name: this.registerForm.controls['comercial_name'].value,
      doc_num: this.registerForm.controls['doc_num'].value,
      doc_num_id: this.registerForm.controls['doc_num_id'].value,
      email: this.registerForm.controls['email'].value,
      giro_id: this.registerForm.controls['giro_id'].value,
      giro: this.registerForm.controls['giro_name'].value,
      id: this.registerForm.controls['id'].value,
      is_juridico: this.isLegalPersonSelected ? 1 : 0,
      is_final_consumer: this.isConsumidorFinalSelected ? 1 : 0,
      is_natural_person: this.isNaturalPersonSelected ? 1 : 0,
      is_gran_contribuyente: this.isBigTaxPayerSelected ? 1 : 0,
      lead_customer: this.isLeadCustomer ? 1 : 0,
      name: this.registerForm.controls['name'].value,
      others: this.registerForm.controls['others'].value,
      phone: this.registerForm.controls['phone'].value,
      type_doc: this.registerForm.controls['type_doc'].value,
      type_doc_id: this.registerForm.controls['type_doc_id'].value,
      address: this.registerForm.controls['address'].value,
      colonia: this.registerForm.controls['colonia'].value,
      calle: this.registerForm.controls['calle'].value,
      numeroCasa: this.registerForm.controls['numeroCasa'].value,
      apartamentoLocal: this.registerForm.controls['apartamentoLocal'].value,
      departamento: this.registerForm.controls['departamento'].value,
      municipio: this.registerForm.controls['municipio'].value,
      is_extranjero: this.isForeignCustomerSelected ? 1 : 0,
      subject_type: this.registerForm.value['subject_type'],
      editable: false,
      fecha: '',
      id_branch: '',
      monto_maximo_credit: 0,
    };

    let data2 = {
      customer_state: 1,
      name: this.registerForm2.controls['name'].value,
      email: this.registerForm2.controls['email'].value,
      phone: this.registerForm2.controls['phone'].value,
      nit: this.registerForm2.controls['NIT'].value,
      NRC: this.registerForm2.controls['NRC'].value,
      idMunicipio: this.registerForm2.controls['idMunicipio'].value,
      nombreMunicipio: this.registerForm2.controls['nombreMunicipio'].value,
      nombreDepartamento: this.registerForm2.controls['idDepto'].value,
      direccion: this.registerForm2.controls['address'].value,
      colonia: this.registerForm2.controls['colonia'].value,
      calle: this.registerForm2.controls['calle'].value,
      numeroCasa: this.registerForm2.controls['numeroCasa'].value,
      apartamentoLocal: this.registerForm2.controls['apartamentoLocal'].value,
      giro_id: this.registerForm2.controls['giro_id'].value,
      giro: this.registerForm2.controls['giro_name'].value,
    };

    if (
      this.transaction.id_doc_transaction_type == '40' &&
      this.CR_manual == true
    ) {


      if (data2.NRC == '' || !data2.NRC) {
        return this.util.showSWAL(
          'FORMULARIO INCOMPLETO',
          'Por favor Digite el NRC del Cliente',
          'Aceptar',
          'error'
        );
      }

      if (data2.giro == null || data2.giro == '') {
        return this.util.showSWAL(
          'FORMULARIO INCOMPLETO',
          'Por favor Seleccione la Actividad Económica del Cliente',
          'Aceptar',
          'error'
        );
      }

      if (data2.direccion == null || data2.direccion == '') {
        return this.util.showSWAL(
          'FORMULARIO INCOMPLETO',
          'Por favor Digite la Dirección del Cliente',
          'Aceptar',
          'error'
        );
      }
    }

    if (this.transaction.id_doc_transaction_type == '41') {
      data2 = {
        customer_state: 1,
        name: 'CENTRO NACIONAL DE REGISTROS',
        email: 'rcom@cnr.gob.sv',
        phone: '25935000',
        nit: '06140512941026',
        NRC: '887633',
        idMunicipio: '14',
        nombreMunicipio: 'SAN SALVADOR',
        nombreDepartamento: 'SAN SALVADOR',
        direccion: '1a Calle Poniente y 43 Avenida Norte No. 2310',
        colonia: '',
        apartamentoLocal: '',
        calle: '',
        numeroCasa: '',

        giro: 'ACTIVIDADES DE LA ADMINISTRACIÓN PÚBLICA EN GENERAL',
        giro_id: '84110',
      };
    }

    if (this.transaction.id_doc_transaction_type != '40') {

      if (
        (data.email == null && data2.email == null) ||
        (data.email == '' && data2.email == '')
      ) {
        this.util.showSWAL(
          'Correo Electrónico Requerido',
          'Debe Escribir un Correo electrónico válido',
          'Aceptar',
          'info'
        );
        return;
      }


      if (
        (data.phone == null && data2.phone == null) ||
        (data.phone == '' && data2.phone == '')
      ) {
        this.util.showSWAL(
          'Número Telefónico Requerido',
          'Debe Escribir un número de Teléfono válido',
          'Aceptar',
          'info'
        );
        return;
      }

      if (data.giro == '' && this.transaction.id_doc_transaction_type == '39') {
        this.util.showSWAL(
          'Giro de la Empresa Requerido',
          'Debe Seleccionar una Actividad Económica',
          'Aceptar',
          'info'
        );
        return;
      }


      if (this.transaction.id_customer == 'add2') {
        if (this.registerForm2.invalid) {


          let errores = '';
          Object.keys(this.registerForm2.controls).forEach((key) => {
            const control = this.registerForm2.get(key);
            if (control != null) {
              const controlErrors = control.errors;
              if (controlErrors != null) {
                Object.keys(controlErrors).forEach((keyError) => {
                  errores =
                    'Campo inválido: ' +
                    key +
                    ', error: ' +
                    keyError +
                    ', valor: ' +
                    controlErrors[keyError];
                });
              }
            }
          });
          return this.util.showSWAL(
            'ERROR EN FORMULARIO 2',
            errores,
            'error',
            'error'
          );
        }
      } else {
        if (
          this.registerForm.invalid &&
          this.transaction.id_doc_transaction_type != '41'
        ) {


          let errores = '';
          Object.keys(this.registerForm.controls).forEach((key) => {
            const control = this.registerForm.get(key);
            if (control != null) {
              const controlErrors = control.errors;
              if (controlErrors != null) {
                Object.keys(controlErrors).forEach((keyError) => {
                  errores =
                    'Campo inválido: ' +
                    key +
                    ', error: ' +
                    keyError +
                    ', valor: ' +
                    controlErrors[keyError];
                });
              }
            }
          });
          return this.util.showSWAL(
            'ERROR EN FORMULARIO 1',
            errores,
            'error',
            'error'
          );
        }
      }


      if (
        this.registerForm2.invalid &&
        this.registerForm.invalid &&
        this.transaction.id_doc_transaction_type != '41'
      ) {
        if (this.registerForm2.invalid) {
          let errores = '';
          Object.keys(this.registerForm2.controls).forEach((key) => {
            const control = this.registerForm2.get(key);
            if (control != null) {
              const controlErrors = control.errors;
              if (controlErrors != null) {
                Object.keys(controlErrors).forEach((keyError) => {
                  errores =
                    'Campo inválido: ' +
                    key +
                    ', error: ' +
                    keyError +
                    ', valor: ' +
                    controlErrors[keyError];
                });
              }
            }
          });
          return this.util.showSWAL(
            'ERROR EN FORMULARIO',
            errores,
            'error',
            'error'
          );
        }

      }

      if (this.transaction.id_customer == 'add' && this.registerForm.invalid) {
        if (this.registerForm.invalid) {
          let errores = '';
          Object.keys(this.registerForm.controls).forEach((key) => {
            const control = this.registerForm.get(key);
            if (control != null) {
              const controlErrors = control.errors;
              if (controlErrors != null) {
                Object.keys(controlErrors).forEach((keyError) => {
                  errores =
                    'Campo inválido: ' +
                    key +
                    ', error: ' +
                    keyError +
                    ', valor: ' +
                    controlErrors[keyError];
                });
              }
            }
          });
          return this.util.showSWAL(
            'ERROR EN FORMULARIO',
            errores,
            'error',
            'error'
          );
        }

      }












      if (
        this.transaction.id_customer == 'add2' &&
        this.registerForm2.invalid
      ) {
        if (!this.registerForm2.value['name']) {
          return this.util.showSWAL(
            'FORMULARIO INCOMPLETO',
            'Por favor Digite el Nombre del Receptor',
            'Aceptar',
            'error'
          );
        }

        if (!this.registerForm2.value['email']) {
          return this.util.showSWAL(
            'FORMULARIO INCOMPLETO',
            'Por favor Digite el Correo Electrónico del Receptor',
            'Aceptar',
            'error'
          );
        }

        if (!this.registerForm2.value['phone']) {
          return this.util.showSWAL(
            'FORMULARIO INCOMPLETO',
            'Por favor Digite el Número Telefónico del Receptor',
            'Aceptar',
            'error'
          );
        }


        if (this.registerForm2.invalid) {
          let errores = '';
          Object.keys(this.registerForm2.controls).forEach((key) => {
            const control = this.registerForm2.get(key);
            if (control != null) {
              const controlErrors = control.errors;
              if (controlErrors != null) {
                Object.keys(controlErrors).forEach((keyError) => {
                  errores =
                    'Campo inválido 2: ' +
                    key +
                    ', error: ' +
                    keyError +
                    ', valor: ' +
                    controlErrors[keyError];
                });
              }
            }
          });
          return this.util.showSWAL(
            'ERROR EN FORMULARIO 2',
            errores,
            'error',
            'error'
          );
        }

      }
    }




    this.transaction.impuestos = JSON.stringify(this.impuestos_array);

    this.transaction.type_descuentos = this.descuentos.porcentaje;
    this.transaction.descuentos = this.descuentos.cantidad;





    if (this.updating) {
      this.validationcredit = this.compareTransactions(this.transaction.id);
    }

    if (
      this.calculos.total > this.transaction.monto_maximo_credit &&
      this.transaction.id_payment_cond == '458' &&
      this.validationcredit
    ) {
      return this.util
        .SWALYESNO(
          'Monto maximo de credito Superado',
          'Monto de credito Maximo: $' +
          this.transaction.monto_maximo_credit +
          '<br>Monto total de deuda: $' +
          this.transaction.deuda.toFixed(2) +
          '<br>Monto total de transaccion: $' +
          this.calculos.total.toFixed(2) +
          '<br><b>¿Desea enviar a revision para aprobacion?</b>',
          'Enviar a aprobacion'
        )
        .then((res) => {
          if (res) {
            this.transaction.state = 1697;
            this.transaction.id_payment_status = '1687';
            this.transaction.monto_maximo_credit =
              this.calculos.total.toFixed(2);
            this.manageTransaction(1);
          } else {

            return;
          }
        });
    } else {
    }







    switch (opt) {
      case 1:
        if (!this.customer?.id) {
          this.customer = data;
          this.storeCustomer(() => {
            if (this.transaction.state == 1696) {
              this.transaction.id_payment_status = '1687';
            }
            if (
              this.transaction.state == 1698 &&
              this.transaction.id_payment_cond == '457'
            ) {
              this.transaction.state = 1697;
            } else {

            }








            this.totalIva = 0;

            this.campos.forEach((detalle) => {


              let descuento = this.descuentos.porcentaje;
              const itemCNR = {
                nombreItem: detalle.item,
                descripcion:
                  detalle.item_desc != '' ? detalle.item_desc : detalle.item,
                precioUnitario: this.isCreditoFiscal
                  ? detalle.precio
                  : detalle.precioiva,
                tipoItem: 'BIENES',
              };
              this.totalIva = detalle.isgravada
                ? this.totalIva + detalle.precio * detalle.cantidad
                : 0;

              let total = 0;
              total = (detalle.cantidad * (detalle.precio ?? 0));
              let ivaItem = 0.0;





              let opcion_a =
                detalle.cantidad *
                (detalle.precioiva - detalle.precioiva / 1.13) -
                (detalle.cantidad *
                  (detalle.precioiva - detalle.precioiva / 1.13) *
                  this.cliente?.descuento) /
                100;

              let opcion_b =
                detalle.cantidad *
                (detalle.precioiva - detalle.precioiva / 1.13);





              ivaItem =
                this.transaction.id_customer != 'add2'
                  ? detalle.cantidad *
                  (detalle.precioiva - detalle.precioiva / 1.13) -
                  (detalle.cantidad *
                    (detalle.precioiva - detalle.precioiva / 1.13) *
                    this.cliente?.descuento) /
                  100
                  : detalle.cantidad *
                  (detalle.precioiva - detalle.precioiva / 1.13);



              const newItem = {
                itemCNR: itemCNR,
                cantidad: detalle.cantidad,
                clasificacion: detalle.clasificacion,
                codigoProducto: detalle.codigo_producto,
                codigoIngreso: detalle.codigo_ingreso,
                editable: detalle.editable,
                precioUnitario: this.isCreditoFiscal
                  ? detalle.precio
                  : detalle.precioiva,




                ivaItem: detalle.isgravada ? ivaItem : 0,
                montoDescuento: this.x100_descuento_aplicar == 100 ? 0 : (this.descuentos.porcentaje
                  ? detalle.precio - this.descuentos.porcentaje * detalle.precio
                  : 0),
                ventaNosujeta:
                  detalle.issujeta == true
                    ? total.toFixed(4)
                    : 0,
                ventaExenta:
                  detalle.isexenta == true
                    ? this.transaction.id_customer != 'add2'
                      ? total - (this.cliente?.descuento / 100) * total
                      : total
                    : 0,
                ventaGravada:
                  detalle.isgravada == true
                    ? this.transaction.id_customer != 'add2'
                      ? total - (this.cliente?.descuento / 100) * total
                      : total
                    : 0,
                ventaNogravada: 0,


                codigo: detalle.codigo,
                numeroDocumento: detalle.numeroDocumento,
              };


              this.itemsG.push(newItem);

            });



            var valor =
              this.transaction.id_doc_transaction_type == '35'
                ? 'FACTURA'
                : 'CREDITO FISCAL';




            let tipoDTE: string =
              this.transaction.id_doc_transaction_type.toString();



            let tipoDocumento: string;

            switch (tipoDTE) {
              case '35':
                tipoDocumento = 'FACTURA';
                break;
              case '36':
                tipoDocumento = 'COMPROBANTE_CREDITO_FISCAL';
                break;
              case '37':
                tipoDocumento = 'NOTA_REMISION';
                break;
              case '38':
                tipoDocumento = 'NOTA_CREDITO';
                break;
              case '39':
                tipoDocumento = 'NOTA_DEBITO';
                break;
              case '40':
                tipoDocumento = 'COMPROBANTE_RETENCION';
                break;
              case '41':
                tipoDocumento = 'COMPROBANTE_LIQUIDACION';
                break;
              case '42':
                tipoDocumento = 'DOCUMENTO_CONTABLE_LIQUIDACION';
                break;
              case '43':
                tipoDocumento = 'FACTURAS_EXPORTACION';
                break;
              case '44':
                tipoDocumento = 'FACTURA_SUJETO_EXCLUIDO';
                break;
              case '45':
                tipoDocumento = 'COMPROBANTE_DONACION';
                break;
              default:
                tipoDocumento = 'Tipo de documento no reconocido';
            }



            let tipoPago: string;
            switch (this.transaction.payment_type) {
              case 'BILLETES Y MONEDAS':
                tipoPago = 'BILLETES_Y_MONEDAS';
                break;
              case 'TARJETA DÉBITO':
                tipoPago = 'TARJETA_DEBITO';
                break;
              case 'TARJETA CRÉDITO':
                tipoPago = 'TARJETA_CREDITO';
                break;
              case 'CHEQUE':
                tipoPago = 'CHEQUE';
                break;
              case 'TRANSFERENCIA DEPÓSITO BANCARIO':
                tipoPago = 'TRANSFERENCIA_DEPOSITO_BANCARIO';
                break;
              case 'VALES O CUPONES':
                tipoPago = 'VALES_O_CUPONES';
                break;
              case 'DINERO ELECTRÓNICO':
                tipoPago = 'DINERO_ELECTRONICO';
                break;
              case 'MONEDERO ELECTRÓNICO':
                tipoPago = 'MONEDERO_ELECTRONICO';
                break;
              case 'CERTIFICADO O TARJETA DE REGALO':
                tipoPago = 'CERTIFICADO_O_TARJETA_DE_REGALO';
                break;
              case 'BITCOIN':
                tipoPago = 'BITCOIN';
                break;
              case 'OTRAS CRIPTOMONEDAS':
                tipoPago = 'OTRAS_CRIPTOMONEDAS';
                break;

              case 'CUENTAS POR PAGAR DEL RECEPTOR':
                tipoPago = 'CUENTAS_POR_PAGAR_DEL_RECEPTOR';
                break;
              case 'GIRO BANCARIO':
                tipoPago = 'GIRO_BANCARIO';
                break;

              case 'OTROS (SE DEBE INDICAR EL MEDIO DE PAGO)':
                tipoPago = 'BILLETES_Y_MONEDAS';
                break;

              default:
                tipoPago = 'BILLETES_Y_MONEDAS';
                break;
            }



            // const fechaActual = this.formatDate2(new Date());

            const horaActual: number = new Date().getTime();

            const fechaActual = this.formatDateAndTime(new Date());




            if (!this.validarClienteCNR()) {
              return;
            }

            let data5: any[] = [];

            if (
              this.transaction.id_doc_transaction_type == '40' &&
              this.CR_manual != false
            ) {
              if (this.docAsociado && this.docAsociado.length > 0) {
                data5 = this.docAsociado.map((transaccion, index) => {


                  if (this.validarCampos(transaccion)) {
                    return {
                      numeroItem: index + 1,
                      tipoDte: transaccion.tipoDte,
                      // tipoDoc: transaccion.tipoGeneracion, //habilitar en producción
                      tipoDoc: "01", //habilitar en producción
                      numDocumento: transaccion.nroDocumento,
                      codigoGeneracion: transaccion.nroDocumento,
                      fechaEmision: new Date(transaccion.fechaEmision)
                        .toISOString()
                        .split('T')[0],
                      montoSujeto: transaccion.monto,
                      ivaRetenido: transaccion.ivaRetenido,
                      descripcion: transaccion.descripcion,
                      tipoRetencion: '22',
                    };
                  } else {







                    return;
                  }
                });



                let totalMontoSujeto = 0;
                let totalIvaRetenido = 0;

                data5.forEach((item) => {
                  totalMontoSujeto += Number(item.montoSujeto);
                  totalIvaRetenido += item.ivaRetenido;
                });



                this.calculos.total = totalMontoSujeto;
                this.calculos.Gravadas = totalMontoSujeto;
                this.calculos.PERCEPCION = totalIvaRetenido;

                this.cuerpoDocCrDTO = data5;

                this.transaction.id_customer = 'add2';
              }
            }







            if (this.transaction.id_doc_transaction_type == '40') {
              this.nombre_giro = data2.giro;
            } else {

              this.nombre_giro =
                this.transaction.id_customer != 'add2' &&
                  this.transaction.id_doc_transaction_type != '41'
                  ? data.giro
                  : data2.giro ?? 'OTROS';
            }


            this.registerForm.controls['municipio']?.disable({
              onlySelf: false
            })



            let transaccionDTO = {
              idUser: 1,
              idSucursal: 1,
              fechaTransaccion: fechaActual,
              horaTransaccion: horaActual,
              clienteCNR: {
                idCliente: null,
                nombre:
                  this.transaction.id_customer != 'add2' &&
                    this.transaction.id_doc_transaction_type != '41'
                    ? this.customer.name
                    : data2.name,
                porcentajeDescuento:
                  this.transaction.id_customer != 'add2' &&
                    this.transaction.id_doc_transaction_type != '41'
                    ? this.cliente?.descuento ?? 0.0
                    : 0.0,
                descripcionDescuento: '',




                email:

                  this.transaction.id_customer != 'add' &&
                    this.transaction.id_doc_transaction_type != '41'
                    ? data.email
                    : data2.email,

                actividadEconomica:
                  this.nombre_giro != '' ? this.nombre_giro : 'OTROS',










                nrc:
                  tipoDocumento != 'FACTURA'
                    ? this.transaction.id_customer != 'add2' &&
                      this.transaction.id_doc_transaction_type != '41'
                      ? data.NRC
                      : data2.NRC ?? ''
                    : null,

                actividadEconomicaCodigo:
                  this.codigo_giro != ''
                    ? this.nombre_giro
                    : this.transaction.id_customer != 'add'
                      ? this.transaction.id_customer != 'add2'
                        ? this.cliente?.actividad_nombre
                        : data2.giro
                      : data.giro,
                telefono:
                  this.transaction.id_customer != 'add'
                    ? this.transaction.id_customer != 'add2'
                      ? data.phone
                      : data2.phone
                    : '',
                tipoDocumento:

                  Object.keys(this.cliente).length === 0
                    ? 'NIT'
                    : this.cliente.es_contribuyente != 'S'
                      ? 'DUI'
                      : 'NIT',


                TipoPersona:
                  this.transaction.id_customer != 'add'
                    ? 'PERSONA_NATURAL'
                    : data.is_natural_person
                      ? 'PERSONA_NATURAL'
                      : data.is_juridico
                        ? 'PERSONA_JURIDICA'
                        : 'PERSONA_NATURAL',
                numeroDocumento:
                  Object.keys(this.cliente).length === 0
                    ? data2.nit
                    : this.cliente.es_contribuyente != 'S'
                      ? this.formatDUI(this.cliente?.dui ?? '')
                      : this.transaction.id_customer != 'add2'
                        ? this.cliente?.nit
                        : data2.nit,







                nombreComercial:
                  this.transaction.id_customer != 'add'
                    ?


                    this.transaction.id_customer != 'add2'
                      ? data.comercial_name
                      : data2.name
                    : '',

                nit:
                  this.transaction.id_customer != 'add2'
                    ? this.cliente?.nit
                    : data2.nit,

                pais: 'EL SALVADOR',
                departamento:
                  this.departamentoSelected?.id_mh ?? this.departamentoSelected,
                departamentocod: this.departamentoSelected?.id_mh ?? this.departamentoSelected,
                municipio:
                  this.municipioSelected?.id_mh ?? this.municipioSelected,
                municipiocod: this.municipioSelected?.id_mh ?? this.municipioSelected,
                direccion:
                  this.transaction.id_customer != 'add2'
                    ? data.address
                    : data2.direccion ?? 'Sin Direccion',
                    colonia:
                    this.transaction.id_customer != 'add2'
                      ? data.colonia
                      : data2.colonia ?? 'Sin Colonia',
                      apartamentoLocal:
                      this.transaction.id_customer != 'add2'
                        ? data.apartamentoLocal
                        : data2.apartamentoLocal ?? 'Sin Direccion',
                        numeroCasa:
                        this.transaction.id_customer != 'add2'
                          ? data.numeroCasa
                          : data2.numeroCasa ?? 'Sin Direccion',
                          calle:
                          this.transaction.id_customer != 'add2'
                            ? data.calle
                            : data2.calle ?? 'Sin Direccion',




                esExtranjero: this.isForeignCustomerSelected ? 1 : 0,
                esGranContribuyente: this.isBigTaxPayerSelected ? 1 : 0,
                esGobierno: false,
                esConsumidorFinal: this.cliente.es_contribuyente != 'S',
              },

              items:
                this.transaction.id_doc_transaction_type != '40'
                  ? this.itemsG
                  : [],

              cuerpoDocCrDTO:
                this.transaction.id_doc_transaction_type == '40'
                  ? this.cuerpoDocCrDTO
                  : null,

              documentosAsociados:
                this.transaction.id_doc_transaction_type != '40'
                  ? this.transacciones
                  : null,

              formaDePago: tipoPago,
              comprobantePagos: this.transaction.comprobantes,
              tipoDTE: tipoDocumento,

              notas: this.transaction.notas ?? '',
              total:
                this.transaction.id_doc_transaction_type == '40'
                  ? this.calculos.total != null
                    ? (this.calculos.Gravadas)
                    : 0
                  : this.calculos.total != null
                    ? (this.calculos.total)
                    : 0.0,

              totalGravado:
                this.transaction.id_doc_transaction_type == '40'
                  ? this.calculos.Gravadas ?? 0.0
                  : (this.calculos.Gravadas -
                    (this.calculos.Gravadas *
                      (this.transaction.id_customer != 'add2'
                        ? this.cliente.descuento ?? 0
                        : 0)) /
                    100),




              totalNosujeto: this.calculos.Sujetas ?? 0.0,



              totalExento: this.calculos.Exentas
                ? this.calculos.Exentas
                : 0.0,
              totalNogravado: 0.0,
              totalIVA:
                this.transaction.id_doc_transaction_type == '36' ||
                  this.transaction.id_doc_transaction_type == '38' ||
                  this.transaction.id_doc_transaction_type == '39'
                  ? this.calculos.Gravadas * 0.13 -
                  (this.calculos.Gravadas *
                    0.13 *
                    (this.cliente?.descuento ?? 0)) /
                  100
                  : this.transaction.id_doc_transaction_type == '40'
                    ? 0
                    : this.transaction.id_doc_transaction_type == '44'
                      ? 0
                      : this.calculos.Gravadas -
                      this.calculos.Gravadas / 1.13 -
                      ((this.calculos.Gravadas - this.calculos.Gravadas / 1.13) *
                        (this.cliente?.descuento ?? 0)) /
                      100,

              descuentoExento:
                this.transaction.id_doc_transaction_type == '40'
                  ? 0
                  : this.transaction.id_doc_transaction_type == '44'
                    ? 0
                    : (this.calculos.Exentas *
                      (this.transaction.id_customer != 'add2'
                        ? this.cliente?.descuento ?? 0
                        : 0)) /
                    100,


              descuentoNosujeto:
                this.transaction.id_doc_transaction_type == '40'
                  ? 0
                  : this.transaction.id_doc_transaction_type == '44'
                    ? 0
                    : (this.calculos.Sujetas *
                      (this.transaction.id_customer != 'add2'
                        ? this.cliente?.descuento
                        : 0)) /
                    100,
              descuentoGravado:
                this.transaction.id_doc_transaction_type == '40'
                  ? 0
                  : this.transaction.id_doc_transaction_type == '44'
                    ? 0
                    : (this.calculos.Gravadas *
                      (this.transaction.id_customer != 'add2'
                        ? this.cliente?.descuento
                        : 0)) /
                    100,

              ivaRetenido: Math.abs(this.calculos.PERCEPCION),
              rentaRetenido:
                this.transaction.id_doc_transaction_type == '40'
                  ? 0
                  : (this.calculos.RENTA ?? 0.0).toFixed(2),
              saldoAfavor: 0.0,
            };

            if (
              this.transaction.id_doc_transaction_type == '40' &&
              this.CR_manual == false
            ) {
              let clienteCR =
                this.sharedDataService.getVariableValue('clienteCR');



              transaccionDTO.clienteCNR = {
                idCliente: clienteCR.idCliente,
                nombre: clienteCR.nombre,
                porcentajeDescuento: clienteCR.porcentajeDescuento,
                descripcionDescuento: '',
                email: clienteCR.email,
                actividadEconomica: clienteCR.actividadEconomica,
                nrc: clienteCR.nrc,
                actividadEconomicaCodigo: clienteCR.actividadEconomica,
                telefono: clienteCR.telefono,
                tipoDocumento: 'NIT',
                TipoPersona: 'PERSONA_NATURAL',
                numeroDocumento: clienteCR.numeroDocumento,
                nombreComercial: clienteCR.nombreComercial,
                nit: clienteCR.nit,
                pais: 'EL SALVADOR',
                departamento: this.departamentoSelected?.id_mh ?? this.departamentoSelected,
                departamentocod: this.departamentoSelected?.id_mh ?? this.departamentoSelected,
                municipio: this.municipioSelected?.id_mh ?? this.municipioSelected,
                municipiocod: this.municipioSelected?.id_mh ?? this.municipioSelected,
                direccion: clienteCR.direccion,

                colonia: clienteCR.colonia,
                calle: clienteCR.calle,
                apartamentoLocal: clienteCR.apartamentoLocal,
                numeroCasa: clienteCR.numeroCasa,

                esExtranjero: 0,
                esGranContribuyente: 0,
                esGobierno: false,
                esConsumidorFinal: true,
              };
            }

            transaccionDTO?.items?.forEach((item: any) => {
              const documentoAsociado: any =
                transaccionDTO?.documentosAsociados?.find((doc: any) =>
                  doc.items.some(
                    (docItem: any) => docItem.nombre === item.itemCNR.nombreItem
                  )
                );

              if (documentoAsociado) {
                const numeroDocumentoItem = documentoAsociado.nroDocumento;
                item.numeroDocumento = numeroDocumentoItem;
              }
            });



            this.api
              .doRequest(
                '/transacciones/crearTransaccion',
                transaccionDTO,
                'post'
              )
              .then((res: any) => {
                if (res.code == 200) {
                  this.campos = [new detalles()];

                  this.setview('index');
                  this.customers = res.data;
                  this.items = res.data.items;
                  this.customers = res.data.clientes;

                  this.dataSource = new MatTableDataSource<any>(
                    res.data.transactions
                  );
                  this.dataSource.paginator = this.paginator;

                  interface Transaction {
                    SIMPLE_SALE: string;
                    TEST_MODE: string;

                  }




















































                  this.setNOADatatransaction(res.data.noaprobadas);

                  this.transaction = new transaction();
                  this.campos = [new detalles()];
                  this.util.showSWAL(
                    'Transacción Realizada',
                    'la Transacción fue guardada con éxito',
                    'Aceptar'
                  ).then(() => {
                    this.loadCurrentPageVentas();
                  });
                } else {
                  this.util.showSWAL(
                    'transacion rechazada',
                    res.status,
                    res.data,
                    'error'
                  ).then(() => {
                    this.loadCurrentPageVentas();
                  });
                }
              });
          });
        } else {

          if (
            this.transaction.state == 1698 &&
            this.transaction.id_payment_cond == '457'
          ) {
            this.transaction.state = 1696;
          }



          let transaccionDTO = {
            clienteCNR: {
              nombre: 'Nombre del cliente',
              porcentajeDescuento: 10.0,
              descripcionDescuento: 'Descuento especial',
              email: 'h2@gmail.com',
            },
            items: [
              {
                itemCNR: {
                  nombreItem: 'Producto 1',
                  tipoItem: {
                    codigo: 1,
                    descripcion: 'Descripción del Producto 1',
                  },
                },
                cantidad: 2,
                precioUnitario: 10.0,
                ventaNosujeta: false,
                ventaExenta: false,
                ventaGravada: true,
              },
            ],
            formaDePago: {
              codigo: '01',
              descripcion: 'Billetes y monedas',
            },
            tipoDTE: {
              codigo: '01',
              descripcion: 'Factura',
            },
            notas: 'Notas adicionales',
          };



          return this.loadCurrentPageVentas();
        }

        break;
      case 2:

        if (
          this.transaction.state == 1698 &&
          this.transaction.id_payment_cond == '457' &&
          opt == 2
        ) {

          this.transaction.state = 1696;
          this.transaction.id_payment_status = '1687';

        }
        if (
          this.transaction.state == 1698 &&
          this.transaction.id_payment_cond == '458' &&
          opt == 2
        ) {


          this.transaction.state = 1697;
          this.transaction.id_payment_status = '1687';
        }
        if (!this.customer.NIT) {
          this.customer = data;
          this.storeCustomer(() => {
            this.api
              .doRequest(
                'transaction/update/' + this.transaction.id,
                this.transaction,
                'put'
              )
              .then((res: any) => {

                if (res.code == 200) {
                  this.setview('index');
                  this.customers = res.data;
                  this.items = res.data.items;
                  this.customers = res.data.clientes;
                  this.transaction = new transaction();
                  this.campos = [new detalles()];
                  this.util.showSWAL(res.status, res.message);



                  interface Transaction {
                    SIMPLE_SALE: string;
                    TEST_MODE: string;

                  }


                  interface FilteredTransactions {
                    filteredTransactions1: Transaction[];
                    filteredTransactions2: Transaction[];
                    filteredTransactions3: Transaction[];
                    filteredTransactions4: Transaction[];
                  }

                  function getKey(
                    transaction: Transaction,
                    testModeComparison: '==' | '!='
                  ): keyof FilteredTransactions {
                    let testModeMatch =
                      testModeComparison == '=='
                        ? transaction.TEST_MODE == '1'
                        : transaction.TEST_MODE != '1';
                    return `filteredTransactions${(transaction.SIMPLE_SALE == '1' ? 1 : 2) +
                      (testModeMatch ? 0 : 2)
                      }` as keyof FilteredTransactions;
                  }

                  let filteredTransactions = (
                    res.data.transactions as Transaction[]
                  ).reduce<FilteredTransactions>(
                    (acc, transaction) => {
                      let key = getKey(
                        transaction,
                        res.data.users.TEST_MODE == '1' ? '==' : '!='
                      );
                      acc[key].push(transaction);
                      return acc;
                    },
                    {
                      filteredTransactions1: [],
                      filteredTransactions2: [],
                      filteredTransactions3: [],
                      filteredTransactions4: [],
                    }
                  );

                  if (this.opcion == '3') {
                    this.setTransactions(
                      filteredTransactions.filteredTransactions1
                    );
                  } else {
                    this.setTransactions(
                      filteredTransactions.filteredTransactions2
                    );
                  }

                  this.setNOADatatransaction(res.data.noaprobadas);

                } else {
                  this.util.showSWAL(res.status, res.data);
                }
              });
          });
        } else {
          this.api
            .doRequest(
              'transaction/update/' + this.transaction.id,
              this.transaction,
              'put'
            )
            .then((res: any) => {

              if (res.code == 200) {
                this.setview('index');
                this.customer = new Cliente();
                this.customers = res.data;
                this.items = res.data.items;
                this.customers = res.data.clientes;
                this.transaction = new transaction();
                this.campos = [new detalles()];
                this.util.showSWAL(res.status, res.message);
                this.setTransactions(res.data.transactions);

                interface Transaction {
                  SIMPLE_SALE: string;
                  TEST_MODE: string;

                }


                interface FilteredTransactions {
                  filteredTransactions1: Transaction[];
                  filteredTransactions2: Transaction[];
                  filteredTransactions3: Transaction[];
                  filteredTransactions4: Transaction[];
                }

                function getKey(
                  transaction: Transaction,
                  testModeComparison: '==' | '!='
                ): keyof FilteredTransactions {
                  let testModeMatch =
                    testModeComparison == '=='
                      ? transaction.TEST_MODE == '1'
                      : transaction.TEST_MODE != '1';
                  return `filteredTransactions${(transaction.SIMPLE_SALE == '1' ? 1 : 2) +
                    (testModeMatch ? 0 : 2)
                    }` as keyof FilteredTransactions;
                }

                let filteredTransactions = (
                  res.data.transactions as Transaction[]
                ).reduce<FilteredTransactions>(
                  (acc, transaction) => {
                    let key = getKey(
                      transaction,
                      res.data.users.TEST_MODE == '1' ? '==' : '!='
                    );
                    acc[key].push(transaction);
                    return acc;
                  },
                  {
                    filteredTransactions1: [],
                    filteredTransactions2: [],
                    filteredTransactions3: [],
                    filteredTransactions4: [],
                  }
                );

                if (this.opcion == '3') {
                  this.setTransactions(
                    filteredTransactions.filteredTransactions1
                  );
                } else {
                  this.setTransactions(
                    filteredTransactions.filteredTransactions2
                  );
                }

                this.setNOADatatransaction(res.data.noaprobadas);

              } else {
                this.util.showSWAL(res.status, res.data);
              }
            });
        }
        break;

      case 3:

        this.api
          .doRequest('transaction/storeNotes', this.transaction, 'post')
          .then((res: any) => {
            if (res.code == 200) {
              this.setview('index');
              this.customer = new Cliente();
              this.customers = res.data;
              this.items = res.data.items;
              this.customers = res.data.clientes;
              this.setTransactions(res);
              this.transaction = new transaction();
              this.campos = [new detalles()];
              this.util.showSWAL(res.status, res.message);
            } else {
              this.util.showSWAL(res.status, res.data);
            }
          });
        break;
      case 4:
        if (!this.customer.NIT) {
          this.customer = data;
          this.storeCustomer(() => {
            if (
              this.transaction.state == 1698 &&
              this.transaction.id_payment_cond == '457'
            ) {
              this.transaction.state = 1966;
            }


            this.api
              .doRequest('transaction/storePaintBill', this.transaction, 'post')
              .then((res: any) => {
                if (res.code == 200) {

                  this.setview('index');
                  this.customers = res.data;
                  this.items = res.data.items;
                  this.customers = res.data.clientes;

                  this.setTransactions(res.data.transactions);
                  this.setNOADatatransaction(res.data.noaprobadas);

                  this.transaction = new transaction();
                  this.campos = [new detalles()];
                  this.util.showSWAL(
                    'Transacción Realizada',
                    res.status,
                    res.message
                  );
                } else {
                  this.util.showSWAL(
                    'transacion rechazada',
                    res.status,
                    res.data,
                    'error'
                  );
                }
              });
          });
        } else {

          if (
            this.transaction.state == 1698 &&
            this.transaction.id_payment_cond == '457'
          ) {
            this.transaction.state = 1966;
          }


          this.api
            .doRequest('transaction/storePaintBill', this.transaction, 'post')
            .then((res: any) => {
              if (res.code == 200) {
                this.setview('index');
                this.customer = new Cliente();
                this.customers = res.data;
                this.items = res.data.items;
                this.customers = res.data.clientes;
                this.setTransactions(res.data.transactions);

                interface Transaction {
                  SIMPLE_SALE: string;
                  TEST_MODE: string;

                }


                interface FilteredTransactions {
                  filteredTransactions1: Transaction[];
                  filteredTransactions2: Transaction[];
                  filteredTransactions3: Transaction[];
                  filteredTransactions4: Transaction[];
                }

                function getKey(
                  transaction: Transaction,
                  testModeComparison: '==' | '!='
                ): keyof FilteredTransactions {
                  let testModeMatch =
                    testModeComparison == '=='
                      ? transaction.TEST_MODE == '1'
                      : transaction.TEST_MODE != '1';
                  return `filteredTransactions${(transaction.SIMPLE_SALE == '1' ? 1 : 2) +
                    (testModeMatch ? 0 : 2)
                    }` as keyof FilteredTransactions;
                }

                let filteredTransactions = (
                  res.data.transactions as Transaction[]
                ).reduce<FilteredTransactions>(
                  (acc, transaction) => {
                    let key = getKey(
                      transaction,
                      res.data.users.TEST_MODE == '1' ? '==' : '!='
                    );
                    acc[key].push(transaction);
                    return acc;
                  },
                  {
                    filteredTransactions1: [],
                    filteredTransactions2: [],
                    filteredTransactions3: [],
                    filteredTransactions4: [],
                  }
                );

                if (this.opcion == '3') {
                  this.setTransactions(
                    filteredTransactions.filteredTransactions1
                  );
                } else {
                  this.setTransactions(
                    filteredTransactions.filteredTransactions2
                  );
                }

                this.setNOADatatransaction(res.data.noaprobadas);
                this.transaction = new transaction();
                this.campos = [new detalles()];
                this.util.showSWAL(
                  'Transacción Realizada',
                  res.status,
                  res.message
                );
              } else {
                this.util.showSWAL(
                  'transacion rechazada',
                  res.status,
                  res.data,
                  'error'
                );
              }
            });
        }
        break;
    }
    return;
  }
  compareTransactions(id: any) {
    let transa = this.transactions_array.filter((item: any) => {
      return item.id == id;
    })[0];

    if (this.campos.length != transa.items_transactions.length) {
      return true;
    } else {
      return false;
    }
  }


  resetOptions() {

    this.filtervalues('', 2);


  }

  setTransactions(transactions: any) {


    this.transactions = [];
    this.sortedData = [];
    this.dataTransaction = new MatTableDataSource<any>();

    transactions.forEach((tra: any) => {
      let val = new dataTransaction();
      val.customer = tra.cliente.nombre;
      val.date = moment(tra.createdAt).format('DD-MM-YYYY HH:mm');
      val.pay_type = 'efectivo';
      val.total = tra.totalTransaccion;












      val.sale = tra.ID_TYPE_TRANSACTIONS == '1677' ? true : false;
      this.transactions.push(val);
    });


    this.dataTransaction = new MatTableDataSource<any>(this.transactions);

    this.dataTransaction.paginator = this.paginator;
    this.sortedData = this.transactions;

    this.transactions_array = transactions;
  }

  setNOADatatransaction(data: any) {
    this.NOAtransactions = [];
    this.sorteNOAdData = [];
    this.dataNOATransaction = new MatTableDataSource<any>();
    if (data) {
      data.forEach((tra: any) => {
        let val = new dataTransaction();
        val.customer = tra.customers.NAME_CUSTOMER;
        val.date = moment(tra.CREATED_AT).format('DD-MM-YYYY HH:mm');
        val.pay_type = 'Credito';
        val.total = tra.TOTAL_TRANSACTIONS.toFixed(2);
        val.type_transaction = tra.DOC_TYPE;
        val.pay_condition = tra.PAY_COND;
        val.id = tra.ID_TRANSACTIONS;
        val.generation_code = tra.VALID_STAMP;
        val.deuda = this.getTotalamount(tra.ID_CUSTUMER);
        val.customerdata = tra.customers;
        val.state = tra.STATE;
        this.NOAtransactions.push(val);
      });
    }
    this.dataNOATransaction = new MatTableDataSource<any>(this.NOAtransactions);
    this.dataNOATransaction.paginator = this.NOApaginator;
    this.sorteNOAdData = this.NOAtransactions;
  }

  getTotalamount(id: any) {
    let deuda: number = 0;
    let customs: any = this.customers.filter((res: any) => {
      return res.id == id;
    })[0];
    try {
      customs.record_credit.forEach((ele: any) => {
        deuda += parseFloat(ele.TOTAL_TRANSACTIONS);
      });
    } catch (e) {
      return deuda;
    }
    this.transaction.monto_maximo_credit = customs.monto_maximo_credit - deuda;
    return deuda;
  }

  storeCustomer(callback: Function) {
    if (this.transaction.id_customer === 'add') {

      if (!this.registerForm.get('giro_id')?.value) {
        this.registerForm.get('giro_name')?.setValue('');
        this.util.showSWAL(
          'Actividad Económica Incorrecta',
          'Debe Escribir una actividad Económica válida',
          'OK',
          'warning'
        );
        return;
      }
      if (
        !this.registerForm.get('type_doc_id')?.value &&
        (this.registerForm.value['is_extranjero'] ||
          this.registerForm.value['is_final_consumer'])
      ) {
        this.registerForm.get('type_doc_id')?.setValue('');
        this.util.showSWAL(
          'Tipo de documento incorrecto ',
          'Debe Escribir una tipo de documento valido',
          'OK',
          'warning'
        );
        return;
      }




















      callback();
    } else {
      callback();
    }
  }

  closeModal() {
    this.dialog.closeAll();
  }

  aplica_retencion: string = '';
  contribuyente: string = '';

  codigo_giro = '';
  nombre_giro = '';

  filtervalues(event: any, opt: any = 1, index?: number) {

    this.filteredOptions.productos = this.items;


    const camposConNumeroDocumento = this.campos.map((campo) => {
      const item = this.filteredOptions.productos.find(
        (item: { nombre: string }) => item.nombre === campo.item
      );
      return { ...campo, numeroDocumento: item ? item.nroDocumento : null };
    });



    switch (opt) {
      case 1:



        const searchTerm = event.toLowerCase();


        let value = this.customers.filter((item) => {


          if (item.name && typeof item.name === 'string') {





            return item.nombreConcatenado.toLowerCase().includes(searchTerm);
          }
          return false;
        });


        this.transaction.id_customer = '';
        this.filteredOptions.clientes = value;




        break;

      case 2:
        if (
          this.transaction.id_doc_transaction_type == '36' ||
          this.transaction.id_doc_transaction_type == '38' ||
          this.transaction.id_doc_transaction_type == '39'
        ) {
          this.filteredOptions.productos = this.filteredOptions.productos.filter(
            (producto: any) => producto.tipo === 'G'
          );
        } else {
          this.filteredOptions.productos = this.items_iniciales;
        }

        if (this.transaction.id_doc_transaction_type != '44') {
          const items2 = this.filteredOptions.productos;


          event = event.toLowerCase();

          let value2 = items2.filter((item: any) => {
            let name = item.codigo_producto + ' ' + item.nombre;


            if (
              name.toLowerCase().includes(event) ||
              name.toLowerCase().includes(event.toLocaleString())
            ) {
              return item;
            }
          });

          this.filteredOptions.productos = value2;
        }
        break;
      case 3:
        let value3 = this.catalogos.tipoIdentificacion.filter((item: any) => {
          if (item.name.toLowerCase().includes(event)) {
            return item;
          }
        });
        this.filteredOptions.tipoIdentificacion = value3;
        break;
      case 4:
        let value4 = this.catalogos.formaDePago.filter((item: any) => {
          if (item.name && item.name.toLowerCase().includes(event)) {
            return item;
          }
        });
        this.filteredOptions.formaDePago = value4;
        break;
      case 5:
        if (event) {




          const searchTerm = event.toLowerCase();


          let value5 = this.catalogos.actividad_economica.filter(
            (item: any) => {

              if (item.name && typeof item.name === 'string') {


                this.codigo_giro = item.id_mh;
                this.nombre_giro = item.name;
                return item.name.toLowerCase().includes(searchTerm);
              }
              return false;
            }
          );


          this.filteredOptions.actividad_economica = value5;

        }

        break;
      case 6:
        let value6 = this.catalogos.impuestos.filter((item: any) => {
          return item.name.toLowerCase().includes(event);
        });
        this.filteredOptions.impuestos = value6;
        break;
    }


  }

  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    input.value = this.formatInput(input.value);
  }

  formatInput(value: string): string {
    value = value.replace(/-/g, '');
    if (value.length > 8) {
      value = value.slice(0, 8) + '-' + value.slice(8);
    }
    if (value.length > 13) {
      value = value.slice(0, 13) + '-' + value.slice(13);
    }
    if (value.length > 18) {
      value = value.slice(0, 18) + '-' + value.slice(18);
    }
    if (value.length > 23) {
      value = value.slice(0, 23) + '-' + value.slice(23);
    }
    return value;
  }

  onInputDTE(event: Event) {
    const input = event.target as HTMLInputElement;
    input.value = this.formatInputDTE(input.value);
  }

  formatInputDTE(value: string): string {
    value = value.replace(/-/g, '');
    if (!value.startsWith('DTE')) {
      value = 'DTE' + value;
    }
    if (value.length > 3) {
      value = value.slice(0, 3) + '-' + value.slice(3);
    }
    if (value.length > 6) {
      value = value.slice(0, 6) + '-' + value.slice(6);
    }
    if (value.length > 16) {
      value = value.slice(0, 16) + '-' + value.slice(16);
    }
    return value;
  }

  compareObjects(o1: any, o2: any): boolean {
    return parseInt(o1) === parseInt(o2);
  }


  seleccionFacturaCredito(id?: any) {


    this.clearSelection();


    this.filteredOptions.clientes = this.clientes;


    if (id != 35) {
      this.filteredOptions.clientes = this.filteredOptions.clientes.filter(
        (elemento: { id: any }) => elemento.id !== 'add2'
      );
    } else {
      this.filteredOptions.clientes = this.clientes;
    }

    const temp = this.transaction.id_customer;
    if (id == 44) {

      this.transaction.id_customer = 'add2';
      this.opcion = '3';
    } else {
      this.transaction.id_customer = '';
    }

    this.calculos.SUMAS = 0.0;
    this.calculos.Gravadas = 0.0;
    this.calculos.Sujetas = 0.0;
    this.calculos.Exentas = 0.0;
    this.calculos.DESCUENTO = 0.0;
    this.calculos.IMPUESTOS = 0.0;
    this.calculos.SUBTOTAL = 0.0;
    this.calculos.IVA = 0.0;
    this.calculos.PERCEPCION = 0.0;
    this.calculos.total = 0.0;

    let arr = [35, 40, 44];


    let newarrr = arr.filter((value: any) => value === id);




    this.isFactura = newarrr.length > 0;
    this.isCreditoFiscal = newarrr.length == 0;

    this.campos = [new detalles()];

    /**
    Basado en Factura, Factura de exportación y el resto de tipos
    se muestra en la sección de impuestos el registro del catálogo cuando sea el resto de tipos!
    */
    if (this.isFactura) {
      this.impuestos_array = [];
    }

    if (
      this.isCreditoFiscal &&
      this.transaction.id_doc_transaction_type != '43'
    ) {
      this.impuestos_array = [
        {
          ID_CATALOGO: 408,
          VALOR: 'IMPUESTO AL VALOR AGREGADO 13%',
          CAT_PADRE: 'CAT-015',
          ID_MH: '20',
          ALTERNO: '13%',
        },
      ];
    }

    /**
    Filtrando Items para mostrar únicamente los que son Tipo gravado.
    */
    if (
      this.transaction.id_doc_transaction_type == '36' ||
      this.transaction.id_doc_transaction_type == '38' ||
      this.transaction.id_doc_transaction_type == '39'
    ) {
      this.filteredOptions.productos = this.filteredOptions.productos.filter(
        (producto: any) => producto.tipo === 'G'
      );
    } else {
      this.filteredOptions.productos = this.items_iniciales;
    }
  }

  abrirModalAnulacion(element: any) {

    this.anularElemento = element;
    this.sharedDataService.setVariable('anularElemento', element);
    const modalData = this.util.openModal({
      title: 'Invalidación de DTE',
      botonAceptar: 'Agregar',
      componentToLoad: ModalAnulacionComponent,
      callerComponent: this,
      data: null,
    });

    const anular = document.getElementById('anular');
    if (anular) {

      const click$ = fromEvent(anular, 'click');
      click$.pipe().subscribe(() => {
        this.confirmarAnulacion(element);
      });
    }
  }

  confirmarAnulacion(element: any) {


    let payload = {
      tipoAnulacion: this.motivoSelect.value,
      motivoAnulacion: this.descripcionTextarea.nativeElement.value,
    };
    if (this.validarAnulacionDET(payload)) {

      this.api
        .doRequest(
          'api/v1/dte/anularDte/' + element.codigoGeneracion,
          payload,
          'post'
        )
        .then((res: any) => {
          if (res.code != 200) {
            return this.util.showSWAL(
              'Error no se invalido el documento',
              res.data.mensaje,
              'error'
            );
          }

          let tra = res.data;
          let val = new dataTransaction();


          val.customer = tra.customers.NAME_CUSTOMER;
          val.date = moment(tra.CREATED_AT).format('DD-MM-YYYY HH:mm');
          val.pay_type = 'efectivo';
          val.total = tra.TOTAL_TRANSACTIONS.toFixed(2);
          val.type_transaction = res.data.tipodocumento[0].VALOR;
          val.id_type_transaction = tra.ID_DOC_TYPE;
          val.pay_condition = res.data.condpago[0].VALOR;
          val.id = tra.ID_TRANSACTIONS;
          val.generation_code = tra.VALID_STAMP;
          val.id_padre = tra.id_padre;
          val.transaction_padre = tra.transaccion_padre;
          val.deuda = this.getTotalamount(tra.ID_CUSTUMER);
          val.days_pass = tra.DIFF_DAYS;
          val.invalid_stamp = tra.INVALID_STAMP;
          val.state = tra.STATE;



          this.dataTransaction = new MatTableDataSource<any>(
            this.dataTransaction.data
          );
          this.table.renderRows();
          this.closeModal();
          return this.util.showSWAL(
            'Documento invalidado',
            'El Documento ha sido invalidado exitosamente',
            'Aceptar',
            'success'
          );
        });
    } else {

    }
  }

  validarAnulacionDET(payload: any) {

    if (payload.motivoAnulacion == undefined && payload.tipoAnulacion == '') {
      this.util.showSWAL(
        'Todos los campos son requeridos',
        'Por favor llenar todos los campos',
        'error',
        'warning'
      );
      return false;
    }

    if (payload.tipoAnulacion == undefined) {
      this.util.showSWAL(
        'Motivo es requerido',
        'Por favor seleccione el motivo',
        'error',
        'warning'
      );
      return false;
    }

    if (payload.motivoAnulacion == '') {
      this.util.showSWAL(
        'Descripción es requerido',
        'Por favor llene la descripción del motivo',
        'error',
        'warning'
      );
      return false;
    }

    if (payload.motivoAnulacion.length < 5) {
      this.util.showSWAL(
        'Mínimo de caracteres',
        'Minimo de caracteres es 5',
        'error',
        'warning'
      );
      return false;
    }
    return true;
  }

  selectChange(event: any, opt = 1, i: number, index: number = 0) {






    this.clienteDisable = true;
    let id = event.value || event.option.value;


    switch (opt) {
      case 1:
        let val: any = this.items.filter((item) => {
          if (item.codigo_producto === id) {


            return item;
          }
        })[0];
        if (id == 'add') {
          this.openModal('product');
        } else {

          this.campos[i].item_desc = val.descripcion;
          this.campos[i].item = val.nombre;
          this.campos[i].nombre = val.nombre;
          this.campos[i].name = val.nombre;
          this.campos[i].item_id = id;
          this.campos[i].precio = val.precio;
          this.campos[i].iva = val.iva;
          this.campos[i].precioiva = val.total;
          this.campos[i].precioiva2 =
            val.tipo == 'G' ? (val.precio ?? 0) / 1.13 : val.precio;
          this.campos[i].codigo = val.codigo_producto;
          this.campos[i].codigo_producto = val.codigo_producto;
          this.campos[i].codigo_ingreso = val.codigo_ingreso;
          this.campos[i].clasificacion = val.clasificacion;
          this.campos[i].existencia_producto = val.existencia;
          this.campos[i].tipo = val.tipo;
          this.campos[i].editable = val.editable;
          this.campos[i].cantidad = 1;
          this.campos[i].numeroDocumento = val.numeroDocumento;
          this.campos[i].isgravada = val.tipo == 'G' ? true : false;
          this.campos[i].isexenta = val.tipo == 'E' ? true : false;
          this.campos[i].issujeta = val.tipo == 'NS' ? true : false;
          this.campos[i].subtotal =
            this.campos[i].cantidad *
            (this.isCreditoFiscal
              ? this.campos[i].precio
              : this.campos[i].precioiva ?? this.campos[i].precioiva2);
          this.subtotalChange(event, i, true);
        }

        break;
      case 2:
        this.isBigTaxPayerSelected = false;
        this.isLeadCustomer = false;

        if (id == 'CONSUMIDOR FINAL') {

        }
        if (id === 'add') {
          this.customer.id_branch = id;
        } else {



          let filteredCustomers = this.customers.filter(
            (item) => item.name === id
          );



          let val2: any = filteredCustomers.filter((item) => {
            if (item.name === id) {
              this.es_ConsumidorFinal = item.es_Contribuyente;
              return item;
            }
          })[0];




          this.customer = val2.id;
          this.cliente = val2;
          this.llenarcampos();

          if (val2.descuento) {
            this.x100_descuento_aplicar = val2.descuento;

          } else {
            this.x100_descuento_aplicar = 0;
          }

          if (val2.es_contribuyente == 'N') {
            this.es_ConsumidorFinal = true;
            val2.esConsumidorFinal = true;
          } else {
            this.es_ConsumidorFinal = false;
            val2.esConsumidorFinal = false;
          }
          this.transaction.id_customer = val2.id;
        }
        break;
      case 3:

        let val3 = this.catalogos.formaDePago.filter((item: any) => {


          if (item.name.includes(id)) {
            return item;
          }
        })[0];
        this.transaction.id_payment_type = val3.id_catalogo;
        break;
      case 4:
        let value6 = this.catalogos.impuestos.filter((item: any) => {
          return item.id_catalogo == id;
        })[0];
        if (value6.ALTERNO.includes('%')) {
          let percent = value6.ALTERNO / 100;
          value6['subtotal'] = this.calculos.SUBTOTAL * percent;
        } else {
          value6['subtotal'] = value6.ALTERNO;
        }
        this.impuestos_array.push(value6);
        this.subtotalChange('descuentos', 1);
        this.impuestotext = '';
        break;
      case 5:
        let valprov: any = this.proveedores.filter((item: any) => {
          if (item.id === parseInt(id)) {
            return item;
          }
        })[0];
        this.customer = valprov;
        this.inputChange = valprov.name;
        this.transaction.id_customer = valprov.id;
        this.customer.others = valprov.otros;
        valprov.bigtaxpayer
          ? (this.isBigTaxPayerSelected = true)
          : (this.isBigTaxPayerSelected = false);
        valprov.leadcustomer
          ? (this.isLeadCustomer = true)
          : (this.isLeadCustomer = false);

        break;
    }
    this.calculoTotales();

    this.filteredOptions.productos = this.items_iniciales;
  }

  llenarcampos() {


    this.registerForm.patchValue({
      name: this.cliente.name,
      comercial_name: this.cliente.name,
      email: this.cliente.correo,
      address: this.cliente.direccion,
      DUI: this.cliente.dui,
      NIT: this.cliente.nit,
      NRC: this.cliente.nrc,
      phone: this.cliente.telefono,
      giro_name: (this.cliente.actividad_nombre ?? '').toUpperCase(),
      esConsumidorfinal: this.cliente.es_contribuyente == 'S' ? true : false,
      departamento: this.cliente?.departamento_codigo.toString(),
      municipio: this.cliente?.municipio_codigo.toString()
    });

    this.departamentoSelected = this.cliente?.departamento_codigo.toString()
    this.municipioSelected = this.cliente?.municipio_codigo.toString()


    this.registerForm.controls['departamento']?.disable({
      onlySelf: true
    })
    this.registerForm.controls['municipio']?.disable({
      onlySelf: true
    })

    this.contribuyente = this.cliente.contribuyente;
    this.aplica_retencion = this.cliente.aplica_retencion;
  }
  calculoTotales() {



    this.calculos = {
      SUMAS: 0.0,
      Sujetas: 0.0,
      Gravadas: 0.0,
      Exentas: 0.0,
      DESCUENTO: 0.0,
      IMPUESTOS: 0.0,
      SUBTOTAL: 0.0,
      IVA: 0.0,
      total: 0.0,
      PERCEPCION: 0.0,
    };
    let cliente = this.cliente;
    this.calculos.RENTA = 0.0;

    this.campos.forEach((el, index) => {
      if (el.item != '') {


        el.isgravada = el.tipo == 'G' ? true : false;
        el.isexenta = el.tipo == 'E' ? true : false;
        el.issujeta = el.tipo == 'NS' ? true : false;

        if (this.cliente?.descuento == '100') {
          

          el.issujeta = true;
          el.isgravada = false;
          el.isexenta = false;
          el.tipo = 'NS';
        }

        if (
          this.transaction.id_doc_transaction_type == '44' ||
          this.transaction.id_doc_transaction_type == '40'
        ) {
          if (!el.issujeta) {
            el.isgravada = true;
            el.tipo = 'G';

          }
        }
        if (this.transaction.id_doc_transaction_type == '44') {
          this.calculos.iva = 0;
          this.calculos.IMPUESTOS = 0;
        } else {
          if (this.transaction.id_doc_transaction_type == '40') {
            this.calculos.iva = el.tipo == 'G' ? el.precio * 0.13 : 0;
            this.calculos.IMPUESTOS = 0;
          } else {


            this.calculos.iva =
              el.tipo == 'G'
                ? el.cantidad * el.precio * 0.13 -
                (cliente || (cliente != '' && cliente != null)
                  ? (cliente.descuento / 100) * el.precio * 0.13
                  : 0)
                : 0;
          }
        }

        this.calculos.IVA = this.calculos.IVA ?? 0.0;



        if (el.tipo == 'G') {
          this.calculos.SUMAS +=
            el.cantidad * (this.isCreditoFiscal ? el.precio : el.precio * 1.13);
        } else {
          this.calculos.SUMAS += el.cantidad * el.precio;
        }








        this.calculos.SUBTOTAL = this.calculos.SUMAS;



        if (el.tipo == 'E') {
          this.calculos.Exentas += el.subtotal;
          this.calculos.SUMAS += el.cantidad * el.precio;

        }
        if (el.tipo == 'NS') {
          this.calculos.Sujetas += el.subtotal;
          this.calculos.SUMAS += el.cantidad * el.precio;
        }
        if (el.tipo == 'G') {
          this.calculos.Gravadas += el.subtotal;
          this.calculos.SUMAS += el.cantidad * el.precio;
        }






        this.calculos.total = 0;

        if (
          el.isgravada &&
          this.isCreditoFiscal &&
          this.transaction.id_doc_transaction_type != '43'
        ) {


          this.calculos.IVA =
            this.calculos.Gravadas -
            this.calculos.Gravadas *
            (cliente || (cliente != '' && cliente != null)
              ? this.cliente?.descuento / 100
              : 0) -
            (this.calculos.Gravadas -
              this.calculos.Gravadas *
              (cliente || (cliente != '' && cliente != null)
                ? this.cliente?.descuento / 100
                : 0)) /
            1.13;
        }

        // if (el.issujeta) {
        //   this.calculos.Sujetas = (this.calculos.Gravadas + this.calculos.Exentas).toFixed(4);
        //   this.calculos.Gravadas = 0.00;
        //   this.calculos.Exentas = 0.00;
        // }

        this.calculos.SUBTOTAL =
          this.calculos.Gravadas +
          this.calculos.Sujetas +
          this.calculos.Exentas;


        if (this.x100_descuento_aplicar) {
          this.calculos.DESCUENTO =
            this.calculos.SUBTOTAL -
            this.calculos.SUBTOTAL * (1 - this.x100_descuento_aplicar / 100);

        }


        this.ventasGravadasNetas =
          this.calculos.Gravadas -
          this.calculos.Gravadas * (this.x100_descuento_aplicar / 100);



        this.calculos.SUBTOTAL =
          this.calculos.Gravadas +
          this.calculos.Sujetas +
          this.calculos.Exentas -
          this.calculos.DESCUENTO;




        if (this.calculos.DESCUENTO == (this.calculos.Gravadas +
          this.calculos.Sujetas +
          this.calculos.Exentas)) {
          this.calculos.Gravadas = 0.00;
          this.calculos.Sujetas = this.calculos.DESCUENTO;
          this.calculos.Exentas = 0.00;
        }





        if (this.cliente && Object.keys(this.cliente).length !== 0) {


          const isTransactionTypeValid =
            this.transaction.id_doc_transaction_type == '35' ||
            this.transaction.id_doc_transaction_type == '36' ||
            this.transaction.id_doc_transaction_type == '40';

          const isRetentionApplicable =
            this.cliente.aplica_retencion === 'S' &&
            this.ventasGravadasNetas >= 100;



          if (isTransactionTypeValid && isRetentionApplicable) {






            if (
              this.transaction.id_doc_transaction_type == '35' &&
              this.ventasGravadasNetas / 1.13 >= 113
            ) {
              this.calculos.PERCEPCION =
                (this.ventasGravadasNetas / 1.13) * -this.IVARetx100;
            } else {
              this.calculos.PERCEPCION =
                this.ventasGravadasNetas * -this.IVARetx100;
            }


          } else {
            this.calculos.PERCEPCION = 0.0;
          }
        } else {




          if (this.transaction.id_doc_transaction_type == '44') {




            if (el.renta1) {



              if (el.renta13) {
                this.calculos.RENTA =
                  this.calculos.RENTA + (this.calculos.SUBTOTAL / 1.13) * 0.1;
              } else {
                this.calculos.RENTA =
                  this.calculos.RENTA + this.calculos.SUBTOTAL * 0.1;
              }
            } else {
              this.calculos.RENTA = 0.0;
            }
            if (el.renta13) {




              this.calculos.PERCEPCION =
                this.calculos.PERCEPCION + (el.precioiva / 1.13) * 0.13;

            } else {
              this.calculos.PERCEPCION = 0.0;
            }

          }













        }
      }















    });
    this.impuestos_array.map((res: any) => {
      if (res.ALTERNO.includes('%')) {
        let percent = parseFloat(res.ALTERNO) / 100;
        res['subtotal'] = this.calculos.Gravadas * percent;
      } else {
        res['subtotal'] = parseFloat(res.ALTERNO);
      }
      this.calculos.IMPUESTOS += parseFloat(res['subtotal']);

      this.calculos.IMPUESTOS =
        this.calculos.IMPUESTOS -
        this.calculos.IMPUESTOS *
        (this.cliente || (this.cliente != '' && this.cliente != null)
          ? this.cliente?.descuento / 100
          : 0);
      this.calculos.IMPUESTOS = this.calculos.IMPUESTOS ?? 0.0;
    });























    this.calculos.IMPUESTOS = this.calculos.IMPUESTOS ?? 0.0;
    this.calculos.total =
      this.calculos.SUBTOTAL +
      this.calculos.IMPUESTOS -
      this.calculos.RENTA -
      Math.abs(this.calculos.PERCEPCION);















  }

  totalItemsCR: number = 0.00;
  totalIvaRetenidoCR: number = 0.00;


  calcularTotalItemsCR(): void {
    this.totalItemsCR = this.docAsociado.reduce((total, item) => {

      const monto = typeof item.monto === 'string' ? parseFloat(item.monto) : item.monto;
      return total + (isNaN(monto) ? 0 : monto);
    }, 0);
    this.totalIvaRetenidoCR = this.docAsociado.reduce((total, item) => {

      const ivaRetenido = typeof item.ivaRetenido === 'string' ? parseFloat(item.ivaRetenido) : item.ivaRetenido;
      return total + (isNaN(ivaRetenido) ? 0 : ivaRetenido);
    }, 0);
  }


  subtotalChangeDocAsociado(event: any, i: number): void {

    this.docAsociado[i].ivaRetenido = parseFloat((event * 0.01).toFixed(2));


    this.docAsociado[i].monto = typeof event === 'string' ? parseFloat(event) : event;


    this.calcularTotalItemsCR();





  }


  subtotalChange(event: any, i: number, uniprecio: boolean = false) {









    if (event != 'descuentos') {
      let unitprecio = 0;
      if (uniprecio && this.isCreditoFiscal) {
        this.campos[i].precioiva2 =
          this.campos[i].precio - this.campos[i].precioiva;
      }

      unitprecio = this.isCreditoFiscal
        ? this.campos[i].precio
        : this.campos[i].precioiva;

      if (this.transaction.id_doc_transaction_type == '40') {
        unitprecio = this.campos[i].precio;
      }

      //*Se mejor la legibilidad y calidad del codigo
      let precioBase = this.campos[i].precio * this.campos[i].cantidad;

      if (!this.isCreditoFiscal && !this.campos[i].isexenta) {
          precioBase = this.campos[i].precioiva * this.campos[i].cantidad;
      }

      this.campos[i].subtotal = precioBase;

  

    }


    this.calculoTotales();
  }

  openModal(modal: string) {

    this.option = modal;

    this.new_item = new item();
















  }

  changeChk(event: any, opt: number = 1, index: number = 0) {
    if (event) {
    }
    switch (opt) {
      case 1:

        this.registerForm.controls['is_juridico'].setValue(event);
        this.registerForm.controls['is_final_consumer'].setValue(!event);
        this.registerForm.controls['is_natural_person'].setValue(!event);
        this.isLegalPersonSelected = event;
        this.isNaturalPersonSelected = !event;
        this.isConsumidorFinalSelected = !event;
        this.isPersonSelected();
        break;
      case 2:
        this.registerForm.controls['is_extranjero'].setValue(event);
        this.isForeignCustomerSelected = event;
        break;
      case 3:
        this.registerForm.controls['is_gran_contribuyente'].setValue(event);
        if (event) {
          this.isBigTaxPayerSelected = true;
        } else {
          this.isBigTaxPayerSelected = false;
        }
        this.calculoTotales();
        break;
      case 4:
        if (event) {
          this.registerForm.controls['subject_type'].setValue(event);
          this.campos[index].isgravada = false;
          this.campos[index].isexenta = false;
          this.campos[index].issujeta = false;
          if (event == 1695) {
            this.campos[index].isgravada = true;
            this.subtotalChange(event, index);
          }
          if (event == 1693) {
            this.campos[index].isexenta = true;
            this.subtotalChange(event, index);
          }
          if (event == 1694) {
            this.campos[index].issujeta = true;
            this.subtotalChange(event, index);
          }
        }
        break;
      case 5:

        this.registerForm.controls['is_natural_person'].setValue(event);
        this.registerForm.controls['is_juridico'].setValue(!event);
        this.registerForm.controls['is_final_consumer'].setValue(!event);

        this.isNaturalPersonSelected = event;
        this.isConsumidorFinalSelected = !event;
        this.isLegalPersonSelected = !event;
        this.isPersonSelected();
        break;
      case 6:

        this.registerForm.controls['is_juridico'].setValue(!event);
        this.registerForm.controls['is_natural_person'].setValue(!event);

        this.registerForm.controls['is_gran_contribuyente'].setValue(!event);
        this.registerForm.controls['is_final_consumer'].setValue(event);
        this.isConsumidorFinalSelected = event;
        this.isLegalPersonSelected = !event;
        this.isNaturalPersonSelected = !event;
        this.isPersonSelected();
        break;
      case 7:
        this.registerForm.controls['lead_customer'].setValue(event);
        this.isLeadCustomer = event;
        break;
    }
  }

  isPersonSelected() {
    if (this.isNaturalPersonSelected) {

      this.isNaturalPersonSelected = true;
      this.isLegalPersonSelected = false;
      this.isConsumidorFinalSelected = false;
    } else if (this.isLegalPersonSelected) {

      this.isNaturalPersonSelected = false;
      this.isLegalPersonSelected = true;
      this.isConsumidorFinalSelected = false;
    } else if (this.isConsumidorFinalSelected) {

      this.isNaturalPersonSelected = false;
      this.isLegalPersonSelected = false;
      this.isConsumidorFinalSelected = true;




    }
    this.registerForm.controls['is_juridico'].setValue(
      this.isLegalPersonSelected
    );
    this.registerForm.controls['is_final_consumer'].setValue(
      this.isConsumidorFinalSelected
    );
    this.registerForm.controls['is_natural_person'].setValue(
      this.isNaturalPersonSelected
    );

  }

  onKeyPressNumbersAndDecimal(event: KeyboardEvent) {

    const currentValue = (event.target as HTMLInputElement).value;

    if (!event.key.match(/[0-9]/)) {

      if (event.key === '.' && currentValue.match(/[0-9]/)) {
        return;
      }
      event.preventDefault();
    }
  }
  onKeyPressNoNumeros(event: KeyboardEvent) {

    if (!event.key.match(/[a-zA-ZñÑáéíóúÁÉÍÓÚ ]/)) {
      event.preventDefault();
    }
  }
  onKeyPressNumbersAndHyphens(event: KeyboardEvent) {

    if (!event.key.match(/[0-9-]/)) {
      event.preventDefault();
    }
  }
  onKeyPressNumbers(event: KeyboardEvent) {

    if (!event.key.match(/[0-9]/)) {
      event.preventDefault();
    }
  }
  onKeyPressNoSignos(event: KeyboardEvent) {

    if (!event.key.match(/[a-zA-Z0-9 ]/)) {
      event.preventDefault();
    }
  }

  sendFactura(index: any) {
    this.transaction_id = index;
    let card = this.transactions_array[index];


    this.api
      .doRequest('transaction/sendpdf/' + card.id, {}, 'get')
      .then((res: any) => {
        if (res.code == 200) {
          this.emailForm.controls['email'].setValue(
            card.customers.EMAIL_CUSTUMER
          );

          this.pdfSRC = res.data;
        }
      });
  }

  onPayemendCondSelected(id: any) {
    if (id == '457') {

      this.transaction.id_credit_time = '1678';
      this.transaction.payment_type = 'BILLETES Y MONEDAS';
      this.transaction.id_payment_type = '460';
      this.transaction.id_payment_status = '1688';
    }

    if (id == '458') {

      this.transaction.id_credit_time = '474';
      this.transaction.payment_type = 'CUENTAS POR PAGAR DEL RECEPTOR';
      this.transaction.id_payment_type = '471';
      this.transaction.id_payment_status = '1687';
    }

  }

  filtered_items: Array<any> = [];
  filterprods(event: any) {
    this.filtered_items = this.items.filter(
      (valor: any, index: number, key: any) => {
        return (
          (valor.code.toLowerCase().includes(event) ||
            valor.name.toLowerCase().includes(event)) &&
          valor.existencia > 0
        );
      }
    );
  }

  toLower(event: any) {
    event.target.value = event.target.value.toLowerCase();
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^@.A-Za-z0-9_]/g, '').toLowerCase();
    input.value = value;
    this.registerForm.get('email')?.setValue(value);
  }

  addProduct(event: Event) {

    event.stopPropagation();


    this.manageField(5, 0);
  }


  addProductCR(event: Event) {

    event.stopPropagation();


    this.manageField(6, 0);



  }


  manageField(opt: number, i?: any) {





    let agregar = false;

    switch (opt) {
      case 1:
        agregar = true;
        this.campos.forEach((element) => {


          // if (this.transaction.id_doc_transaction_type == '44') {
          //   element.cantidad = 1;
          // }

          if (
            (element.item == '' && element.cantidad == 0) ||
            (this.transaction.id_doc_transaction_type == '44' &&
              element.precioiva == 0)
          ) {
            agregar = false;
          }
        });
        // if (agregar) {
        //   this.campos.push(new detalles());
        //   this.selected.push('1');
        // }



        break;
      case 2:
        this.campos.splice(i, 1);
        this.selected.splice(i, 1);
        break;

      case 3:
        // this.campos.push(new detalles());
        break;
      case 4:



        this.camposcat.pop();
        this.camposcat = [];

        this.items.forEach((it) => {
          let numindex = this.campos.findIndex(
            (item) => item.item_id === it.id
          );
          if (numindex >= 0) {

            this.camposcat.push(this.campos[numindex]);
          } else {
            let data: detalles = {
              item: it.nombre,
              name: it.nombre,
              nombre: it.nombre,
              item_id: it.id,
              item_desc: it.descripcion,
              cantidad: 0,
              iva: it.iva,
              precio: this.isCreditoFiscal
                ? it.precio
                : it.precio + it.precio * 0.13,
              precioiva: it.precio * 0.13,
              precioiva2: it.precio / 1.13,
              subtotal: 0,
              codigo: it.codigo,
              tipo: it.tipo,
              isgravada: it.tipo == 'G' ? true : false,
              isexenta: it.tipo == 'E' ? true : false,
              issujeta: it.tipo == 'NS' ? true : false,
              existencia_producto: 0,
              editable: it.editable,
              codigo_producto: it.codigo_producto,
              codigo_ingreso: it.codigo_ingreso,
              clasificacion: it.clasificacion,
              numeroDocumento: it.numeroDocumento,
              renta1: false,
              renta13: false,
            };
            this.camposcat.push(data);
          }
        });
        this.openModal('usermodal');
        break;
      case 5:
        agregar = true;
        this.campos.forEach((element) => {


          if (
            element.item == '' &&
            element.cantidad == 0 &&
            element.precioiva == 0
          ) {
            agregar = false;
          }
        });
        if (agregar) {
          this.campos.push(new detalles());
          this.selected.push('1');
        }
        break;

      case 6:
        agregar = true;
        if (agregar) {

          this.docAsociado.push(new CuerpoDto());
          this.selected.push('1');
        }
        break;

    }

    this.calculoTotales();



  }


  onQuantityChange(event: any, index: number) {
    if (
      (this.opcion == '1' || this.opcion == '3') &&
      this.camposcat[index].cantidad > this.items[index].existencia
    ) {
      this.camposcat[index].cantidad = this.items[index].existencia;
    }
  }
  checkValue(event: any, index: number) {
    if (this.campos[index].cantidad > this.campos[index].existencia_producto) {
      this.campos[index].cantidad = this.campos[index].existencia_producto;
    }
  }

  onInputChange(event: any, index: number) {

    let inputValue = event.target.value;


    if (inputValue.charAt(0) === '0') {
      inputValue = inputValue.slice(1);
    }


    if (inputValue > this.campos[index].existencia_producto) {
      inputValue = this.campos[index].existencia_producto;
      this.campos[index].cantidad = this.campos[index].existencia_producto;
    }


    event.target.value = inputValue;
    this.campos[index].cantidad = inputValue;

    this.subtotalChange(inputValue, index);
  }

  observacionesArray: any[] = [];

  showFactura(element: any) {

    console.log(' show factura element : ', element);


    if (
      element.status != 'ANULADO' &&
      element.status != 'APROBADO' &&
      element.status != 'APROBADO_CONTINGENCIA' &&
      element.status != 'RECHAZADO'
    ) {
      return this.util.showSWAL(
        'No se puede Mostrar el PDF',
        'Para mostrar el PDF relacionado el Documento debe haber sido transmitido con éxito',
        'Aceptar',
        'info'
      );
    }

    if (element.status === 'RECHAZADO') {
      const transaccion = element.dteTransacciones.find(
        (trans: any) =>
          (trans.codigoGeneracion === element.codigoGeneracion || trans.codigoGeneracionContingencia) &&
          trans.estadoDte === 'RECHAZADO'
      );

      console.log('transaccion : ', transaccion);

      if (!transaccion) {

        return;
      }

      let observacionesText = '';
      let descripcionMsg = '';
      let observacionesObj;

      try {
        // if (transaccion.|) {

        // }


        observacionesObj = JSON.parse(transaccion.observaciones);
      } catch (error) {
        console.log(error);
        console.log(observacionesObj);
        return this.util.showSWAL(
          'Error',
          'No se pudieron interpretar las observaciones.',
          'Aceptar',
          'error'
        );
      }

      console.log('observacionesObj : ', observacionesObj);

      descripcionMsg = observacionesObj.descripcionMsg;



      if (Array.isArray(observacionesObj.observaciones)) {
        this.observacionesArray = observacionesObj.observaciones;
        observacionesText = observacionesObj.observaciones
          .map((obs: string) => `• ${obs}`)
          .join('<br>');
      } else {

        observacionesText = 'No se encontraron observaciones.';
      }

      if (
        transaccion &&
        Array.isArray(observacionesObj.observaciones) &&
        observacionesObj.observaciones.length > 0
      ) {
        return this.util.showSWAL(
          'DTE Rechazado',
          `<b>Motivo:</b><br>${descripcionMsg} <br> <b>Observaciones:</b><br>${observacionesText}`,
          'Aceptar',
          'info'
        );
      } else {
        return this.util.showSWAL(
          'DTE Rechazado',
          `<b>Motivo:</b><br>${descripcionMsg}`,
          'Aceptar',
          'info'
        );
      }
    }

    this.openModalpdf(element);
    return;

  }

  openAproveModal() {
    this.dialog.open(this.aproveModal, {
      maxWidth: '900px',
      maxHeight: '100vh',

      panelClass: 'custom-dialog-container',
    });
  }

  openModalpdf(element: any) {

    this.sharedDataService.setVariable('mostrarElemento', element);

    const modalData = this.util.openModal({
      title: 'Vista Previa DTE',
      botonAceptar: 'Agregar',
      componentToLoad: ModalPdfComponent,
      callerComponent: this,
      data: element,
    });
  }

  abrirModalClientes() {
    const modalData = this.util.openModal({
      title: 'Busqueda de Clientes',
      botonAceptar: 'Agregar',
      componentToLoad: ModalClientesComponent,
      callerComponent: this,
      data: null,
    });
  }

  limpiarCliente() { }

  abrirModalVentas() {
    this.sharedDataService.setVariable(
      'tipoDTE',
      this.transaction.id_doc_transaction_type
    );
    this.sharedDataService.setVariable('cliente', this.cliente);
    this.sharedDataService.setVariable(
      'id_doc_transaction_type',
      this.transaction.id_doc_transaction_type
    );


    if (
      Object.keys(this.cliente).length > 0 ||
      this.transaction.id_doc_transaction_type == '41' ||
      this.transaction.id_doc_transaction_type == '40'
    ) {
      const modalData = this.util.openModal({
        title: 'Ventas Finalizadas',
        botonAceptar: 'Agregar',
        componentToLoad: ModalVentasComponent,
        callerComponent: this,
        data: null,
      });
    } else {
      this.util.showSWAL(
        'Cliente Desconocido',
        'Por Favor Seleccione un Cliente para Continuar. ',
        'Aceptar',
        'warning'
      );
    }
  }

  envio_correo = false;
  div_correo() {
    this.envio_correo = !this.envio_correo;
  }

  enviar_correo() {
    var updatedEmail = false;
    if (this.emailForm.value.email != this.card.customers.EMAIL_CUSTUMER) {
      var id = this.card.customers.ID_CUSTUMER;
      this.util
        .SWALYESNO(
          '¿Desea continuar?',
          'Se actualizará automáticamente el cambio de correo electrónico',
          'Si actualizar',
          'Enviar Sin Actualizar'
        )
        .then((res) => {
          if (res == true) {
            const data = {
              email: this.emailForm.value.email,
              name: this.card.customers.NAME_CUSTOMER,
              phone: this.card.customers.TELEPHONE,
            };
            this.api
              .doRequest('customer/update/' + id, data, 'put')
              .then((res: any) => {
                if (res.code == 200) {

                  updatedEmail = true;

                  var emailSent = '';
                  if (updatedEmail == true) {
                    emailSent = this.emailForm.value.email;
                  } else {
                    emailSent = this.card.customers.EMAIL_CUSTUMER;
                  }
                  this.enviarPdf(emailSent, true);
                }
              });
          } else {
            this.enviarPdf(this.card.customers.EMAIL_CUSTUMER);
          }
        });
    } else {
      this.enviarPdf(this.card.customers.EMAIL_CUSTUMER);
    }
  }

  enviarPdf(emailSent: string, updated?: boolean) {
    this.api
      .doRequest('transaction/sendpdf/' + this.card.id, {}, 'get')
      .then((res: any) => {
        if (res.code == 200) {
          this.util.showSWAL(
            'Envío Completo',
            'Factura Enviada Correctamente a: ' + emailSent,
            'Aceptar',
            'success'
          );
          this.envio_correo = !this.envio_correo;



        } else {
          this.util.showSWAL(
            'Error en el Envio',
            'No se pudo enviar el correo',
            'Aceptar',
            'error'
          );
        }
      });
  }

  generar_QR() {
    this.opcion =
      this.route.snapshot.paramMap.get('option') != null
        ? (this.route.snapshot.paramMap.get('option') as string)
        : '1';



    this.util
      .SWALYESNO('¿Esta seguro de Compmletar esta transaccion?', '')
      .then((res) => {



        if (res) {
          this.api
            .doRequest('hacienda/generarQR/' + this.card.id, {}, 'post')
            .then((res: any) => {
              if (res.code == 200) {
                this.pdfSRC = res.data.pdf;
                res.data = res.data.transacciones;
                this.setTransactions(res.data.transactions);

                let filteredTransactions = [];
                let filteredTransactions2 = [];
                for (let i = 0; i < res.data.transactions.length; i++) {
                  if (res.data.transactions[i].SIMPLE_SALE == '1') {
                    filteredTransactions.push(res.data.transactions[i]);
                  } else if (res.data.transactions[i].SIMPLE_SALE != '1') {
                    filteredTransactions2.push(res.data.transactions[i]);
                  }
                }
                if (this.opcion == '3') {
                  this.setTransactions(filteredTransactions);
                } else {
                  this.setTransactions(filteredTransactions2);
                }
                this.setNOADatatransaction(res.data.noaprobadas);


                this.util.showSWAL(
                  'Envío Completo',
                  'QR Generado y recibido por el Ministerio de Hacienda con éxito  ',
                  'Aceptar',
                  'success'
                );
                this.qr_generado = true;
              } else {
                if (res.code == 500) {
                  this.util.showSWAL(
                    res.status + ': ' + res.message,
                    '' + res.data.mensaje + '',
                    'Aceptar',
                    'error'
                  );
                } else {
                  this.util.showSWAL(
                    res.data.estado + ': ' + res.message,
                    '<b>' +
                    res.data.descripcionMsg +
                    '</b> <br>' +
                    this.listarObservaciones(res.data.observaciones),
                    'Aceptar',
                    'error'
                  );
                }
              }
            });
        } else {
        }
      });
  }

  listarObservaciones(observaciones: any) {
    let listaHtml = '<ul>';
    observaciones.forEach((elemento: any) => {
      listaHtml += `<li>${elemento}</li>`;
    });
    listaHtml += '</ul>';
    return listaHtml;
  }

  formatUSD(money: any) {
    try {
      return money.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD',
      });
    } catch (e) {
      return '$0.00';
    }
  }


  validatePercentage() {
    const total = this.calculos.total;
    if (this.descuentos.porcentaje === '%' && this.descuentos.cantidad > 100) {
      this.descuentos.cantidad = 0;
      this.descuentos.cantidad = 100;
    }
    if (
      this.descuentos.porcentaje === '-' &&
      this.descuentos.cantidad > total
    ) {
      this.descuentos.cantidad = 0;
      this.descuentos.cantidad = total;
    }
  }

  manageItems(opt: any, i?: any) {
    switch (opt) {
      case 1:
        this.item.subtotal = this.item.cantidad * this.item.precio;
        this.calculos.SUMAS += this.item.subtotal;
        this.calculos.SUBTOTAL = this.calculos.SUMAS;
        this.details.push(this.item);
        this.dataSource = new MatTableDataSource<any>(this.details);
        // this.item = new detalles();
        break;
      case 2:
        this.campos.splice(i, 1);
        this.docAsociado.splice(i,2);

        this.calculos = 0;






        break;
      case 3:
        this.api
          .doRequest('items/store', this.new_item, 'post')
          .then((res: any) => {
            if (res.code == 200) {


              this.items = res.data;
              this.filteredOptions.productos = res.data;
              this.new_item = new item();
              this.closeModal();
              this.util.showSWAL(res.status, res.message);
            } else {
              this.util.showSWAL(res.status, res.data);
            }
          });
        break;
      case 4:

        this.api
          .doRequest('items/store', this.new_item, 'post')
          .then((res: any) => {
            if (res.code == 200) {


              this.items = res.data;
              this.filteredOptions.productos = res.data;
              this.new_item = new item();
            } else {
              this.util.showSWAL(res.status, res.data);
            }
          });
        break;
      case 5:
        this.transacciones.splice(i, 1);
        if (this.transaction.id_doc_transaction_type == '41') {
          this.calculos.total = this.calculos.SUBTOTAL =
            this.transacciones.reduce(
              (total, transaction) => total + transaction.monto,
              0
            );
        }








        break;
    }
    if (this.transaction.id_doc_transaction_type != '41') {
      this.calculoTotales();
    }
  }

  total: number = 0;

  impuestotext: string = '';
  deleteimp(i: number) {
    this.impuestos_array.splice(i, 1);
    this.subtotalChange('descuentos', 1);
  }

  openVenta() {
    /**
     * CONSULTANDO DATOS REQUERIDOS PARA FACTURACION
     */

    if (
      this._auth.getRequiredInvoice() &&
      Object.keys(this._auth.getRequiredInvoice()).length > 0
    ) {
      this.util
        .SWALYESNO(
          'Debes actualizar tu perfil!',
          'Quieres actualizar ahora?',
          'Actualizar ahora',
          'Recuerdame más tarde'
        )
        .then((e) => {
          if (e) {
            const modalData = this.util.openModal({
              title: 'Actualización de Usuario',
              botonAceptar: 'Actualizar',
              componentToLoad: ManageUserComponent,
              callerComponent: this,
              footer: false,
              data: {
                user: {
                  idUser: 0,
                  usuario: this._auth.getLocalData('cnr-info', 'username'),
                },
                requerido: this._auth.getRequiredInvoice(),
                origin: 'ventas',
              },
            });
          }
        });
    } else {
      this.transaction = new transaction();
      this.x100_descuento_aplicar = 0;

      if (this.items) {
        if (this.items.length == 0) {
          this.util.showSWAL(
            'Error al Cargar Productos',
            'No existe comunicación con el Catálogo de Productos',
            'Aceptar',
            'error'
          );
        }
      }
      this.registerForm.reset();
      this.registerForm2.reset();
      this.clearSelection();

      this.filteredOptions.productos = this.items_iniciales;
      this.items = this.items_iniciales;
      this.itemsG = [];
      this.totalIva = 0;

      this.transaction = new transaction();




      this.view = 'forms';

      this.cliente = null;
      this.filteredOptions.clientes = this.clientes;
      this.customers = this.clientes;







      this.items = this.filteredOptions.productos;
    }

    /**
     * FIN - CONSULTANDO DATOS REQUERIDOS PARA FACTURACION
     */


  }

  fecha(timestamp: any) {
    const date = new Date(timestamp);

    const dia = date.getDate();
    const mes = date.getMonth() + 1;
    const año = date.getFullYear();

    const diaString = dia < 10 ? '0' + dia : dia;
    const mesString = mes < 10 ? '0' + mes : mes;

    return `${diaString}/${mesString}/${año}`;
  }

  fechaYHora(timestamp: any) {
    const date = new Date(timestamp);

    const dia = date.getDate();
    const mes = date.getMonth() + 1;
    const año = date.getFullYear();

    const horas = date.getHours();
    const minutos = date.getMinutes();
    const segundos = date.getSeconds();

    const diaString = dia < 10 ? '0' + dia : dia;
    const mesString = mes < 10 ? '0' + mes : mes;
    const horasString = horas < 10 ? '0' + horas : horas;
    const minutosString = minutos < 10 ? '0' + minutos : minutos;
    const segundosString = segundos < 10 ? '0' + segundos : segundos;

    return `${diaString}/${mesString}/${año} ${horasString}:${minutosString}:${segundosString}`;
  }


  fechaYHoraAMPM(timestamp: any) {
    const date = new Date(timestamp);

    const dia = date.getDate();
    const mes = date.getMonth() + 1;
    const año = date.getFullYear();

    let horas = date.getHours();
    const minutos = date.getMinutes();
    const segundos = date.getSeconds();

    // Determinar AM o PM
    const ampm = horas >= 12 ? 'PM' : 'AM';

    // Convertir a formato 12 horas
    horas = horas % 12;
    horas = horas ? horas : 12; // La hora "0" debe ser "12"

    const diaString = dia < 10 ? '0' + dia : dia;
    const mesString = mes < 10 ? '0' + mes : mes;
    const horasString = horas < 10 ? '0' + horas : horas;
    const minutosString = minutos < 10 ? '0' + minutos : minutos;
    const segundosString = segundos < 10 ? '0' + segundos : segundos;

    return `${diaString}/${mesString}/${año} ${horasString}:${minutosString}:${segundosString} ${ampm}`;
  }

  horaElSalvador(timestamp: any) {
    const fechaEnElSalvador = moment(timestamp).tz("America/El_Salvador").format('DD-MM-YYYY hh:mm A');
    return fechaEnElSalvador;

  }

  convertirEstado(state: any): string {
    switch (state) {
      case '1697':
        return 'PENDIENTE DE APROBACION';
      case '1698':
        return 'RECHAZADA';
      case '1699':
        return 'APROBADA';
      case '1719':
        return 'FINALIZADA';
      case '1720':
        return 'ANULADA';
      case '1696':
        return 'EN PROCESO';
      default:
        return 'EN PROCESO';
    }
  }

  delete(element: any) { }
  anular(element: any) {
    console.log('element : ', element);

    this.abrirModalAnulacion(element);
  }
  addNote(element: any, i: any) { }

  manageaproval(data: any, opt: number = 1) {

    let response = {
      id: data.id,
      state: opt == 1 ? 1699 : 1698,
    };

  }

  capitalizeFirst(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  capitalizeWords(value: string): string {
    if (!value) return '';
    return value
      .split(' ')
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  formatTimestamp(timestamp: any) {

    var date = new Date(timestamp);


    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0');
    var day = date.getDate().toString().padStart(2, '0');
    var hours = date.getHours().toString().padStart(2, '0');
    var minutes = date.getMinutes().toString().padStart(2, '0');
    var seconds = date.getSeconds().toString().padStart(2, '0');


    var formattedDate = day + '/' + month + '/' + year;
    var formattedTime = hours + ':' + minutes + ':' + seconds;


    return formattedDate + ' ' + formattedTime;
  }

  formatDate(timestamp: any) {

    var date = new Date(timestamp);


    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0');
    var day = date.getDate().toString().padStart(2, '0');
    var hours = date.getHours().toString().padStart(2, '0');
    var minutes = date.getMinutes().toString().padStart(2, '0');
    var seconds = date.getSeconds().toString().padStart(2, '0');



    var formattedDate = year + '-' + month + '-' + day;

    var formattedTime = hours + ':' + minutes + ':' + seconds;

    return formattedDate;
  }


  formatDate2(timestamp: any): string {
    // Convertir el timestamp a la zona horaria de El Salvador
    const dateInElSalvador = moment2.tz(timestamp, "America/El_Salvador");

    // Formatear la fecha y la hora
    const formattedDate = dateInElSalvador.format('YYYY-MM-DD'); // Formato de fecha
    const formattedTime = dateInElSalvador.format('HH:mm:ss');   // Formato de hora

    // Retornar la fecha formateada y la hora si es necesario
    return `${formattedDate} ${formattedTime}`; // O simplemente return formattedDate si solo necesitas la fecha
  }


  formatDateAndTime(timestamp: any) {
    // Convierte el timestamp a una fecha en la zona horaria de El Salvador
    const fechaHora = moment2.tz(timestamp, "America/El_Salvador");

    // Formatear la fecha y hora en un solo string en formato ISO
    const formattedDateTime = fechaHora.format("YYYY-MM-DDTHH:mm:ss"); // Formato ISO 8601

    return formattedDateTime; // Retorna un solo string
  }


  obtenerItemsUnicos(arrayOriginal: any[]): any[] {
    let itemsUnicos: { [key: number]: boolean } = {};
    let resultado: any[] = [];

    arrayOriginal.forEach((objeto: any) => {
      objeto.items.forEach((item: any) => {
        if (!itemsUnicos[item.nombre]) {


          resultado.push(item);
          itemsUnicos[item.nombre] = true;
        }
      });
    });

    return resultado;
  }











  completarInformacion(arrayIdItems: any[], itemsCompletos: any[]): any[] {



    return arrayIdItems.map((item) => {
      const itemCompleto = itemsCompletos.find((i) => i.nombre === item.nombre);
      const newItem = { ...item, ...itemCompleto };
      const itemID = itemsCompletos.find(
        (i) => i.nombre === item.nombre
      )?.nroDocumento;
      return { ...newItem, numeroDocumento: itemID };
    });
  }
  clearSelection() {
    this.x100_descuento_aplicar = 0;

    this.cliente = null;

    this.cliente = {};

    if (this.registerForm) {
      this.registerForm.reset();
    }
    if (this.registerForm2) {
      this.registerForm2.reset();
    }
    this.opcion = '1';
    this.transacciones = [];
    this.campos = [new detalles()];

    this.filteredOptions.productos = this.items;

    this.inputChange = '';

    this.customer = new Cliente();

    this.calculoTotales();
  }

  validarCantidades(arreglo: any[]): boolean {


    if (arreglo.length == 0) {
      this.campos = [new detalles()];
      return false;
    }

    arreglo.forEach((res, index) => {
      if (res.cantidad > 0 && res.item != '') {
      } else {
        this.campos.splice(index, 1);
      }
    });

    for (let i = 0; i < arreglo.length; i++) {
      const item = arreglo[i];
      const cantidad = parseInt(item.cantidad);


      if (cantidad < 1 || cantidad > 2000) {






        return false;
      }
    }


    return true;
  }

  CR_manual: boolean = false;
  seleccionCR(value: any) {


    switch (value) {
      case 1:
        this.CR_manual = true;
        this.transaction.id_customer = 'add2';
        break;
      case 2:
        this.CR_manual = false;
        this.transaction.id_customer = '';
        break;
      default:
        this.CR_manual = false;

    }



  }

  tipo_DTE(tipoDTE: any) {
    let tipoDocumento: string;

    switch (tipoDTE) {
      case '01':
        tipoDocumento = 'FACTURA';
        break;
      case '03':
        tipoDocumento = 'COMPROBANTE_CREDITO_FISCAL';
        break;
      case '04':
        tipoDocumento = 'NOTA_REMISION';
        break;
      case '05':
        tipoDocumento = 'NOTA_CREDITO';
        break;
      case '06':
        tipoDocumento = 'NOTA_DEBITO';
        break;
      case '07':
        tipoDocumento = 'COMPROBANTE_RETENCION';
        break;
      case '08':
        tipoDocumento = 'COMPROBANTE_LIQUIDACION';
        break;
      case '09':
        tipoDocumento = 'DOCUMENTO_CONTABLE_LIQUIDACION';
        break;
      case '11':
        tipoDocumento = 'FACTURAS_EXPORTACION';
        break;
      case '14':
        tipoDocumento = 'FACTURA_SUJETO_EXCLUIDO';
        break;
      case '15':
        tipoDocumento = 'COMPROBANTE_DONACION';
        break;
      default:
        tipoDocumento = 'Tipo de documento no reconocido';
    }

    return tipoDocumento;
  }

  timestampToDate(timestamp: number): Date {
    return new Date(timestamp);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  filterColumns: string[] = [
    'customer',
    'numeroDTE',
    'type_transaction',
    'fechaTransaccion',
    'codigoGeneracion',
    'status',
    'totalTransaccion',
  ];

  applyCustomFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.toUpperCase();
    this.dataSource.filterPredicate = (data: any, filter: string) => {
      return this.filterColumns.some((column) => {
        if (column === 'customer') {
          const value =
            (data.cliente &&
              data.cliente.nombre &&
              data.cliente.nombre.toLowerCase().trim()) ||
            '';
          return value.includes(filter.toLowerCase());
        } else if (column === 'date') {

          const formattedDate = this.fecha(data.createdAt);
          return formattedDate.includes(filter.toLowerCase());
        } else if (column === 'type_transaction') {
          const tipoDte = this.tipo_DTE(data.tipoDTE).toLowerCase();
          return tipoDte.includes(filter.toLowerCase());
        } else if (column === 'totalTransaccion') {

          const value = data.totalTransaccion
            ? data.totalTransaccion.toString()
            : '';
          return value.includes(filter);
        } else {
          const value =
            (data[column] && data[column].toLowerCase().trim()) || '';
          return value.includes(filter.toLowerCase());
        }
      });
    };
    this.dataSource.filter = filterValue;
  }

  selectChangeMunicipio(event: any, opt = 1) {

    console.log("selectChangeMunicipio", event);
    let id = event.option.value;




    switch (opt) {
      case 1:
        let val2: any = this.municipios.filter((item) => {
          if (item.name.includes(id)) {
            return item;
          }
        })[0];


        this.departamentoSelected = val2.id_mh_padre;
        this.municipioSelected = val2.id_mh;

        this.registerForm2.get('idMunicipio')?.setValue(val2.id);
        this.registerForm2.get('nombreMunicipio')?.setValue(val2.municipio);
        this.registerForm2.get('idDepto')?.setValue(val2.departamento);
        break;

      case 2:
        break;
    }
  }

  onKeyPressLetras(event: KeyboardEvent) {



    if (!event.key.match(/[a-zA-Z ]/)) {
      event.preventDefault();
    }
  }

  filtervaluesMunicipio(event: any = '', opt: any = 1) {
    if (event) {
      switch (opt) {
        case 1:
          const searchTerm = event.toUpperCase();


          let value = this.municipios.filter((item) => {

            if (item.name && typeof item.name === 'string') {





              return item.name.toUpperCase().includes(searchTerm);
            }
            return false;
          });
          this.filteredOptions.municipios = value;

          break;
        case 2:

          break;
      }
    }
  }

  aplicarIVARet: boolean = false;
  toggleActivo: 'renta1' | 'renta13' = 'renta1';

  aplicarIVA(event: any, i: any) {
    const toggleId = event.source.id;











    this.calculoTotales();




























  }

  //!No tiene relevancia en la estructura del codigo debido a que no condiciona nada

  /* renta13(event: any) {
    if (event.checked) {


    } else {


    }
  } */

  addComprobante() {
    const comprobanteValue = this.comprobanteForm.get('comprobante')?.value;
    if (comprobanteValue) {



      this.api
        .doRequest2(
          this.apiUrl + '/dte/consulta/valcp/' + comprobanteValue,
          null,
          'get'
        )
        .then((res: any) => {
          if (res.codigo != 1) {
            return this.util.showSWAL(
              'Comprobante de pago inválido',
              res.mensaje,
              'Aceptar',
              'warning'
            );
          }


























          this.transaction.comprobantes.push({
            numeroComprobante: comprobanteValue,
          });
          this.comprobanteForm.reset();
          return;
        });
    }
  }

  removeComprobante(index: number) {
    this.transaction.comprobantes.splice(index, 1);
    this.docAsociado.splice(index,1);
  }

  mask: string = 'SSSSSSSS-SSSS-SSSS-SSSS-SSSSSSSSSSSS';

  handlePaste(event: ClipboardEvent): void {

    const clipboardData = event.clipboardData;
    const pastedText = clipboardData?.getData('text');

    if (pastedText) {

      this.mask = '';


      setTimeout(() => {


        this.mask = 'SSSSSSSS-SSSS-SSSS-SSSS-SSSSSSSSSSSS';
      });
    }
  }

  validarCampos(transaction: any): boolean {


    const requiredFields = [
      { field: 'descripcion', name: 'Descripción' },
      { field: 'monto', name: 'Monto' },
      { field: 'fechaEmision', name: 'Fecha de Emisión' },
      { field: 'nroDocumento', name: 'Número de Documento' },
      { field: 'tipoDte', name: 'Tipo DTE' },
      { field: 'tipoGeneracion', name: 'Tipo de Generación' },
    ];


    if (transaction.tipoDte === '02') {
      requiredFields.push({
        field: 'codigoGeneracion',
        name: 'Código de Generación',
      });
    }

    for (const { field, name } of requiredFields) {
      if (!transaction[field]) {
        this.util.showSWAL(
          'Faltan Datos',
          `${name} no puede ir vacío`,
          'Aceptar',
          'warning'
        );

        return false;
      }
    }


    return true;
  }

  validarClienteCNR(): boolean {
    if (this.transaction.id_customer == 'add2') {
      if (this.registerForm2.invalid) {


        let errores = '';
        Object.keys(this.registerForm2.controls).forEach((key) => {
          const control = this.registerForm2.get(key);
          if (control != null) {
            const controlErrors = control.errors;
            if (controlErrors != null) {
              Object.keys(controlErrors).forEach((keyError) => {
                errores =
                  'Campo inválido o incompleto: ' +
                  key +
                  ', error: ' +
                  keyError;
              });
            }
          }
        });
        this.util.showSWAL(
          'FALTAN DATOS DEL CLIENTE',
          errores,
          'Aceptar',
          'error'
        );
        return false;
      }
    }

    return true;
  }


  formatDUI(dui: string): string {
    if (!dui) return '';

    if (dui.length !== 9) return dui;


    return `${dui.slice(0, 8)}-${dui.slice(8)}`;
  }

  paginate(pe: PageEvent): void {
    this.paginationSize = pe.pageSize;
    this.currentPageNo = pe.pageIndex;
    this.loadCurrentPageVentas(this.currentPageNo, this.paginationSize);
  }




  habilitarFRM() {
    this.registerForm.controls['departamento']?.enable({
      onlySelf: false
    })
  }

  filterValuesDepartamentos(id: any) {
    this.departamentoSelected = this.departamentosArr.filter((depto: any) => depto.id_mh == id)[0]

    this.municipiosArrFiltered = this.municipiosArr.filter((depto: any) => depto.id_mh_padre == id)

    this.registerForm.controls['municipio']?.enable({
      onlySelf: false
    })

  }


  filterValuesMunicipios(id: any) {
    this.municipioSelected = this.municipiosArr.filter((depto: any) => depto.id_mh == id)[0];

  }
  private isComparing = false;
  private isComparingMuni = false;

  compareFn = (o1: any, o2: any): boolean => {
    if (o1 && o2 && o1.toString() === o2.toString()) {
      if (!this.isComparing) {
        this.isComparing = true;
        this.departamentoSelected = this.departamentosArr.filter((depto: any) => depto.id_mh == o1)[0]
        this.municipiosArrFiltered = this.municipiosArr.filter((depto: any) => depto.id_mh_padre == o1)

        this.isComparing = false;
      }
      return true;
    } else {
      return false;
    }
  };

  compareFnMuni(o1: any, o2: any): boolean {
    if (o1 && o2 && o1.toString() === o2.toString()) {
      if (!this.isComparingMuni) {
        this.isComparing = true;

        this.isComparingMuni = false;
      }
      return true;
    } else {
      return false;
    }
  }
}
