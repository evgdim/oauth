import { Component, OnInit } from '@angular/core';
import { AppService } from 'src/services/app.service';

import { AuthService } from "angularx-social-login";
import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  

  constructor(private authService: AuthService) {}
  title = 'frontend';

  ngOnInit(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
   
    this.authService.authState.subscribe((user) => {
      console.log(user);
    });
  }
}
