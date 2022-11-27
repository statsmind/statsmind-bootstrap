import { Component, OnInit } from '@angular/core';
import {SampleDialogComponent} from "../../dialogs/sample-dialog/sample-dialog.component";
import {AuthService} from "../../../../services/auth.service";
import { NzMessageService } from 'ng-zorro-antd/message';
import {NzModalService} from "ng-zorro-antd/modal";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(
    private auth: AuthService,
    private toast: NzMessageService,
    private modal: NzModalService
  ) { }

  ngOnInit(): void {
    this.toast.success("欢迎登录")
    this.toast.success("欢迎登录2")
  }

  get user() {
    return this.auth.user;
  }

  doLogout() {
    this.auth.remove()
    window.location.reload();
  }

  openDialog() {
    this.modal.create({
      nzContent: SampleDialogComponent
    })
  }
}
