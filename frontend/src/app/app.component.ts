import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from "./home/home.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  Count : number = 10;

  ShowMessage(msg:string) : string{
    return msg;
  }
  IncreaseCount(num:number){
    this.Count+=num;
  }
  DecreaseCount(num:number){
    this.Count-=num;
  }

}





