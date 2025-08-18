import { ViewportScroller } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';



@Component({
  selector: 'app-home',
  imports: [RouterLink, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit, OnDestroy {

   private destroy$ = new Subject<void>();
  constructor(
    private route: ActivatedRoute,       // Pour accéder aux fragments d'URL
    private router: Router,             // Pour modifier l'URL
    private viewportScroller:ViewportScroller
  ) {}

 ngOnInit() {
    // Écouter les changements de fragment dans l'URL
    this.route.fragment
      .pipe(takeUntil(this.destroy$))
      .subscribe(fragment => {
        if (fragment) {
          this.handleFragmentNavigation(fragment);
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
/*
   * Gère la navigation vers une section spécifique
   * @param sectionId - ID de la section cible
   */
  scrollTo(sectionId: string) {
    // Mettre à jour l'URL avec le fragment
    this.router.navigate([], { 
      fragment: sectionId,
      replaceUrl: false // Garde l'historique pour le bouton retour
    });
    
    // Effectuer le défilement
    this.performScroll(sectionId);
  }


 /**
   * Gère la navigation basée sur le fragment d'URL
   * @param fragment - Fragment de l'URL
   */
  private handleFragmentNavigation(fragment: string) {
    // Attendre que le DOM soit stable
    setTimeout(() => {
      this.performScroll(fragment);
      
      // Optionnel : nettoyer l'URL après le défilement
      // Décommentez si vous voulez supprimer le fragment après navigation
      /*
      setTimeout(() => {
        this.router.navigate([], {
          fragment: undefined,
          replaceUrl: true,
          queryParamsHandling: 'preserve'
        });
      }, 1000); // Attendre 1 seconde après le défilement
      */
    }, 100); // Délai pour s'assurer que le DOM est rendu
  }

  /**
   * Effectue le défilement vers l'élément cible
   * @param elementId - ID de l'élément cible
   */
  private performScroll(elementId: string) {
    const element = document.getElementById(elementId);
    
    if (element) {
      // Méthode 1 : Utiliser ViewportScroller (recommandé par Angular)
      this.viewportScroller.scrollToAnchor(elementId);
      
      // Méthode alternative si ViewportScroller ne fonctionne pas bien
      // element.scrollIntoView({ 
      //   behavior: 'smooth', 
      //   block: 'start',
      //   inline: 'nearest'
      // });
    } else {
      console.warn(`Élément avec l'ID "${elementId}" non trouvé`);
    }
  }

  /**
   * Navigation vers le haut de la page
   */
  scrollToTop() {
    this.viewportScroller.scrollToPosition([0, 0]);
    // Supprimer le fragment de l'URL
    this.router.navigate([], {
      fragment: undefined,
      replaceUrl: true,
      queryParamsHandling: 'preserve'
    });
  }

}