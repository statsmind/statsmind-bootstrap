import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {UserIndexComponent} from "./containers/user-index/user-index.component";
import {HomeComponent} from "./components/home/home.component";

const routes: Routes = [
  {
    path: '',
    component: UserIndexComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
