# cnr-transmisor
Transmisor DTEs CNR

Pasos para su correcto funcionamiento:

1-Registros en tabla RCatalogos de acuerdo al manual de catalogos del Ministerio de Hacienda.
2-Tabla Company con la información real del CNR (Clave, certificado, nit, nrc, actividad economica, etc).
3-Firmador MH con el certificado proporcionado por el CNR.
4-Tabla rango con registros para cada uno de los DTEs que serán transmitidos (Deben de estar activos los rangos y con un rango válido, además de estar asociado al registro correspondiente de la tabla RCatalogo en ID_TIPO_RANGO)
5-Tabla sucursal debe de tener registrado el código de la sucursal (No debe de exceder 4 digitos, caso contrario la tranmisión fallará, la longitud del campo debería de estar validada desde el front).
6-La tabla transacción ya debe de estar guardando el id sucursal, caso contrario la transmisión fallará (como alternativa pueden registrar la transacción, hacer el update en la base y levantar el transmisor posteriormente).
7-Recomiendo dropear las tablas "COLA", "ITEM", "TRANSACCION" y "DTE_TRANSACCION" y/o cualquier tabla que pueda estar relacionada ya que las estructuras pueden estar desfasadas o seguir manteniendo contrains inválidas.
8-Cambiar en el archivo application.yml los properties relacionados a la base de datos.
9-Maven Clean & Install para asegurarse que las dependencias sean descargadas y empaquetadas.
10- Iniciar

¿COMO SABER SI UN DTE FUE TRANSMITIDO?
-SELECT * FROM COLA; (Cola tiene los siguientes atributos: ES_CONTIGENCIA, NOTIFICADO_CONTIGENCIA, FINALIZADO, NRO_INTENTO, NRO_INTENTO_CONT, ID_TRANSACCION) para saber el sttus de las transaccioens encoladas.
-SELECT * FROM TRANSACCION ORDER BY ID_TRANSACCION DESC; (Transacciones más recientes, aprobadas status = 2, error status = 4), consultar Enum StatusCola para saber los estados
-SELECT * FROM DTE_TRANSACCION ORDER BY ID_TRANSACCION DESC, ID_DTE_TRANSACCION DESC; (Intentos de json enviados a MH en orden, se puede visualizar json enviado, estado y observaciones en caso de ser rechazado, entre otra información).
-Correo enviado a dirección electrónica (Recomiendo editar el correo del cliente para que sea enviado a un correo de su preferencia).


Adicionalmente, se encuentra los siguientes 3 endpoints:

-ENDPOINT para visualizar el reporte del DTE (Lógicamente debe de ser usado únicamente con DTEs aprobados, esa validación debería de ser desde el front.
TIPO: GET
/api/v1/dte/reporte/{codigoGeneracion}

-ENDPOINT para anular alguno de los DTEs que han sido emitidos
TIPO: POST
/api/v1/dte/anularDte/{codigoGeneracion}
Body esperado:
{
    "tipoInvalidacion": 2,
    "motivoInvalidacion": "Prueba de invalidacion",
    "nombreResponsable": "Gerardo Ernesto Navarro Góchez",
    "tipoDocResponsable": "02",
    "numDocResponsable": "0626776320",
    "nombreSolicita":"Gerardo Ernesto Navarro Góchez",
    "tipoDocSolicita":"02",
    "numDocSolicita":"0626776320",
    "codigoGeneracionr": "INSERTAR CODIGO GENERACION QUE REMPLAZA SI APLICA "
}

La información ingresada en tipo de invalidación, motivo invalidación y codigoGeneracionr dependerá de acuerdo al tipo de invalidación que se está realizando y es información presentada de acuerdo a catalogos MH. Recomiendo consultar manuales funcionales y técnicos asi como el json schema para verificar las combinaciones válidas.


-ENDPOINT para enviar DTE a algun correo en especifico (Petición funcional de equipo CNR)
TIPO: POST
/api/v1/dte/enviar
Body esperado:
{
  "codigoGeneracion": "CODIGO GENERACION DEL DTE",
  "correo": "dirreccionDeCorreoAEnviarDTE@prueba.com"
}













NOTA 1: el transmisor se levanta sobre el puerto 8080 
NOTA: Nrc, nit de clientes o emisor deben de ser ingresados sin "-"
