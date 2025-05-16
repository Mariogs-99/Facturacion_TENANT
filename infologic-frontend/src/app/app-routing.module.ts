import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/autentificacion/login/login.component';
import { PagesComponent } from './pages/pages.component';
import { NoAuthGuard } from './core/guard/no-auth.guard';
import { AuthGuard } from './core/guard/auth.guard';
import { BranchComponent } from './pages/branch/branch.component';
import { MenuComponent } from './pages/menu/menu.component';
import { VentasComponent } from './pages/ventas/ventas.component';
import { PermissionsComponent } from './pages/permissions/permissions.component';
import { RolesComponent } from './pages/roles/roles.component';
import { UserComponent } from './pages/user/user.component';
import { InstitucionComponent } from './pages/institucion/institucion.component';
import { ForgotPasswordComponent } from './pages/autentificacion/forgot-password/forgot-password.component';
import { ReportesComponent } from './pages/reportes/reportes.component';
import { ComprasComponent } from './pages/compras/compras.component';
import { ProductoComponent } from './pages/producto/producto.component';
import { ClienteComponent } from './pages/cliente/cliente.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [AuthGuard],
    pathMatch: 'full',
  },

  { 
    path: 'productos', component: ProductoComponent 
  },

  { 
    path: 'clientes', component: ClienteComponent 
  },
  {
    path: '',
    redirectTo: 'pages',
    pathMatch: 'full',
  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent,
    pathMatch: 'full',
  },
  {
    path: 'pages',
    component: PagesComponent,
    canActivate: [NoAuthGuard],
    children: [
      {
        path: '',
        component: DashboardComponent,
      },
    ],
  },
  {
    path: 'config',
    component: PagesComponent,
    canActivate: [NoAuthGuard],
    children: [
      {
        path: 'menu',
        component: MenuComponent,
      },
      {
        path: 'permisos',
        component: PermissionsComponent,
      },
      {
        path: 'roles',
        component: RolesComponent,
      },
      {
        path: 'users',
        component: UserComponent,
      },
      {
        path: 'sucursales',
        component: BranchComponent,
      },
      {
        path: 'institucion',
        component: InstitucionComponent,
      },

      { 
        path: 'productos', component: ProductoComponent 
      },
    
      { 
        path: 'clientes', component: ClienteComponent 
      },
    ],
  },
  {
    path: 'transacciones',
    component: PagesComponent,
    canActivate: [NoAuthGuard],
    children: [
      {
        path: 'ventas',
        component: VentasComponent,
      },
      {
        path: 'reportes',
        component: ReportesComponent
      },{
        path: 'compras',
        component: ComprasComponent
      },
    ]
  },
  {
    path: '**',
    redirectTo: 'pages',
  },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
