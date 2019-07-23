import { Component, OnInit } from '@angular/core';
import { AppService } from 'src/services/app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  

  constructor(private appService: AppService) {}
  title = 'frontend';

  ngOnInit(): void {
    this.appService.obtainAccessToken()
    this.title = this.appService.getAccessToken()
  }
}
