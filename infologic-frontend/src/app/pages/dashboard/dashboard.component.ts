import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { ApiService } from 'src/app/core/services/api.service';
import { CounterUp } from './dashboad.model';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  kpiData: any[] = [];
  kpiDataSucursal: any[] = [];
  sucursales: any[] = [];
  selectedPuntoVenta!: number;

  totalEmitidos: number = 0;
  totalInvalidados: number = 0;
  totalContingencia: number = 0;
  totalRechazados: number = 0;
  totalFECanceladas: number = 0;
  totalCCFECancelados: number = 0;

  totalInvalidadosSucursal: number = 0;
  totalContingenciaSucursal: number = 0;
  totalRechazadosSucursal: number = 0;
  chart!: Chart<'bar', any[], any>;

  constructor(private api: ApiService, private router: Router) {}

  redirigirAPaginaEspecifica() {
    this.router.navigate(['/transacciones/ventas']);
  }

  redirigirAPaginaReportes() {
    this.router.navigate(['/transacciones/reportes']);
  }

  ngOnInit(): void {
    this.api.doRequest('/kpis/getKPI', {}, 'get').then((res: any) => {
      this.kpiData = res;

      this.totalEmitidos =
        this.kpiData.find((kpi) => kpi.id === 'kpi1')?.target || 0;

      this.totalInvalidados =
        this.kpiData.find((kpi) => kpi.id === 'kpi1')?.value || 0;
      this.totalContingencia =
        this.kpiData.find((kpi) => kpi.id === 'kpi2')?.value || 0;
      this.totalRechazados =
        this.kpiData.find((kpi) => kpi.id === 'kpi3')?.value || 0;
      this.totalFECanceladas =
        this.kpiData.find((kpi) => kpi.id === 'kpi4')?.value || 0;
      this.totalCCFECancelados =
        this.kpiData.find((kpi) => kpi.id === 'kpi5')?.value || 0;
    });
    setTimeout(() => {
      this.chartTest();
      this.loadTiles();
    }, 1900);

    this.api
      .doRequest('/kpis/getSucursalByPuntoVenta', {}, 'get')
      .then((res: any) => {
        this.sucursales = res;
      });
  }

  chartTest() {
    const labels = this.kpiData.map((kpi) => kpi.name);
    const values = this.kpiData.map((kpi) => kpi.value);
    const totalTarget = this.kpiData.length > 0 ? this.kpiData[0].target : 0;

    this.chart = new Chart('completedTasksChartCredits', {
      type: 'bar',
      data: {
        labels: [...labels, 'DTE Emitidos'], 
        datasets: [
          {
            label: 'KPI Values',
            data: [...values, totalTarget], 
            backgroundColor: [
              ...labels.map(() => 'rgba(75, 192, 192, 0.2)'),
              'rgba(255, 159, 64, 0.2)', 
            ],
            borderColor: [
              ...labels.map(() => 'rgba(75, 192, 192, 1)'),
              'rgba(255, 159, 64, 1)', 
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }

  private kpiXSucursalChart: Chart | undefined; 

  chartSucursal(idSucursal: any) {
    this.api
      .doRequest('/kpis/listKPIByVenta/' + idSucursal, {}, 'get')
      .then((res: any) => {
        this.kpiDataSucursal = res;

        this.totalInvalidadosSucursal =
          this.kpiDataSucursal.find((kpi) => kpi.id === 'kpi1')?.value || 0;
        this.totalContingenciaSucursal =
          this.kpiDataSucursal.find((kpi) => kpi.id === 'kpi2')?.value || 0;
        this.totalRechazadosSucursal =
          this.kpiDataSucursal.find((kpi) => kpi.id === 'kpi3')?.value || 0;

        this.updateChart();
        this.loadTiles();
      });
  }

  updateChart() {
    if (this.kpiXSucursalChart) {
      this.kpiXSucursalChart.destroy();
    }

    const labels = this.kpiDataSucursal.map((kpi) => kpi.name);
    const values = this.kpiDataSucursal.map((kpi) => kpi.value);

    const canvas = document.getElementById('kpiXSucursal') as HTMLCanvasElement;
    this.kpiXSucursalChart = new Chart(canvas, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'KPI x Sucursal',
            data: values,
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
        responsive: true,
        maintainAspectRatio: false,
      },
    });
  }

  public dashtotalEmitidos: any = [];
  public dashtotalRechazados: any = [];
  public dashtotalInvalidados: any = [];
  public dashtotalContingencia: any = [];
  public dashtotalCCFECancelados: any = [];
  public dashtotalFECanceladas: any = [];

  public dashtotalRechazadosSucursal: any = [];
  public dashtotalInvalidadosSucursal: any = [];
  public dashtotalContingenciaSucursal: any = [];

  public response: any;
  public timeDuration = 5000;

  loadTiles() {
    this.dashtotalEmitidos = new CounterUp(
      this.totalEmitidos,
      this.timeDuration
    );
    this.dashtotalRechazados = new CounterUp(
      this.totalRechazados / this.totalEmitidos,
      this.timeDuration
    );
    this.dashtotalInvalidados = new CounterUp(
      this.totalInvalidados / this.totalEmitidos,
      this.timeDuration
    );
    this.dashtotalContingencia = new CounterUp(
      this.totalContingencia / this.totalEmitidos,
      this.timeDuration
    );
    this.dashtotalCCFECancelados = new CounterUp(
      this.totalCCFECancelados / this.totalEmitidos,
      this.timeDuration
    );
    this.dashtotalFECanceladas = new CounterUp(
      this.totalFECanceladas / this.totalEmitidos,
      this.timeDuration
    );

    this.dashtotalRechazadosSucursal = new CounterUp(
      this.totalRechazadosSucursal,
      this.timeDuration
    );

    this.dashtotalInvalidadosSucursal = new CounterUp(
      this.totalInvalidadosSucursal,
      this.timeDuration
    );
    this.dashtotalContingenciaSucursal = new CounterUp(
      this.totalContingenciaSucursal,
      this.timeDuration
    );
  }
}
