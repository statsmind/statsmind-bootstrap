import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AuthGuard} from "../services/auth.guard";
import {AppComponent} from "./app.component";

const routes: Routes = [
  {
    path: 'portal',
    component: AppComponent,
    children: [
      {
        path: 'auth',
        loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
      },
      {
        path: 'user',
        loadChildren: () => import('./user/user.module').then(m => m.UserModule),
        canActivate: [AuthGuard],
        canLoad: [AuthGuard]
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'user'
      },
    ]
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'portal'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
