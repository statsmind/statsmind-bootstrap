import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({ providedIn: 'root' })
export class HttpService {
  api!: string;

  constructor(
    public http: HttpClient
  ) {}

  /**
   * get请求
   *
   * @param {string} url 地址
   * @param {any} [params] 参数
   * @returns
   * @memberof HttpService
   */
  get(url: string, params?: any, isBody?: boolean): Observable<any> {
    return this.request('GET', url, params, isBody);
  }

  /**
   * get请求
   *
   * @param {string} url 地址
   * @param {any} [params] 参数
   * @returns
   * @memberof HttpService
   */
  post(url: string, params?: any, progress?: boolean): Observable<any> {
    return this.request('POST', url, params, true, progress);
  }

  /**
   * put请求
   *
   * @param {string} url 地址
   * @param {any} [params] 参数
   * @returns
   * @memberof HttpService
   */
  put(url: string, params?: any): Observable<any> {
    return this.request('PUT', url, params);
  }

  /**
   * delete请求
   *
   * @param {string} url 地址
   * @param {any} [params] 参数
   * @returns
   * @memberof HttpService
   */
  delete(url: string, params?: any): Observable<any> {
    return this.request('DELETE', url, params);
  }

  download(url: string, params?: any): Observable<any> {
    let option = {};
    url = `${url}`;

    option = { body: params, responseType: 'blob' };
    this.addHeader(option);
    return this.http.request("POST", url, option);
  }
  /**
   * request通用请求
   *
   * @private
   * @param {string} method 请求类型
   * @param {string} url 地址
   * @param {any} [params] 参数
   * @returns
   * @memberof HttpService
   */
  request(method: string, url: string, params?: any, isBody = false, progress: boolean = false): Observable<any> {
    let option = {};
    url = `${url}`;
    method = method.toUpperCase();
    if (['POST', 'PUT', 'DELETE'].indexOf(method) > -1 || isBody) {
      option = { body: params };
    } else if (['GET'].indexOf(method) > -1) {
      option = { params: params };
    }
    this.addHeader(option);

    if (progress) {
      option = Object.assign(option, {
        reportProgress: true,
        observe: 'events',
      })

      return this.http.request(method, url, option);
    }

    return new Observable((x) => {
      this.http.request(method, url, option).subscribe(
        (y: any) => {
          if (y.success) {
            x.next(y.data);
          } else {
            x.error(y);
            this.handleError(y);
          }
          x.complete();
        },
        (y) => {
          x.error(y);
          x.complete();
          this.handleError(y);
        },
        () => {
          x.complete();
        }
      );
    });
  }

  /**
   * 错误处理
   *
   * @private
   * @param {HttpErrorResponse} error
   * @returns
   * @memberof HttpService
   */
  handleError(error: any) {
    let message = "";
    if (!!error?.msgInfo) {
      message = error.msgInfo;
    } else {
      message = error.error;
    }

    console.error(error);
    return throwError(error.error);
  }

  /**
   * 添加头部信息
   *
   * @private
   * @param {*} option
   * @memberof HttpService
   */
  private addHeader(option: any) {
    // let auth = this.setting.get(Consts.AUTH_KEY);
    // let tenant = this.setting.get(Consts.TENANT_KEY);
    //
    // option['headers'] = {}
    // if (!!auth && !!auth['token']) {
    //   option['headers'] = Object.assign(option['headers'], { Authorization: auth['token'] });
    // }
    //
    // if (!!tenant && !!tenant['id']) {
    //   option['headers'] = Object.assign(option['headers'], { "X-Tenant-ID": tenant['id'] });
    // }
  }
}
