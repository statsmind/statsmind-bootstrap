import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import {Observable} from "rxjs";
import {sm3} from "sm-crypto";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(
    private httpService: HttpService,
  ) { }

  login(params: {mobile: string; password: string}): Observable<User> {
    params = Object.assign({}, params)
    params['password'] = sm3(params['password'])
    return this.httpService.post("/api/auth/login", params);
  }
}
