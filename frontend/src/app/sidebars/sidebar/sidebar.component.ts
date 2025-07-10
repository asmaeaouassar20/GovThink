import { NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';


// L'interface utilisée pour le menu de navigation
interface MenuItem{
  titre : string;
  icon  : string;
  lien : string;
}
@Component({
  selector: 'app-sidebar',
  imports: [NgFor, RouterLink],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {


  menuItems : MenuItem[] = [
      { titre: 'Dashboard', icon: 'dashboard', lien: '/dashboard' },
    { titre:'Utilisateurs', icon:'people', lien:'/users'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},
    { titre:'Paramètres', icon:'settings', lien:'/settings'},

  ]

}
