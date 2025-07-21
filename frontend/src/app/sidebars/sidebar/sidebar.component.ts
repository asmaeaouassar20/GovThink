
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
  imports: [RouterLink],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {


  menuItems : MenuItem[] = [
      { titre: 'Accueil ', icon: 'home', lien: '/*' },
    { titre:'Tableaux de bord', icon:'dashboard', lien:'/*'},
    { titre:'Catalogue des jeux de données', icon:'storage', lien:'/*'},
    { titre:'Profils utilisateurs ', icon:'people', lien:'/*'},
    { titre:'Assistant chatbot ', icon:'chat', lien:'/*'},
    { titre:'Forum communautaire', icon:'forum', lien:'/*'},
    { titre:'Ressources et documentation', icon:'library_books', lien:'/*'},
    { titre:'Projets/analyses des utilisateurs', icon:'analytics', lien:'/*'},
    { titre:'À propos / Équipe / Contact', icon:'info', lien:'/*'},
    { titre:'Mentions légales / Politique de confidentialité', icon:'gavel', lien:'/*'},
    { titre:'profil', icon:'account_circle', lien:'/*'},
    { titre:'déconnexion', icon:'logout', lien:'/*'}
  ]


  // le sidebar est au debut large
  reduit = true;

  toggleSidebar(){
    this.reduit=!this.reduit;
  }


  
  

}
