import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})


export class AuthService {

  uri = 'http://localhost:8080';
  token: string;

  constructor(private http: HttpClient, private router: Router) { };


  login(user: string, password: string) {
    let params: HttpParams = new HttpParams();
    params = params.set('user', user).set('password', password);
    
    this.http.post(this.uri + '/login/', null, {"params":params})
      .subscribe(
        (resp: any) => {
          console.log("respuesta", resp);
          this.router.navigate(['users']);
          localStorage.setItem('auth_token', resp.token);
        })
  }

  // borra el token del almacenamiento local
  logout() {
    localStorage.removeItem('token');
  }

  // devuelve un boleano que determina si un usuario está autenticado
  public get logIn(): boolean {
    return (localStorage.getItem('token') !== null);
  }

}





