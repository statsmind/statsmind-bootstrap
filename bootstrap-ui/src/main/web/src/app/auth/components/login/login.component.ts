import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {ApiService} from "../../../../services/api.service";
import {AuthService} from "../../../../services/auth.service";
import {NzMessageService} from "ng-zorro-antd/message";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm = {
    mobile: '',
    password: ''
  }

  constructor(
    private api: ApiService,
    private auth: AuthService,
    private toast: NzMessageService,
    private router: Router
  ) {
    if (this.auth.isLoggedIn) {
      this.doRedirectBack()
    }
  }

  ngOnInit(): void {
    this.loginForm.password = '';
  }

  doLogin() {
    this.auth.login(this.loginForm).subscribe(r => {
      this.toast.success("登录成功").onClose.subscribe(r => {
        this.doRedirectBack()
      })
    }, err => {
      this.toast.error('登录失败');
    })
  }

  doRedirectBack() {
    let redirectUrl = this.auth.redirectUrl || '/portal/user/home'
    if (!redirectUrl.startsWith('/portal')) {
      redirectUrl = '/portal/user/home'
    }

    console.log(redirectUrl)
    this.router.navigate([redirectUrl]).then(() => {})
  }
}
