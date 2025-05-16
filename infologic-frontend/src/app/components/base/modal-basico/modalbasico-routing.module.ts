import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ModalBasicoComponent } from './modalbasico.component';

const routes: Routes = [
  {
    path: '',
    component: ModalBasicoComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ModalBasicoRoutingModule { }
