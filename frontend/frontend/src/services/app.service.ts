import { Injectable } from '@angular/core';

@Injectable({providedIn:"root"})
export class AppService {
  
  constructor(){

    }
  
  obtainAccessToken(){
    console.log('asd')
     
  }

  getAccessToken() {
    
  }
 
  isLoggedIn(){
    
    return true;
  } 
 
  logout() {

  }
}