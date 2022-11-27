import { Injectable } from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  key = 'Auth';
  isLoggedIn = false;
  redirectUrl!: string;

  private _user!: User;

  get user(): User {
    if (!this._user) {
      let data = JSON.parse(window.localStorage.getItem(this.key) || "{}");
      this._user = Object.assign({}, data);
    }

    return this._user;
  }

  set user(value: User) {
    this._user = Object.assign(this._user ? this._user : {}, value);
    window.localStorage.setItem(this.key, JSON.stringify(this._user));
  }

  remove() {
    window.localStorage.removeItem(this.key);
  }

  constructor(
    private apiService: ApiService
  ) {
    if (this.user.mobile && this.user.token) {
      this.isLoggedIn = true;
    }
  }

  login(user: { mobile: string, password: string }): Observable<any> {
    return new Observable((x) => {
      this.apiService.login(user).subscribe(
        (z) => {
          this.user = Object.assign(user, z);
          this.isLoggedIn = true;
          x.next(z);
          x.complete();
        },
        (k) => {
          x.error(k);
          x.complete();
        },
        () => {
          x.complete();
        }
      );
    });
  }
}
