import { ViewportScroller } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,       // Pour accéder aux fragments d'URL
    private router: Router,             // Pour modifier l'URL
    private viewportScroller: ViewportScroller // Pour gérer le scroll
  ) {}

  ngOnInit() {
    // On écoute les changements de fragment dans l'URL
    this.route.fragment.subscribe(fragment => {
      if (fragment) {
        // Timeout nécessaire pour la stabilité du DOM
        setTimeout(() => {
          // Scroll vers l'élément avec l'ID correspondant
          this.viewportScroller.scrollToAnchor(fragment);
          
          // Nettoyage de l'URL après le scroll
          this.router.navigate([], {
            fragment: undefined,    // Supprime le fragment (#section)
            replaceUrl: true,      // Évite d'ajouter une entrée d'historique
            queryParamsHandling: 'preserve' // Garde les paramètres existants
          });
        }, 0); // Délai minimal
      }
    });
  }
}




