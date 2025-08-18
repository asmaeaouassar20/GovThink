import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";

@Component({
  selector: 'app-apropos',
  imports: [SidebarComponent, RouterLink],
  templateUrl: './apropos.component.html',
  styleUrl: './apropos.component.css'
})
export class AproposComponent {

}
