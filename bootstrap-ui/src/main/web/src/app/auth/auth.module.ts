import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { FormsModule } from "@angular/forms";
import {LoginComponent} from "./components/login/login.component";
import {AuthIndexComponent} from "./containers/auth-index/auth-index.component";
import {ForgotPasswordComponent} from "./components/forgot-password/forgot-password.component";
import {SignupComponent} from "./components/signup/signup.component";


@NgModule({
  declarations: [
    LoginComponent,
    AuthIndexComponent,
    ForgotPasswordComponent,
    SignupComponent
  ],
    imports: [
        CommonModule,
        AuthRoutingModule,
        FormsModule
    ]
})
export class AuthModule { }
