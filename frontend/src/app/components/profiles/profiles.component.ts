import { Component, ElementRef, OnInit, Renderer2, ViewChild } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";


@Component({
  selector: 'app-profiles',
  imports: [SidebarComponent],
  templateUrl: './profiles.component.html',
  styleUrl: './profiles.component.css'
})
export class ProfilesComponent {

  @ViewChild('voirplusmodal') model : ElementRef | undefined;
 
  /*  Variables de l'état du composant  */
  selectedProfile: string | null = null;
  selectedProfileTitle: string = '';
  isLoading: boolean = false;


  // Injection des servces Angular nécessaires
  constructor(
    private renderer: Renderer2 // Permet de manipuler le DOM de façon sécurisée
  ) {}





  /**
   * ** Fonction appelée lorsqu'un utilisateur sélectionne un profil **
   * Elle met à jour les données du profil sélectionné et déclenche une vibration
   */
  selectProfile(profileId: string, profileTitle: string): void {
    // Mise à jour des données
    this.selectedProfile = profileId;
    this.selectedProfileTitle = profileTitle;
  }

  /**
   * ** Gestion des effets de survol des cartes **
   * -> Fonction appelée quand la souris survole une carte (ou la quitte)
   * -> Permet d'ajouter une animation de survol (hover)
   */
  onCardHover(event: MouseEvent, isEntering: boolean): void {
    // Récupération de l'élément HTML cible (la carte survolée)
    const card = event.currentTarget as HTMLElement;
    
    // Si la souris entre et que la carte n'est pas sélectionnée, on applique une animation
    if (isEntering && !card.classList.contains('selected')) {
      this.renderer.setStyle(card, 'transform', 'translateY(-5px) scale(1.02)');
    }
    // Si la souris quitte la carte, on remet la carte à son état initial
    else if (!isEntering && !card.classList.contains('selected')) {
      this.renderer.setStyle(card, 'transform', 'translateY(0) scale(1)');
    }
  }





  /**
   * Méthode pour réinitialiser la sélection : Remet toutes les propriétés de sélection à leur état initial
   */
  resetSelection(): void {
    this.selectedProfile = null;
    this.selectedProfileTitle = '';
    this.isLoading = false;
  }

  /**
   * Getter pour vérifier si un profil est sélectionné
   */
  get hasSelectedProfile(): boolean {
    return this.selectedProfile !== null;
  }















  openPopup(){
      if(this.model){
        this.model.nativeElement.style.display = 'block';
      }
  } 

  closePopup(){
    if(this.model){
      this.model.nativeElement.style.display='none';
    }
  }






}
