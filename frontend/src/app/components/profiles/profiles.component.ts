import { Component, OnInit, Renderer2 } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { Router } from '@angular/router';


@Component({
  selector: 'app-profiles',
  imports: [SidebarComponent],
  templateUrl: './profiles.component.html',
  styleUrl: './profiles.component.css'
})
export class ProfilesComponent implements OnInit {

  selectedProfile: string | null = null;
  selectedProfileTitle: string = '';
  isLoading: boolean = false;



  constructor(
    private router: Router,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    this.renderer.listen('document', 'click', (event) => {
      const target = event.target as HTMLElement;
      if (target.closest('.profile-card')) {
        this.createClickParticles(event.clientX, event.clientY);
      }
    });
  }

  /**
   * Sélectionne un profil utilisateur
   */
  selectProfile(profileId: string, profileTitle: string): void {
    this.selectedProfile = profileId;
    this.selectedProfileTitle = profileTitle;
    
    // Effet de vibration (si supporté)
    if (navigator.vibrate) {
      navigator.vibrate(50);
    }

    // Optionnel : Sauvegarder le profil sélectionné
    this.saveSelectedProfile(profileId);
  }

  /**
   * Gestion des effets de survol des cartes
   */
  onCardHover(event: MouseEvent, isEntering: boolean): void {
    const card = event.currentTarget as HTMLElement;
    
    if (isEntering && !card.classList.contains('selected')) {
      this.renderer.setStyle(card, 'transform', 'translateY(-5px) scale(1.02)');
    } else if (!isEntering && !card.classList.contains('selected')) {
      this.renderer.setStyle(card, 'transform', 'translateY(0) scale(1)');
    }
  }

  /**
   * Continue avec le profil sélectionné
   */
  continueWithProfile(): void {
    if (!this.selectedProfile) return;

    this.isLoading = true;

    // Simulation d'un délai de chargement
    setTimeout(() => {
      this.navigateToDashboard();
    }, 1000);
  }

  /**
   * Navigue vers le tableau de bord du profil sélectionné
   */
  private navigateToDashboard(): void {
    if (this.selectedProfile) {
      // Navigation vers le dashboard spécifique au profil
      this.router.navigate(['/dashboard', this.selectedProfile]);
      
      // Ou vers une route générale avec des paramètres
      // this.router.navigate(['/dashboard'], { 
      //   queryParams: { profile: this.selectedProfile } 
      // });
    }
  }

  /**
   * Sauvegarde le profil sélectionné (localStorage, service, etc.)
   */
  private saveSelectedProfile(profileId: string): void {
    // Exemple avec localStorage
    localStorage.setItem('selectedProfile', profileId);
    
    // Ou avec un service Angular
    // this.profileService.setSelectedProfile(profileId);
  }

 

  /**
   * Crée un effet de particules lors du clic
   */
  private createClickParticles(x: number, y: number): void {
    const particleCount = 6;
    
    for (let i = 0; i < particleCount; i++) {
      const particle = this.renderer.createElement('div');
      
      // Style des particules
      this.renderer.setStyle(particle, 'position', 'fixed');
      this.renderer.setStyle(particle, 'left', `${x}px`);
      this.renderer.setStyle(particle, 'top', `${y}px`);
      this.renderer.setStyle(particle, 'width', '4px');
      this.renderer.setStyle(particle, 'height', '4px');
      this.renderer.setStyle(particle, 'background', 'white');
      this.renderer.setStyle(particle, 'border-radius', '50%');
      this.renderer.setStyle(particle, 'pointer-events', 'none');
      this.renderer.setStyle(particle, 'z-index', '1000');
      
      // Calcul de la trajectoire
      const angle = (i / particleCount) * Math.PI * 2;
      const velocity = 50;
      const vx = Math.cos(angle) * velocity;
      const vy = Math.sin(angle) * velocity;
      
      // Ajout au DOM
      this.renderer.appendChild(document.body, particle);
      
      // Animation
      this.animateParticle(particle, x, y, vx, vy);
    }
  }

  /**
   * Anime une particule
   */
  private animateParticle(
    particle: HTMLElement, 
    startX: number, 
    startY: number, 
    vx: number, 
    vy: number
  ): void {
    let opacity = 1;
    let currentX = startX;
    let currentY = startY;
    
    const animate = () => {
      opacity -= 0.02;
      currentX += vx * 0.02;
      currentY += vy * 0.02;
      
      this.renderer.setStyle(particle, 'opacity', opacity.toString());
      this.renderer.setStyle(particle, 'left', `${currentX}px`);
      this.renderer.setStyle(particle, 'top', `${currentY}px`);
      
      if (opacity > 0) {
        requestAnimationFrame(animate);
      } else {
        this.renderer.removeChild(document.body, particle);
      }
    };
    
    requestAnimationFrame(animate);
  }

  /**
   * Méthode pour réinitialiser la sélection
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

}
