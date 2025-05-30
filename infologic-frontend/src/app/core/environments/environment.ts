export const environment = {
  production: false,
  apiServerUrl: (window as any)['env']?.apiServerUrl || '',
  apiTransmisorUrl: (window as any)['env']?.apiTransmisorUrl || '',

};
