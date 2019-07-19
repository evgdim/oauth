import { HttpService } from './../core/http/http.service';
import { Component, OnInit } from '@angular/core';

import { environment } from '@env/environment';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {
  version: string | null = environment.version;
  http: HttpService;

  constructor(http: HttpService) {
    this.http = http;
  }

  ngOnInit() {
    this.http.get("/user").subscribe(resp => console.log(resp))
  }
}
